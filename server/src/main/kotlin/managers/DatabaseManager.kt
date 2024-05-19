package managers

import data.Coordinates
import data.FuelType
import data.Vehicle
import data.VehicleType
import database.DbConnection
import exceptions.InvalidPasswordException
import exceptions.UserAlreadyExistsException
import exceptions.UserNotFoundException
import model.User
import org.apache.logging.log4j.LogManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.PassHash
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.system.exitProcess


const val pepper = "2Hq@*!8fdAQl" //todo: сменить
/**
 * Класс, выполняющий действия с базой данных
 */
class DatabaseManager() : KoinComponent {
    private val dbConnection: DbConnection by inject()
    private val connection = dbConnection.getConnection()
    private val logger = LogManager.getLogger("logger")


    /**
     * Загружает коллекцию из базы данных в память
     * @return ArrayDeque с элементами типа Vehicle
     */
    fun loadCollectionFromDB(): ArrayDeque<Vehicle> {
        val collection = ArrayDeque<Vehicle>()
        try {
            val joinStatement = connection.prepareStatement(
                ResourceBundle
                    .getBundle("queries")
                    .getString("add_user")
            )
            val result = joinStatement.executeQuery()
            while (result.next()) {
                val vehicle: Vehicle = extractObjectFromRow(result)
                collection.add(vehicle)
            }
            joinStatement.close()
            logger.info("Коллекция успешно загружена из базы данных")
        } catch (e: SQLException) {
            logger.fatal("При загрузке коллекции из базы данных произошла ошибка")
            exitProcess(-1)
        }
        return collection
    }

    /**
     * Преобразует строку из таблицы vehicle в объект типа Vehicle
     * @return объект типа Vehicle, соответствующей переданной строке
     */
    private fun extractObjectFromRow(resultSet: ResultSet): Vehicle {
        val id: Long = resultSet.getLong("id")
        val name: String = resultSet.getString("vehicle_name") //todo: делать переменные???
        val coordinates = Coordinates(x = resultSet.getInt("x"), y = resultSet.getFloat("y"))
        val creationDate: ZonedDateTime =
            resultSet.getTimestamp("creation_date").toLocalDateTime().atZone(ZoneId.of("Europe/Moscow"))
        val enginePower: Double = resultSet.getDouble("engine_power")
        val fuelConsumption: Int = resultSet.getInt("fuel_consumption")
        val vehicleType: VehicleType = VehicleType.valueOf(
            resultSet.getString("vehicle_type")
                .uppercase()
        )
        val fuelType: FuelType = FuelType.valueOf(
            resultSet.getString("fuel_type")
                .uppercase()
        )
        return Vehicle(id, name, coordinates, creationDate, enginePower, fuelConsumption, vehicleType, fuelType)
    }


    /**
     * Проверяет существование пользователя с заданным логином
     */
    private fun userExists(user: User): Boolean {
        val hashedPassword: String = PassHash.encryptString(user.password + pepper)
        val checkUserStatement: PreparedStatement = connection.prepareStatement(
            ResourceBundle
                .getBundle("queries").getString("check_user")
        )
        checkUserStatement.setString(1, user.username)
        checkUserStatement.setString(2, hashedPassword)
        val result: ResultSet = checkUserStatement.executeQuery()
        result.next()
        val count = result.getInt(1)
        checkUserStatement.close()
        return count == 1
    }


    /**
     * Добавляет пользователя в базу данных
     */
    fun registerUser(user: User) {
        if (userExists(user)) {
            logger.error("User with username $user already exists")
            throw UserAlreadyExistsException()
        }
        val addUserStatement: PreparedStatement = connection
            .prepareStatement(
                ResourceBundle
                    .getBundle("queries")
                    .getString("add_user")
            )
        addUserStatement.setString(1, user.username)
        addUserStatement.setString(2, user.password)
        addUserStatement.executeUpdate()
        addUserStatement.close()
    }

    /**
     * Проверяет, что пользователь с таким логином и паролем существует
     * @throws InvalidPasswordException если пользователь существует, но пароль не совпадает
     * @throws UserNotFoundException если пользователя с таким именем не существует
     */
    fun login(user: User) {
        if (!userExists(user)) {
            logger.error("Could not found user with username=${user.username}")
            throw UserNotFoundException()
        }
        val getPasswordStatement: PreparedStatement =
            connection.prepareStatement(ResourceBundle.getBundle("queries").getString("get_pass"))
        val result = getPasswordStatement.executeQuery().getString("password")
        if (result != user.password) {
            logger.error("Invalid password was given for user with username=${user.username}")
            throw InvalidPasswordException()
        }
    }

    /**
     * Добавляет в базу данных координаты переданного объекта
     * @return id координат
     */
    private fun addCoordinates(coordinates: Coordinates): Int {
        val preparedStatement = connection.prepareStatement(
            ResourceBundle.getBundle("queries").getString("add_coordinates"),
            PreparedStatement.RETURN_GENERATED_KEYS
        )
        preparedStatement.setInt(1, coordinates.x)
        preparedStatement.setFloat(2, coordinates.y)
        preparedStatement.executeUpdate()
        var coordinatesId: Int = -1
        val generatedKeys: ResultSet = preparedStatement.generatedKeys
        if (generatedKeys.next()) {
            coordinatesId = generatedKeys.getInt(1)
        }
        return coordinatesId
    }

