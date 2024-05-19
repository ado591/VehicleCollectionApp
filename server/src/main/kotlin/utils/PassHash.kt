package utils

import org.apache.logging.log4j.LogManager
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


const val encryptAlgorithm: String = "MD5"
class PassHash {
    companion object {
        val logger = LogManager.getLogger("logger")
        fun encryptString(password: String): String {
            return try {
                val md: MessageDigest = MessageDigest.getInstance(encryptAlgorithm)
                val digest: ByteArray = md.digest(password.toByteArray(StandardCharsets.UTF_8))
                val numRepresentation = BigInteger(1, digest)
                var hashedString = numRepresentation.toString(16)
                while (hashedString.length < 32) { //todo: подумать над хэшированием
                    hashedString = "0$hashedString"
                }
                hashedString
            } catch (e: NoSuchAlgorithmException) {
                logger.error("Could not find algorithm")
                "" //todo: эээээ
            }
        }
    }
}