package commands

import response.Response
import utility.XmlWriter

class Save: Command("save", "сохранить коллекцию в файл") {

    override fun execute(argument: String): Response {
        val filePath = argument
        try {
            XmlWriter().write(collectionManager.getCollection(), filePath)
        } catch (e: Exception) { // todo: divide exceptions for IO and file not found?
            return Response("Возникла ошибка при записи в файл")
        } //todo: catch jackson exceptions
        return Response("Файл успешно сохранен")
    }
}