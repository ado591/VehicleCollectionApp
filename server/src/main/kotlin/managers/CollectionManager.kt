package managers

import data.Vehicle
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayDeque

class CollectionManager(filepath: String) : KoinComponent {
    private val dbManager: DatabaseManager by inject()
    private var loadedCollection: ArrayDeque<Vehicle> = dbManager.loadCollectionFromDB()
    private var collection = Collections.synchronizedList(loadedCollection)
    private val initTime: ZonedDateTime = ZonedDateTime.now()
    private val logger = LogManager.getLogger("logger")
    init {
        logger.info("Инициализирован менеджер коллекций")
    }
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

    fun getCollection(): MutableList<Vehicle> {
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
            .filter { vehicle -> vehicle.id > start }
            .forEach { it.id -= 1 }
        Vehicle.setCurrentId(collection.size.toLong())
    }

}
