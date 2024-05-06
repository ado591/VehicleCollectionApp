package model.request

import data.Vehicle
import java.io.Serializable


data class Request(
    var message: String,
    var requestType: RequestType = RequestType.REGULAR,
    var vehicle: Vehicle? = null
) : Serializable {
    constructor() : this(message = "")
}