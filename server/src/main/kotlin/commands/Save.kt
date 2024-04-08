package commands

import exceptions.InvalidArgumentException
import model.response.ErrorResponse
import model.response.Response
import model.response.SuccessResponse
import utility.XmlWriter
import java.io.FileNotFoundException
import java.io.IOException
import java.util.ResourceBundle

class Save : Command(
    "save",
    ResourceBundle.getBundle("message/info").getString("save_description")
) {


    /**
     * Writes the collection in xml representation to a file specified by the filePath argument
     * @param argument a string argument representing the file path to save the collection
     * @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?): Response { // todo: недоступна для пользователя
        val filePath: String =
            argument ?: throw InvalidArgumentException("Не передан путь к файлу")
        try {
            XmlWriter().write(collectionManager.getCollection(), filePath)
        } catch (e: FileNotFoundException) {
            return ErrorResponse("Файл не найден", isFatal = false)
        } catch (e: IOException) {
            return ErrorResponse("Возникла ошибка при записи коллекции", isFatal = false)
        } catch (e: IllegalArgumentException) {
            return ErrorResponse("Неверно указан путь к файлу", isFatal = false)
        }
        return SuccessResponse("Файл успешно сохранен")
    }
}