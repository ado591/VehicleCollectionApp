package utils.xml

import com.fasterxml.jackson.core.type.TypeReference
import data.Vehicle
import exceptions.LoaderException
import org.apache.logging.log4j.LogManager
import org.koin.core.component.KoinComponent
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.system.exitProcess

class XmlReader : KoinComponent {

    /**
     * @param filePath -- path to xml file
     * @return collection of vehicles, assigned in xml file
     */
    fun parseDocument(filePath: String): ArrayDeque<Vehicle> {
        val collection: ArrayDeque<Vehicle> = ArrayDeque()
        val logger = LogManager.getLogger("logger")
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
            logger.fatal("File was not found")
            exitProcess(1)
        } catch (e: LoaderException) {
            logger.fatal("File contains invalid item")
            exitProcess(1)
        } catch (e: IOException) {
            logger.fatal("Error occurred while collection initialization")
            exitProcess(1)
        }
        logger.info("Collection was parsed from XML file")
        return collection
    }
}