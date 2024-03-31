package exceptions

import java.util.*

class LoaderException :
    Exception(ResourceBundle.getBundle("message/error").getString("loader_exception")){
}