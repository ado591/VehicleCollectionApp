package model

import java.io.Serializable

data class User(
    val username: String? = null, //todo: Null оставить или убрать??
    val password: String? = null
): Serializable

