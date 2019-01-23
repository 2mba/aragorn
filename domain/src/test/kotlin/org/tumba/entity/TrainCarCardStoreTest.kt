package org.tumba.entity

import org.amshove.kluent.`should equal`
import org.junit.Test

import org.tumba.`should contains same`

class TrainCarCardStoreTest {

    @Suppress("UNUSED_CHANGED_VALUE")
    @Test
    fun testInitWithEmptyCard() {
        var id = 0
        val stackCards = listOf(
            TrainCarCard(id++, TrainCarCard.Kind.BLACK),
            TrainCarCard(id++, TrainCarCard.Kind.BLUE),
            TrainCarCard(id++, TrainCarCard.Kind.RED),
            TrainCarCard(id++, TrainCarCard.Kind.GREEN),
            TrainCarCard(id++, TrainCarCard.Kind.LOCOMOTIVE)
        )
        val stack = CardStack(stackCards, listOf())
        val wagonCardStore = TrainCarCardStore(emptyList(), STORE_SIZE, stack)

        wagonCardStore.cards `should contains same` stackCards
    }

    @Suppress("UNUSED_CHANGED_VALUE")
    @Test
    fun testInitWithFullCard() {
        var id = 0
        val stackCards = listOf(
            TrainCarCard(id++, TrainCarCard.Kind.BLACK),
            TrainCarCard(id++, TrainCarCard.Kind.BLACK),
            TrainCarCard(id++, TrainCarCard.Kind.BLACK),
            TrainCarCard(id++, TrainCarCard.Kind.BLACK)
        )
        val storeCards = listOf(
            TrainCarCard(id++, TrainCarCard.Kind.RED),
            TrainCarCard(id++, TrainCarCard.Kind.BLACK),
            TrainCarCard(id++, TrainCarCard.Kind.YELLOW),
            TrainCarCard(id++, TrainCarCard.Kind.GREEN),
            TrainCarCard(id++, TrainCarCard.Kind.GREEN)
        )
        val stack = CardStack(stackCards, listOf())
        val wagonCardStore = TrainCarCardStore(storeCards, STORE_SIZE, stack)

        wagonCardStore.cards `should contains same` storeCards
    }

    @Suppress("UNUSED_CHANGED_VALUE")
    @Test
    fun testGetCardFromStore() {
        var id = 0
        val stackCards = listOf(
            TrainCarCard(id++, TrainCarCard.Kind.BLACK),
            TrainCarCard(id++, TrainCarCard.Kind.BLUE),
            TrainCarCard(id++, TrainCarCard.Kind.RED),
            TrainCarCard(id++, TrainCarCard.Kind.GREEN),
            TrainCarCard(id++, TrainCarCard.Kind.LOCOMOTIVE),
            TrainCarCard(id++, TrainCarCard.Kind.RED),
            TrainCarCard(id++, TrainCarCard.Kind.BLACK)
        )
        val stack = CardStack(stackCards, listOf())
        val wagonCardStore = TrainCarCardStore(emptyList(), STORE_SIZE, stack)

        wagonCardStore.cards `should contains same` stackCards.take(STORE_SIZE)

        wagonCardStore.getCardFromStore(0) `should equal` stackCards[0]
        wagonCardStore.cards[0] `should equal` stackCards[5]

        wagonCardStore.getCardFromStore(4) `should equal` stackCards[4]
        wagonCardStore.cards `should contains same` listOf(
            stackCards[5],
            stackCards[1],
            stackCards[2],
            stackCards[3],
            stackCards[6]
        )
    }

    @Suppress("UNUSED_CHANGED_VALUE")
    @Test
    fun testGetCardFromStoreAndStack() {
        var id = 0
        val stackCards = listOf(
            TrainCarCard(id++, TrainCarCard.Kind.BLACK),
            TrainCarCard(id++, TrainCarCard.Kind.BLUE),
            TrainCarCard(id++, TrainCarCard.Kind.RED),
            TrainCarCard(id++, TrainCarCard.Kind.GREEN),
            TrainCarCard(id++, TrainCarCard.Kind.LOCOMOTIVE),
            TrainCarCard(id++, TrainCarCard.Kind.RED),
            TrainCarCard(id++, TrainCarCard.Kind.BLACK),
            TrainCarCard(id++, TrainCarCard.Kind.GREEN),
            TrainCarCard(id++, TrainCarCard.Kind.YELLOW)
        )
        val stack = CardStack(stackCards, listOf())
        val wagonCardStore = TrainCarCardStore(emptyList(), STORE_SIZE, stack)

        wagonCardStore.cards `should contains same` stackCards.take(STORE_SIZE)

        wagonCardStore.getCardFromStack() `should equal` stackCards[5]
        wagonCardStore.getCardFromStack() `should equal` stackCards[6]

        wagonCardStore.getCardFromStore(0) `should equal` stackCards[0]
        wagonCardStore.cards `should contains same` listOf(
            stackCards[7],
            stackCards[1],
            stackCards[2],
            stackCards[3],
            stackCards[4]
        )
    }

    companion object {

        private const val STORE_SIZE = 5
    }
}