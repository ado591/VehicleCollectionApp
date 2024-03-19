package commands

import response.Response
import utility.XmlWriter
import java.io.FileNotFoundException

class Save: Command("save", "сохранить коллекцию в файл") {


    /**
     * Writes the collection in xml representation to a file specified by the filePath argument
     * @param argument a string argument representing the file path to save the collection
     * @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?): Response {
        try {
            val filePath = argument!!
            XmlWriter().write(collectionManager.getCollection(), filePath)
        } catch (e: Exception) { // todo: divide exceptions for IO and file not found?
            return Response("Возникла ошибка при записи в файл")
        } catch(e: NullPointerException) {
            return Response("Не передан путь к файлу")
        }
        //todo: catch jackson exceptions
        return Response("Файл успешно сохранен")
    }
}