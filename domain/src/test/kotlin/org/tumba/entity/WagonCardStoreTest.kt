package org.tumba.entity

import org.amshove.kluent.`should equal`
import org.junit.Test

import org.tumba.`should contains same`

class WagonCardStoreTest {

    @Suppress("UNUSED_CHANGED_VALUE")
    @Test
    fun testInitWithEmptyCard() {
        var id = 0
        val stackCards = listOf(
            WagonCard(id++, WagonCard.Kind.BLACK),
            WagonCard(id++, WagonCard.Kind.BLUE),
            WagonCard(id++, WagonCard.Kind.RED),
            WagonCard(id++, WagonCard.Kind.GREEN),
            WagonCard(id++, WagonCard.Kind.MULTICOLOR)
        )
        val stack = WagonCardStack(stackCards)
        val wagonCardStore = WagonCardStore(emptyList(), STORE_SIZE, stack)

        wagonCardStore.cards `should contains same` stackCards
    }

    @Suppress("UNUSED_CHANGED_VALUE")
    @Test
    fun testInitWithFullCard() {
        var id = 0
        val stackCards = listOf(
            WagonCard(id++, WagonCard.Kind.BLACK),
            WagonCard(id++, WagonCard.Kind.BLACK),
            WagonCard(id++, WagonCard.Kind.BLACK),
            WagonCard(id++, WagonCard.Kind.BLACK)
        )
        val storeCards = listOf(
            WagonCard(id++, WagonCard.Kind.RED),
            WagonCard(id++, WagonCard.Kind.BLACK),
            WagonCard(id++, WagonCard.Kind.YELLOW),
            WagonCard(id++, WagonCard.Kind.GREEN),
            WagonCard(id++, WagonCard.Kind.GREEN)
        )
        val stack = WagonCardStack(stackCards)
        val wagonCardStore = WagonCardStore(storeCards, STORE_SIZE, stack)

        wagonCardStore.cards `should contains same` storeCards
    }

    @Suppress("UNUSED_CHANGED_VALUE")
    @Test
    fun testGetCardFromStore() {
        var id = 0
        val stackCards = listOf(
            WagonCard(id++, WagonCard.Kind.BLACK),
            WagonCard(id++, WagonCard.Kind.BLUE),
            WagonCard(id++, WagonCard.Kind.RED),
            WagonCard(id++, WagonCard.Kind.GREEN),
            WagonCard(id++, WagonCard.Kind.MULTICOLOR),
            WagonCard(id++, WagonCard.Kind.RED),
            WagonCard(id++, WagonCard.Kind.BLACK)
        )
        val stack = WagonCardStack(stackCards)
        val wagonCardStore = WagonCardStore(emptyList(), STORE_SIZE, stack)

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
            WagonCard(id++, WagonCard.Kind.BLACK),
            WagonCard(id++, WagonCard.Kind.BLUE),
            WagonCard(id++, WagonCard.Kind.RED),
            WagonCard(id++, WagonCard.Kind.GREEN),
            WagonCard(id++, WagonCard.Kind.MULTICOLOR),
            WagonCard(id++, WagonCard.Kind.RED),
            WagonCard(id++, WagonCard.Kind.BLACK),
            WagonCard(id++, WagonCard.Kind.GREEN),
            WagonCard(id++, WagonCard.Kind.YELLOW)
        )
        val stack = WagonCardStack(stackCards)
        val wagonCardStore = WagonCardStore(emptyList(), STORE_SIZE, stack)

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