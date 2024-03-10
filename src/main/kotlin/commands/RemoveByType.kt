package commands

import response.Response

class RemoveByType(): Command("remove_all_by_type", "удалить из коллекции все элементы, значение поля type которого эквивалентно заданному") {

    override fun execute(args: Array<String>): Response {
        // todo: special message for no element with type found
        TODO("Not yet implemented")
    }
}