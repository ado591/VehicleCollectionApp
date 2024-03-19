package utility

import data.Vehicle
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.core.type.TypeReference
import console.Console
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import response.Response
import java.io.File
import kotlin.system.exitProcess

class XmlReader(): KoinComponent {
    /**
     * @param filepath -- path to xml file
     * @return collection of vehicles, assigned in xml file
     */
    fun parseDocument(filepath: String): ArrayDeque<Vehicle> {
        val console: Console by inject()
        var collection: ArrayDeque<Vehicle>
        try {
            val xmlMapper = XmlMapper()
            val file = File(filepath)
            collection = xmlMapper.readValue(file, object : TypeReference<ArrayDeque<Vehicle>>() {})
            return collection
        } catch (e: Exception) {
            console.print(Response("Возникла ошибка при инициализации коллекции"))
            exitProcess(1)
        }
    }
}