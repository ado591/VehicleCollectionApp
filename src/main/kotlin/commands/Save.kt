package commands

import response.Response
import utility.XmlWriter
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class Save: Command("save",
    ResourceBundle.getBundle("message/info").getString("save_description")) {


    /**
     * Writes the collection in xml representation to a file specified by the filePath argument
     * @param argument a string argument representing the file path to save the collection
     * @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?): Response {
        try {
            val filePath = argument!!
            XmlWriter().write(collectionManager.getCollection(), filePath)
        } catch (e: FileNotFoundException) {
            return Response("Файл не найден")
        } catch (e: IOException) {
            return Response("Возникла ошибка при записи в файл")
        } catch(e: NullPointerException) {
            return Response("Не передан путь к файлу")
        }
        return Response("Файл успешно сохранен")
    }
}