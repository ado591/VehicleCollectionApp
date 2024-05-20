package model.response

import model.User
import java.io.Serializable


data class Response(
    var message: String,
    var responseType: ResponseType = ResponseType.BASIC,
    var statusCode: Int = 0,
    var index: Int = 0,
    var responseUser: User? = null
) : Serializable {
    constructor() :
            this(message = "")
}
