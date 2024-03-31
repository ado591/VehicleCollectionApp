package utility

import com.fasterxml.jackson.core.type.TypeReference
import console.Console
import console.closeWithCleanup
import data.Vehicle
import exceptions.LoaderException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import response.Response
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class XmlReader : KoinComponent {
    private val console: Console by inject()

    /**
     * @param filePath -- path to xml file
     * @return collection of vehicles, assigned in xml file
     */
    fun parseDocument(filePath: String): ArrayDeque<Vehicle> {
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
            console.print(Response("Файл не найден"))
            console.getScanner().closeWithCleanup(1)
        } catch (e: LoaderException) {
            console.print(Response("В файле обнаружены некорректные поля"))
            console.getScanner().closeWithCleanup(1)
        } catch (e: IOException) {
            console.print(Response("Возникла ошибка при инициализации коллекции"))
            console.getScanner().closeWithCleanup(1)
        }
        return collection
    }
}