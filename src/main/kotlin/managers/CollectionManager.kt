package managers

import data.Vehicle
import org.koin.core.component.KoinComponent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayDeque

class CollectionManager(): KoinComponent {
    private lateinit var collection: ArrayDeque<Vehicle>
    private val initTime: Long = System.currentTimeMillis()

    //todo: I've just removed everything from here to make Koin great again
    fun add(e: Vehicle): Unit = TODO()
    fun getMin(): Vehicle = TODO()
    fun clear(): Unit = TODO()
    fun isEmpty(): Boolean = TODO()
    fun getById(id: Int): Vehicle = TODO()

    fun getInitTime(): String {
        val moscowTimeZone = TimeZone.getTimeZone("Europe/Moscow")
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        dateFormat.timeZone = moscowTimeZone
        return dateFormat.format(Date(initTime))
    }

    fun getCollection(): ArrayDeque<Vehicle> = TODO()
    fun getSize(): Int = TODO()
    fun removeById(id: Int): Unit = TODO()
}
