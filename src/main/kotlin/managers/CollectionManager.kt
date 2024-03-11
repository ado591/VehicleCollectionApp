package managers

import data.Vehicle
import org.koin.core.component.KoinComponent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayDeque

class CollectionManager(): KoinComponent {
    private var collection: ArrayDeque<Vehicle> = ArrayDeque() // todo: инициализация через файл
    private val initTime: Long = System.currentTimeMillis()

    fun add(e: Vehicle): Unit {
        collection.add(e);
    }
    fun getMin(): Vehicle { //todo: тут может быть null, поправить
        return collection.minBy { it }
    }
    fun clear(): Unit {
        collection.clear();
    }
    fun isEmpty(): Boolean {
        return collection.isEmpty();
    }
    fun head(): Vehicle {
        try {
            return collection.first()
        } catch(e: IndexOutOfBoundsException) {
            throw e;
        }
    }
    fun getById(id: Int): Vehicle {
        try {
            return collection[id]
        } catch(e: IndexOutOfBoundsException) {
            throw e
        }
    }

    fun getInitTime(): String {
        val moscowTimeZone = TimeZone.getTimeZone("Europe/Moscow")
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        dateFormat.timeZone = moscowTimeZone
        return dateFormat.format(Date(initTime))
    }

    fun getCollection(): ArrayDeque<Vehicle> {
        return collection;
    }
    fun getSize(): Int {
        return collection.size;
    }
    fun removeById(id: Int): Unit {
        try {
            collection.removeAt(id)
        } catch (e: IndexOutOfBoundsException) {
            throw e
        }
    }

    fun update(id: Int, element: Vehicle): Unit {
        try {
            collection[id] = element
        } catch (e: IndexOutOfBoundsException) {
            throw e
        }
    }
}
