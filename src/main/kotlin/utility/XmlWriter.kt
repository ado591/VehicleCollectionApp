package utility

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import console.Console
import data.Vehicle
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import response.Response
import java.io.FileOutputStream
import java.io.IOException
import java.lang.IllegalArgumentException

class XmlWriter: KoinComponent {
    private val console: Console by inject()
    /**
     * writes collection in xml format
     */
    fun write(collection: Collection<Vehicle>, path: String) {
        try {
            val xmlMapper = XmlMapper()
            val file = FileOutputStream(path)
            xmlMapper.writeValue(file, collection)
        } catch (e: IOException) {
            console.print(Response("Возникла ошибка при записи коллекции"))
        } catch (e: IllegalArgumentException) {
            console.print(Response("Неверно указан путь к файлу"))
        }
    }

}