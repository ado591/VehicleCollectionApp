package commands

import commands.extra.ServerOnly
import model.User
import model.response.Response
import model.response.ResponseType
import utils.xml.XmlWriter
import java.io.FileNotFoundException
import java.io.IOException
import java.util.ResourceBundle



const val DEFAULT_SAVE_PATH = "files/blank.xml"
@ServerOnly
class Save : Command(
    "save",
    ResourceBundle.getBundle("message/info").getString("save_description")
) {


    /**
     * Writes the collection in xml representation to a file specified by the filePath argument
     * @param argument a string argument representing the file path to save the collection
     * @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?, user: User): Response {
        val filePath: String =
            argument ?: DEFAULT_SAVE_PATH
        try {
            XmlWriter().write(collectionManager.getCollection(), filePath)
            logger.info("Файл успешно сохранен")
        } catch (e: FileNotFoundException) {
            logger.fatal("Файл не найден")
        } catch (e: IOException) {
            logger.error("Возникла ошибка при записи коллекции")
        } catch (e: IllegalArgumentException) {
            logger.fatal("Неверно указан путь к файлу")
        }
        return Response("По независящим от вас обстоятельствам сервер завершил работу").apply {
            responseType = ResponseType.EXIT
            statusCode = 0
        }
    }
}