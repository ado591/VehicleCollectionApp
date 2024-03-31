package exceptions

import java.util.ResourceBundle

class LoaderException :
    Exception(ResourceBundle.getBundle("message/error").getString("loader_exception")){
}