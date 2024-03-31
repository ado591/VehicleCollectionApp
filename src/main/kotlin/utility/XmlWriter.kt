package utility

import data.Vehicle
import org.koin.core.component.KoinComponent
import java.io.FileOutputStream

class XmlWriter : KoinComponent {

    /**
     * writes collection in xml format
     */
    fun write(collection: Collection<Vehicle>, path: String) {
        FileOutputStream(path).use { file ->
            XmlMapperWrapper.xmlObjectMapper.writeValue(file, collection)
        }
    }
}