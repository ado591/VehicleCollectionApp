package utility

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import console.Console
import data.Vehicle
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import response.Response
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.exitProcess

class XmlReader(): KoinComponent {
    private val console: Console by inject()
    /**
     * @param filePath -- path to xml file
     * @return collection of vehicles, assigned in xml file
     */
    fun parseDocument(filePath: String): ArrayDeque<Vehicle> {
        val collection: ArrayDeque<Vehicle>
        try {
            val xmlMapper = XmlMapper()
            val file = File(filePath)
            collection = xmlMapper.readValue(file, object : TypeReference<ArrayDeque<Vehicle>>() {})
            return collection
        } catch (e: FileNotFoundException) {
            console.print(Response("Файл не найден"))
            exitProcess(1)
        } catch (e: Exception) {
            console.print(Response("Возникла ошибка при инициализации коллекции"))
            exitProcess(1)
        }
    }
}