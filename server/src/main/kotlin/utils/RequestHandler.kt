package utils

import commands.Command
import commands.extra.Autogeneratable
import data.Vehicle
import exceptions.users.UserNotFoundException
import model.User
import model.request.Request
import model.request.RequestType
import model.response.Response
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RequestHandler : KoinComponent {
    private val commandParser: CommandParser by inject()
    private val logger: Logger = LogManager.getLogger("logger")
    fun handle(request: Request): Pair<Command, Response> {
        val inputLine = request.message
        val commandToProcess = commandParser.parseCommand(inputLine.split(" ", limit = 2))
        val commandArguments = commandParser.parseArguments(inputLine)
        logger.info("command to process: $commandToProcess")
        val user: User? = request.user
        return if (request.requestType == RequestType.SCRIPT && commandToProcess is Autogeneratable) {
            Pair(commandToProcess, commandToProcess.execute((commandArguments?.let { "$it --auto" } ?: "--auto"), user))
        } else {
            Pair(commandToProcess, commandToProcess.execute(commandArguments, user))
        }
    }

    fun handleWithObject(element: Vehicle, commandToProcess: Autogeneratable, index: Int, user: User?): Response {
        return commandToProcess.executeWithObject(element, index, user ?: throw UserNotFoundException())
    }


}