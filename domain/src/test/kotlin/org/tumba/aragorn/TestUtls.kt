package org.tumba.aragorn

import org.tumba.aragorn.core.City
import org.tumba.aragorn.entity.IntermediateGameState
import org.tumba.aragorn.entity.IntermediateGameState.*
import org.tumba.aragorn.core.Player

infix fun <T> Iterable<T>.`should contains same`(iterable: Iterable<T>) {
    if (!iterable.all { this.contains(it) }) fail("Difference lists: ${this.joinToString()} and ${iterable.joinToString()}")
    if (!this.all { iterable.contains(it) }) fail("Difference lists: ${this.joinToString()} and ${iterable.joinToString()}")
}

inline fun <reified T : Throwable> assertThrown(block: () -> Unit) {
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

internal fun anyCity() = City(0, "")

internal fun IntermediateGameState.Companion.values(): List<IntermediateGameState> {
    return listOf(
        Starting,
        InitialPickingDestinationTicketCard,
        ChoosingTurnType(createPlayer()),
        PickingTrainCarCard(createPlayer(), 0),
        PickingDestinationTicketCard(createPlayer(), listOf()),
        PlacingTrainCars(createPlayer())
    )
}

internal inline fun <reified T : IntermediateGameState> IntermediateGameState.Companion.valuesExclude()
    : List<IntermediateGameState> {
    return values().filter { it::class == T::class }
}

private fun createPlayer(): Player {
    return Player(0, "", Player.Color.RED)
}
