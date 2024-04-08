package commands

import commands.extra.Autogeneratable
import exceptions.RecursionScriptException
import model.response.ErrorResponse
import model.response.Response
import utils.CommandParser
import java.io.File
import java.io.FileNotFoundException
import java.util.ResourceBundle
import kotlin.collections.HashSet

class ExecuteScript : Command(
    "execute_script",
    ResourceBundle.getBundle("message/info").getString("executeScript_description")
) {

    companion object ExecuteHistory {
        private val scriptHistory: HashSet<String> = HashSet()
        fun addScript(scriptName: String) {
            if (scriptHistory.contains(scriptName)) {
                throw RecursionScriptException()
            } else {
                scriptHistory.add(scriptName)
            }
        }

        fun removeScript(scriptName: String) {
            scriptHistory.remove(scriptName)
        }
    }

    /**
     * executes script
     * @param argument  filePath
     */
    override fun execute(argument: String?): Response {
        val file: File = argument?.let { File(argument).absoluteFile } ?: run {
            return ErrorResponse("Не указано имя скрипта для исполнения", isFatal = false)
        }
        try {
            addScript(file.toString())
        } catch (e: RecursionScriptException) {
            return ErrorResponse("Обнаружена рекурсия!!", isFatal = true)
            //console.print(Response("Обнаружена рекурсия!!"))
            //console.getScanner().closeWithCleanup(1)
        }
        val lines: List<String>
        try {
            lines = file.readLines()
        } catch (e: FileNotFoundException) {
            return ErrorResponse("Файл не найден", isFatal = false)
        }
        for (line in lines) {
            if (line.isBlank()) {
                continue
            }
            val commandToProcess: Command = CommandParser.parseCommand(line.split(" "))
                ?: run {
                    return ErrorResponse("В скрипте указана неизвестная команда", isFatal = true)
                    //console.print(Response("В скрипте указана неизвестная команда"))
                    //console.getScanner().closeWithCleanup(1)
                }
            var commandArgument = line.split(" ").getOrNull(1)
            if (commandToProcess is Autogeneratable) {
                commandArgument = commandArgument?.let { "$it --auto" } ?: "--auto"
            }
            return commandToProcess.execute(commandArgument) // todo: точно ли return?
        }
        removeScript(file.toString())
        return Response("Скрипт $file завершен")
    }
}