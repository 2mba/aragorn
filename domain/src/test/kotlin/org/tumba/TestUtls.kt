package org.tumba

import org.tumba.entity.City

infix fun <T> Iterable<T>.`should contains same`(iterable: Iterable<T>) {
    if (!iterable.all { this.contains(it) }) fail("Difference lists: ${this.joinToString()} and ${iterable.joinToString()}")
    if (!this.all { iterable.contains(it) }) fail("Difference lists: ${this.joinToString()} and ${iterable.joinToString()}")
}

inline fun <reified T: Throwable> assertThrown(block: () -> Unit) {
    var exceptionHadNotBeenThrown = false
    try {
        block.invoke()
        exceptionHadNotBeenThrown = true
    } catch (e: Throwable) {
        if (e.javaClass != T::class.java) {
            fail("Expected ${T::class.java}, actual: ${e.javaClass}")
        }
    }
    if (exceptionHadNotBeenThrown) {
        fail("No exception had been thrown")
    }
}

fun fail(message: String): Nothing {
    throw AssertionError(message)
}

fun anyCity() = City(0, "")