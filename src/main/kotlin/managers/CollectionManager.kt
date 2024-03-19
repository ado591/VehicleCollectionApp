package managers

import data.Vehicle
import org.koin.core.component.KoinComponent
import utility.XmlReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayDeque

class CollectionManager(private var filepath: String): KoinComponent {
    private var collection: ArrayDeque<Vehicle> = ArrayDeque() // todo: инициализация через файл
    private val initTime: Long = System.currentTimeMillis()

    init {
        collection = XmlReader().parseDocument(filepath)
    }

    fun add(e: Vehicle) {
        collection.add(e);
    }
    fun getMin(): Vehicle { //todo: тут может быть null, поправить
        return collection.minBy { it }
    }
    fun clear() {
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
    fun getById(id: Int): Vehicle { //todo: method is redundant
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
}
