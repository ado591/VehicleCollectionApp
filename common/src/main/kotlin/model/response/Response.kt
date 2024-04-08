package model.response

import java.io.Serializable

open class Response(private val message: String) : Serializable { //todo: через enum или сделать наследников?
    fun message(): String = message
}