package utility

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import data.Vehicle
import org.koin.core.component.KoinComponent
import java.io.File

class XmlWriter: KoinComponent {
    //todo: replace with Jackson
    //todo: doesn't work with current gradle build

    /**
     * writes collection in xml format
     */
    fun write(collection: Collection<Vehicle>, path: String): Unit  {
        try {
            val xmlMapper = XmlMapper()
            val file = File(path)
            xmlMapper.writeValue(file, collection)
        } catch (e: Exception) {
            throw e
        }
    }

}