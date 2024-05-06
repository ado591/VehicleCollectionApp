package utils

import Client
import exceptions.RecursionScriptException
import model.request.Request
import model.request.RequestType
import network.UDPClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.exitProcess

class ExecuteScript(private val clientApp: Client) : KoinComponent {
    private val client: UDPClient by inject()

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

    fun execute(argument: String?) {
        val file: File = argument?.let { File(argument).absoluteFile } ?: run {
            println("Не указано имя скрипта для исполнения")
            return
        }
        try {
            addScript(file.toString())
        } catch (e: RecursionScriptException) {
            println("Обнаружена рекурсия!!")
            exitProcess(1)
        }
        println("Начинаем исполнять скрипт ${file.name}")
        val lines: List<String>
        try {
            lines = file.readLines().filter { it.isNotBlank() }
        } catch (e: FileNotFoundException) {
            println("Файл не найден")
            return
        }
        for (line in lines) {
            if (line.split("\\s".toRegex())[0] == "execute_script") {
                val scriptName: String? = line.split(" ", limit = 2)
                    .takeIf { it.size > 1 }?.drop(1)?.joinToString(" ").takeUnless { it.isNullOrBlank() }
                execute(scriptName)
            } else {
                val clientRequest = Request(line).apply { requestType = RequestType.SCRIPT }
                client.sendData(ObjectMapperWrapper.clientMapper.writeValueAsBytes(clientRequest))
                clientApp.handleResponse()
            }
        }
        println("Выполнение скрипта ${file.name} завершено")
        removeScript(file.toString())
    }

}