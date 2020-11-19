package hr.onit.agency.logging

object LoggingWrapper {
    enum class LogLevel { TRACE, DEBUG, INFO, WARN, ERROR }

    val indentation = 40

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
        log("info ", title, message)
    }

    fun warn(title: String, message: String) {
        if (LogLevel.WARN >= getLogLevel())
        log("warn ", title, message)
    }

    fun error(title: String, message: String) {
        if (LogLevel.ERROR >= getLogLevel())
        log("error", title, message)
    }

    private fun log(prefix: String, title: String, message: String) {
        println(prefix + " " + title + indent(indentation - title.length) + message)
    }

    fun getLogLevel() = LogLevel.TRACE

    fun indent(width: Int): String {
        val builder = StringBuilder()
        for (i in width downTo 0) builder.append(' ')
        return builder.toString()
    }
}