    /**
     * Возвращает id пользователя в базе данных по логину
     * @param user пользователь
     * @return id пользователя в таблице users
     */
    private fun getUserIdByLogin(user: User): Int {
        var userId = -1
        if (userExists(user)) {
            try {
                val getUserIdStatement =
                    connection.prepareStatement("SELECT id FROM users WHERE username = ?") //todo: кинуть в ресурсы
                getUserIdStatement.setString(1, user.username)
                val result: ResultSet = getUserIdStatement.executeQuery()

                if (result.next()) {
                    userId = result.getInt("id")
                    getUserIdStatement.close()
                }
            } catch (e: SQLException) {
                logger.error("Error while checking user in database")
            }
        } else {
            throw UserNotFoundException()
        }
        return userId
    }

    /**
     * Добавляет объект типа Vehicle
     * @throws SQLException
     */
    fun addVehicle(vehicle: Vehicle, user: User) {
        try {
            val preparedStatement: PreparedStatement =
                connection.prepareStatement(ResourceBundle.getBundle("queries").getString("add_vehicle"))
            preparedStatement.setString(1, vehicle.name)
            preparedStatement.setInt(2, addCoordinates(vehicle.coordinates))
            preparedStatement.setObject(3, vehicle.creationDate)
            preparedStatement.setDouble(4, vehicle.enginePower)
            preparedStatement.setInt(5, vehicle.fuelConsumption)
            preparedStatement.setString(6, vehicle.type.name)
            preparedStatement.setString(7, vehicle.fuelType.name)
            preparedStatement.setInt(8, getUserIdByLogin(user))
        } catch (e: SQLException) {
            logger.error("Error while inserting vehicle to database")
        }
    }

    /**
     * Проверяет, создан ли объект указанным пользователем
     * @param index индекс объекта для проверки
     * @param user пользователь, который запрашивает доступ к объекту
     * @return true, если пользователь создал объект, false иначе
     */
    fun checkCreator(index: Long, user: User): Boolean {
        val userId = getUserIdByLogin(user)
        if (userId == -1) {
            throw UserNotFoundException()
        }
        var createdBy = -1
        try {
            val checkCreatorStatement = connection.prepareStatement("SELECT created_by FROM vehicle WHERE id = ?")
            checkCreatorStatement.setLong(1, index)
            val result: ResultSet = checkCreatorStatement.executeQuery()

            if (result.next()) {
                createdBy = result.getInt("created_by")
            }

            checkCreatorStatement.close()
        } catch (e: SQLException) {
            logger.error("Error while checking creator in database")
        }

        return createdBy == userId
    }

    /**
     * Обновляет элемент в базе данных по указанному индексу
     */
    fun updateVehicle(vehicle: Vehicle, index: Long, user: User) {
        val preparedStatement: PreparedStatement = connection.prepareStatement(
            ResourceBundle.getBundle("queries").getString("update_vehicle")
        )
        preparedStatement.setString(1, vehicle.name)
        preparedStatement.setObject(2, vehicle.creationDate)
        preparedStatement.setDouble(3, vehicle.enginePower)
        preparedStatement.setInt(4, vehicle.fuelConsumption)
        preparedStatement.setString(5, vehicle.type.name)
        preparedStatement.setString(6, vehicle.fuelType.name)
        preparedStatement.setInt(7, getUserIdByLogin(user))
        preparedStatement.setLong(8, index)
        preparedStatement.setInt(9, vehicle.coordinates.x)
        preparedStatement.setFloat(10, vehicle.coordinates.y)
        preparedStatement.executeUpdate()
        connection.close()
    }

    /**
     * Удаляет элемент по указанному индексу из базы данных
     */
    fun removeVehicle(index: Long) {
        val preparedStatement: PreparedStatement = connection.prepareStatement(
            ResourceBundle.getBundle("queries")
                .getString("remove_element")
        )
        preparedStatement.executeUpdate()
        connection.close()
    }

    /**
     * Очищает таблицу с элементами Vehicle
     */
    fun clearVehicles() {
        val preparedStatement: PreparedStatement =
            connection.prepareStatement(ResourceBundle.getBundle("queries").getString("clear_table"))
        preparedStatement.executeUpdate()
        connection.close()
    }

    /**
     * Удаляет все элементы из базы данных, чей тип равен заданному
     */
    fun removeByType(vehicleType: VehicleType) {
        val preparedStatement: PreparedStatement = connection.prepareStatement(
            ResourceBundle.getBundle("queries").getString("remove_by_type")
        )
        preparedStatement.setString(1, vehicleType.name)
        preparedStatement.executeUpdate()
        connection.close()
    }
}