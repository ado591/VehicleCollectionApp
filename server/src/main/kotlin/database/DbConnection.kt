package database

import org.apache.logging.log4j.LogManager
import org.koin.core.component.KoinComponent
import java.io.File
import java.sql.*
import kotlin.system.exitProcess


class DbConnection : KoinComponent {

    private val databaseName = System.getenv("DB_NAME") // studs

    private val logger = LogManager.getLogger("logger")

    private val username = System.getenv("HELIOS_LOGIN") // sXXXXXX


    private val password = System.getenv("HELIOS_PASS") // пароль из ./pgpass


    private val url = "jdbc:postgresql://127.0.0.1:5432/$databaseName"
    private val connection: Connection by lazy {
        DriverManager.getConnection(url, username, password).apply {
            try {
                val script = File("server/src/main/kotlin/database/schema.sql").readText()
                createStatement().use { statement ->
                    for (sql in script.split(";")) {
                        statement.execute(sql)
                    }
                }
            } catch (e: SQLException) {
                logger.fatal("При инициализации базы данных произошла ошибка")
                exitProcess(1)
            } catch (e: ClassNotFoundException) {
                logger.fatal("Не найден драйвер базы данных")
                exitProcess(1)
            }
        }
    }

    fun getConnection(): Connection {
        return connection
    }

}