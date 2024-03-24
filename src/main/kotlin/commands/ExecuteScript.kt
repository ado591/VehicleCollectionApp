package commands

import commands.extra.Autogeneratable
import exceptions.RecursionScriptException
import response.Response
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import kotlin.collections.HashSet
import kotlin.system.exitProcess

class ExecuteScript: Command("execute_script",
    ResourceBundle.getBundle("message/info").getString("executeScript_description")) {

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
    override fun execute(argument: String?): Response {
        val file: File
        try {
            file = File(argument!!) //todo: проверить пути
        } catch (e: NullPointerException) {
            return Response("Не указано имя скрипта для исполнения")
        } catch (e: FileNotFoundException) {
            return Response("Файл не найден")
        }
        try {
            addScript(file.toString())
        } catch (e: RecursionScriptException) {
            console.print(Response("Обнаружена рекурсия!!"))
            exitProcess(1)
        }
        val lines = file.readLines()
        for (line in lines) {
            if (line.isBlank()) {
                continue
            }
            val commandToProcess: Command
            try {
                commandToProcess = console.parseCommand(line.split(" "))!!
            } catch (e: NullPointerException) {
                console.print(Response("В скрипте указана неизвестная команда"))
                exitProcess(1)
            }
            var commandArgument = line.split(" ").getOrNull(1)
            if (commandToProcess is Autogeneratable) {
                commandArgument = "--auto"
            }
            console.print(commandToProcess.execute(commandArgument))
        }
        removeScript(file.toString())
        return Response("Скрипт $file завершен")
    }
}