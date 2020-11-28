package hr.onit.agency.logging

object LoggingWrapper {
    enum class LogLevel { TRACE, DEBUG, INFO, WARN, ERROR }

    val titleIndentation = 30
    val levelIndentation = 10

    fun trace(title: String, message: String) {
        if (LogLevel.TRACE >= getLogLevel())
            log("trace", title, message)
    }

    fun debug(title: String, message: String) {
        if (LogLevel.DEBUG >= getLogLevel())
        log("debug", title, message)
    }

    fun info(title: String, message: String) {
        if (LogLevel.INFO >= getLogLevel())
        log("info", title, message)
    }

    fun warn(title: String, message: String) {
        if (LogLevel.WARN >= getLogLevel())
        log("warn", title, message)
    }

    fun error(title: String, message: String) {
        if (LogLevel.ERROR >= getLogLevel())
        log("error", title, message)
    }

    private fun log(prefix: String, title: String, message: String) {
        println(prefix + indent(levelIndentation-prefix.length) + title + indent(titleIndentation - title.length) + message)
    }

    fun getLogLevel() = LogLevel.INFO

    fun indent(width: Int): String {
        val builder = StringBuilder()
        for (i in width downTo 0) builder.append(' ')
        return builder.toString()
    }
}