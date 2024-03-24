package managers

import data.Vehicle
import org.koin.core.component.KoinComponent
import utility.XmlReader
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CollectionManager(filepath: String): KoinComponent {
    private var collection: ArrayDeque<Vehicle> = XmlReader().parseDocument(filepath)
    private val initTime: ZonedDateTime = ZonedDateTime.now()

    fun add(e: Vehicle) {
        collection.add(e)
    }
    fun getMin(): Vehicle {
       return collection.minBy { it }
    }
    fun clear() {
        collection.clear()
    }
    fun isEmpty(): Boolean {
        return collection.isEmpty()
    }
    fun head(): Vehicle {
        try {
            return collection.first()
        } catch(e: IndexOutOfBoundsException) {
            throw e
        }
    }

    fun getInitTime(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss z")
        return initTime.format(formatter)
    }

    fun getCollection(): ArrayDeque<Vehicle> {
        return collection
    }
    fun getSize(): Int {
        return collection.size
    }
    fun removeById(id: Int) {
        try {
            collection.removeAt(id)
        } catch (e: IndexOutOfBoundsException) {
            throw e
        }
    }

    fun update(id: Int, element: Vehicle) {
        try {
            collection[id] = element
        } catch (e: IndexOutOfBoundsException) {
            throw e
        }
    }

    /**
     * generates new IDs for the whole collection
     */
    fun rearrange() {
        var newId: Long = 1
        collection
            .forEach{it.id = newId++}
    }

    /**
     * generates new IDs for all elements after removed
     * @param start -- start id for generating
     */

    fun rearrange(start: Int) {
        collection
            .filter { vehicle -> vehicle.id >= start }
            .forEach{ it.id -= 1 }
    }
}
