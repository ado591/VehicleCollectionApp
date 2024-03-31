package commands

import data.Vehicle
import exceptions.InvalidArgumentException
import response.Response
import java.util.ResourceBundle

class PrintAsc : Command(
    "print_ascending",
    ResourceBundle.getBundle("message/info").getString("printAsc_description")
) {

    /**
     * Sorts the elements of the original collection in natural order and creates a string representation of each element
     * Returns a Response object containing the composed message
     * @param argument a string argument (should be null)
     * @return a Response object with a message containing string representations of the sorted elements of the collection
     */
    override fun execute(argument: String?): Response {
        val message = StringBuilder()
        val sortedCollection =
            argument?.let { customSorting(it.split(" ")) } ?: collectionManager.getCollection().sorted()
        for (element in sortedCollection) {
            message.appendLine(element.toString())
        }
        return Response(message.toString())
    }

    private fun customSorting(fields: List<String>): List<Vehicle> {
        val fieldsForSorting = fields.map { field ->
            try {
                Vehicle::class.java.getDeclaredField(field)
                field
            } catch (e: NoSuchFieldException) {
                throw InvalidArgumentException("Неизвестное поле: $field")
            }
        } //в итоге здесь только верные поля
        // todo: not finished yet
        // todo: не работает с координатами
        return collectionManager.getCollection().sorted()
    }
}