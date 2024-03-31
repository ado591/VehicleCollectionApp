package exceptions

import java.util.ResourceBundle

class RecursionScriptException :
    Exception(ResourceBundle.getBundle("message/error").getString("recursive_script")) {
}