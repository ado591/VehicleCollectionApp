package managers

import data.Vehicle
import org.koin.core.component.KoinComponent
import utility.XmlReader
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CollectionManager(filepath: String) : KoinComponent {
    private var collection: ArrayDeque<Vehicle> = XmlReader().parseDocument(filepath)
    private val initTime: ZonedDateTime = ZonedDateTime.now()

    /**
     * adds element to the collection
     */
    fun add(e: Vehicle) {
        collection.add(e)
    }

    /**
     * @return minimum of the collection
     */
    fun getMin(): Vehicle {
        return collection.min()
    }

    /**
     * clears current collection
     */
    fun clear() {
        collection.clear()
    }

    fun isEmpty(): Boolean {
        return collection.isEmpty()
    }

    /**
     * @return first element of the collection
     */
    fun head(): Vehicle? {
        return collection.firstOrNull()
    }

    /**
     * @return time of initialization of current collection
     */

    fun getInitTime(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss z")
        return initTime.format(formatter)
    }

    /**
     * @return current collection
     */

    fun getCollection(): ArrayDeque<Vehicle> {
        return collection
    }

    /**
     * @return size of the collection
     */
    fun getSize(): Int {
        return collection.size
    }

    fun inBounds(id: Int): Boolean {
        return id >= 0 && id < collection.size
    }

    /**
     * removes element from the collection
     * @param id - id of the element to be removed
     */
    fun removeById(id: Int) {
        collection.removeAt(id)
    }

    /**
     * updates element in collection
     * @param id - id of the element
     * @param element - new element
     */

    fun update(id: Int, element: Vehicle) {
        collection[id] = element
        Vehicle.setCurrentId(collection.size.toLong())
    }

    /**
     * Generating new IDs of each vehicle
     */
    fun rearrange() {
        var newId: Long = 1
        collection
            .forEach { it.id = newId++ }
        Vehicle.setCurrentId(collection.size.toLong())
    }

    /**
     * Generating new IDs of each vehicle that has an id greater than or equal to the specified start value
     * @param start -- start id for generating
     */

    fun rearrange(start: Int) {
        collection
            .filter { vehicle -> vehicle.id >= start }
            .forEach{ it.id -= 1 }
        Vehicle.setCurrentId(collection.size.toLong())
    }

}
