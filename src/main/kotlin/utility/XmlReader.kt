package utility

import data.Vehicle
import java.time.ZonedDateTime


class XmlReader() {
    //todo: replace with Jackson
    //todo: doesn't work with current gradle build
    

    /**
     * @param filepath -- path to xml file
     * @return collection of vehicles, assigned in xml file
     */
    fun parseDocument(filepath: String): ArrayDeque<Vehicle> {
        TODO()
    }
}