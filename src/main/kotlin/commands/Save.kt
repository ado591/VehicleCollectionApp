package commands

import response.Response
import utility.XmlWriter
import java.io.IOException
import java.util.ResourceBundle

class Save: Command("save", "сохранить коллекцию в файл") {

    override fun execute(args: Array<String>?): Response {
        val filePath = "meow" //todo: implement without magic number
        try {
            XmlWriter().write(collectionManager.getCollection(), filePath)
        } catch (e: IOException) { // todo: divide exceptions for IO and file not found?
            return Response(ResourceBundle.getBundle("error_message").getString("invalid_path"))
        } //todo: catch jackson exceptions
        return Response(ResourceBundle.getBundle("success_message").getString("save"))
    }
}