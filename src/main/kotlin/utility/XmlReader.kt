package utility

import com.fasterxml.jackson.core.type.TypeReference
import console.Console
import console.closeWithCleanup
import data.Vehicle
import exceptions.LoaderException
import model.response.ErrorResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import model.response.Response
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class XmlReader : KoinComponent { //todo: а эти классы куда? server или common?
    private val console: Console by inject()

    /**
     * @param filePath -- path to xml file
     * @return collection of vehicles, assigned in xml file
     */
    fun parseDocument(filePath: String): ArrayDeque<Vehicle> { // todo: тут происходит инициализация,хотя нужны response
        val collection: ArrayDeque<Vehicle> = ArrayDeque()
        try {
            val xmlMapper = XmlMapperWrapper.xmlObjectMapper
            val file = File(filePath)
            FileInputStream(file).use { fis ->
                BufferedInputStream(fis).use { bis ->
                    collection.addAll(xmlMapper.readValue(bis, object : TypeReference<ArrayDeque<Vehicle>>() {}))
                }
            }
            for (item in collection) {
                if (!item.isValid()) {
                    throw LoaderException()
                }
            }
        } catch (e: FileNotFoundException) {
            return ErrorResponse("Файл не найден", isFatal = true)
            //console.print(Response("Файл не найден"))
            //console.getScanner().closeWithCleanup(1)
        } catch (e: LoaderException) {
            return ErrorResponse("В файле обнаружены некорректные поля", isFatal = true)
            //console.print(Response("В файле обнаружены некорректные поля"))
            //console.getScanner().closeWithCleanup(1)
        } catch (e: IOException) {
            return ErrorResponse("Возникла ошибка при инициализации коллекции", isFatal = true)
            //console.print(Response("Возникла ошибка при инициализации коллекции"))
            //console.getScanner().closeWithCleanup(1)
        }
        return collection
    }
}