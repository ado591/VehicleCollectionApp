package managers

import data.Coordinates
import data.FuelType
import data.Vehicle
import data.VehicleType
import exceptions.users.InvalidPasswordException
import exceptions.users.UserAlreadyExistsException
import exceptions.users.UserNotFoundException
import model.User
import org.apache.logging.log4j.LogManager
import org.koin.core.component.KoinComponent
import utils.PassHash
import java.io.File
import java.io.FileNotFoundException
import java.sql.*
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.system.exitProcess


const val DEFAULT_SQL_PATH = "server/src/main/kotlin/database/schema.sql"
const val pepper = "2Hq@*!8fdAQl"

/**
 * Класс, выполняющий действия с базой данных
 */
class DatabaseManager : KoinComponent {
    private val databaseName = System.getenv("DB_NAME") // studs
    private val username = System.getenv("HELIOS_LOGIN") // sXXXXXX
    private val password = System.getenv("HELIOS_PASS") // пароль из ./pgpass
    private val url = "jdbc:postgresql://127.0.0.1:5432/$databaseName"

    private var connection: Connection
    private val logger = LogManager.getLogger("logger")

    init {
        connection = connectToDatabase()
        initTables(DEFAULT_SQL_PATH)
    }


    private fun connectToDatabase(): Connection {
        val res = DriverManager.getConnection(url, username, password)
        logger.info("Подключились")
        return res
    }


    private fun initTables(pathToScript: String) {
        try {
            val script = File(pathToScript).readText()
            /*for (sql in script.split(";")) {
                logger.info(sql)
                logger.info("Trying to execute $sql")
                val statement = connection.createStatement()
                statement.execute("$sql;")
                //connection.commit()
                logger.info("Executing statement was successful")
            }*/
            val statement = connection.createStatement()
            statement.execute(script)
        } catch (e: FileNotFoundException) {
            logger.error("Could not find file")
            connection.close()
            exitProcess(1)
        } catch (e: SQLException) {
            logger.error("Error while creating database")
            logger.error(e.message)
            connection.close()
            exitProcess(1)
        }
        logger.info("Database initialization was finished")
    }

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
                    .getString("get_vehicles")
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
            logger.fatal(e.message)
            exitProcess(1)
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
        val checkUserStatement: PreparedStatement = connection.prepareStatement(
            ResourceBundle
                .getBundle("queries").getString("user_exists")
        )
        checkUserStatement.setString(1, user.username)
        val result: ResultSet = checkUserStatement.executeQuery()
        return result.next()
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
        val passHash = PassHash.encryptString(user.password ?: throw InvalidPasswordException())
        val salt = PassHash.generateSalt()
        val hashedPassword = pepper + passHash + salt
        addUserStatement.setString(1, user.username)
        addUserStatement.setString(2, hashedPassword)
        addUserStatement.setString(3, salt)
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
        getPasswordStatement.setString(1, user.username)
        val resultSet = getPasswordStatement.executeQuery()
        val requestPassword = user.password
        if (resultSet.next()) {
            val password = resultSet.getString("pass")
            val salt = resultSet.getString("salt")
            val hashedPassword =
                pepper + PassHash.encryptString(requestPassword ?: throw InvalidPasswordException()) + salt
            if (hashedPassword != password) {
                logger.error("Invalid password was given for user with username=${user.username}. Expected $password, given $hashedPassword")
                throw InvalidPasswordException()
            }
        } else {
            logger.error("User with username=${user.username} not found")
            throw UserNotFoundException()
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
        val preparedStatement: PreparedStatement =
            connection.prepareStatement(ResourceBundle.getBundle("queries").getString("add_vehicle"))
        preparedStatement.setString(1, vehicle.name)
        preparedStatement.setInt(2, addCoordinates(vehicle.coordinates))

        val timestamp = Timestamp.from(vehicle.creationDate.toInstant())
        preparedStatement.setTimestamp(3, timestamp)

        preparedStatement.setDouble(4, vehicle.enginePower)
        preparedStatement.setInt(5, vehicle.fuelConsumption)
        //preparedStatement.setObject(6, vehicle.type)
        //logger.info(vehicle.type.toString())
        preparedStatement.setString(6, vehicle.type.toString())
        preparedStatement.setString(7, vehicle.fuelType.toString())
        preparedStatement.setInt(8, getUserIdByLogin(user))
        preparedStatement.executeUpdate()
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
    }

    /**
     * Удаляет элемент по указанному индексу из базы данных
     */
    fun removeVehicle(index: Long) {
        val preparedStatement: PreparedStatement = connection.prepareStatement(
            ResourceBundle.getBundle("queries")
                .getString("remove_element")
        )
        preparedStatement.setLong(1, index)
        preparedStatement.executeUpdate()
    }

    /**
     * Очищает таблицу с элементами Vehicle
     */
    fun clearVehicles() {
        val preparedStatement: PreparedStatement =
            connection.prepareStatement(ResourceBundle.getBundle("queries").getString("clear_table"))
        preparedStatement.executeUpdate()
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
    }

    fun getIndex(): Int {
        val preparedStatement: PreparedStatement = connection.prepareStatement(
            ResourceBundle.getBundle("queries").getString("get_vehicle_index")
        )
        val result: ResultSet = preparedStatement.executeQuery()
        result.next()
        return result.getInt(1)
    }
}