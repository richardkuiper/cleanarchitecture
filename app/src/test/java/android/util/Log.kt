package android.util

/**
 * Dummy implementation of Android log API, only used for JVM testing
 */
object Log {

    @JvmStatic
    fun e(tag: String, msg: String): Int {
        println("ERROR($tag) : $msg")
        return 0
    }

    @JvmStatic
    fun w(tag: String, msg: String): Int {
        println("WARNING($tag) : $msg")
        return 0
    }

    @JvmStatic
    fun d(tag: String, msg: String): Int {
        println("DEBUG($tag) : $msg")
        return 0
    }

    @JvmStatic
    fun v(tag: String, msg: String): Int {
        println("VERBOSE($tag) : $msg")
        return 0
    }

    @JvmStatic
    fun i(tag: String, msg: String): Int {
        println("INFO($tag) : $msg")
        return 0
    }

    @JvmStatic
    fun e(tag: String, msg: String, th: Throwable?): Int {
        println("ERROR($tag) : $msg")
        return 0
    }

    @JvmStatic
    fun w(tag: String, msg: String, th: Throwable): Int {
        println("WARNING($tag) : $msg")
        th.printStackTrace()
        return 0
    }

    @JvmStatic
    fun d(tag: String, msg: String, th: Throwable): Int {
        println("DEBUG($tag) : $msg")
        th.printStackTrace()
        return 0
    }

    @JvmStatic
    fun v(tag: String, msg: String, th: Throwable): Int {
        println("VERBOSE($tag) : $msg")
        th.printStackTrace()
        return 0
    }

    @JvmStatic
    fun i(tag: String, msg: String, th: Throwable): Int {
        println("INFO($tag) : $msg")
        th.printStackTrace()
        return 0
    }

    @JvmStatic
    fun isLoggable(string: String?, i: Int): Boolean {
        return true
    }
}