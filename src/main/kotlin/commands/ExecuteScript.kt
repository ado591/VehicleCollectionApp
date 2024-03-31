package commands

import commands.extra.Autogeneratable
import console.closeWithCleanup
import exceptions.RecursionScriptException
import response.Response
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
     * @param argument - filePath
     */
    override fun execute(argument: String?): Response {
        val file: File = argument?.let { File(argument).absoluteFile } ?: run {
            return Response("Не указано имя скрипта для исполнения")
        }
        try {
            addScript(file.toString())
        } catch (e: RecursionScriptException) {
            console.print(Response("Обнаружена рекурсия!!"))
            console.getScanner().closeWithCleanup(1)
        }
        val lines: List<String>
        try {
            lines = file.readLines()
        } catch (e: FileNotFoundException) {
            return Response("Файл не найден")
        }
        for (line in lines) {
            if (line.isBlank()) {
                continue
            }
            val commandToProcess: Command = console.parseCommand(line.split(" "))
                ?: run {
                    console.print(Response("В скрипте указана неизвестная команда"))
                    console.getScanner().closeWithCleanup(1)
                }
            var commandArgument = line.split(" ").getOrNull(1)
            if (commandToProcess is Autogeneratable) {
                commandArgument = commandArgument?.let { "$it --auto" } ?: "--auto"
            }
            console.print(commandToProcess.execute(commandArgument))
        }
        removeScript(file.toString())
        return Response("Скрипт $file завершен")
    }
}