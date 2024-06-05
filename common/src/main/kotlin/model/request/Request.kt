package model.request

import data.Vehicle
import model.User
import java.io.Serializable


data class Request(
    var message: String,
    var requestType: RequestType = RequestType.REGULAR,
    var vehicle: Vehicle? = null,
    var user: User? = null
) : Serializable {
    constructor() : this(message = "")

}