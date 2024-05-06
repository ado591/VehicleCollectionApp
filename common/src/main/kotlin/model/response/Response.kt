package model.response

import java.io.Serializable


data class Response(
    var message: String,
    var responseType: ResponseType = ResponseType.BASIC,
    var statusCode: Int = 0,
    var index: Int = 0
) : Serializable {
    constructor() :
            this(message = "")
}
