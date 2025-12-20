private const val ANSI_RESET = "\u001B[0m";

fun red(text: String) = "\u001B[31m$text$ANSI_RESET"
fun green(text: String) = "\u001B[32m$text$ANSI_RESET"
fun yellow(text: String) = "\u001B[33m$text$ANSI_RESET"
fun blue(text: String) = "\u001B[34m$text$ANSI_RESET"
fun purple(text: String) = "\u001B[35m$text$ANSI_RESET"
fun cyan(text: String) = "\u001B[36m$text$ANSI_RESET"
fun white(text: String) = "\u001B[37m$text$ANSI_RESET"
fun grayBG(text: String) = "\u001B[100m$text$ANSI_RESET"
