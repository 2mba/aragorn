package org.tumba.entity

import java.util.*

class WagonCardStack(
    cards: List<TrainCarCard>
) {

    private val stack: Deque<TrainCarCard> = ArrayDeque<TrainCarCard>().apply { addAll(cards) }

    val size: Int
        get() = stack.size

    val isEmpty: Boolean
        get() = size == 0

    fun pop(): TrainCarCard? {
        return if (stack.isNotEmpty()) stack.pop() else null
    }
}

class WagonCardStore(
    cards: List<TrainCarCard>,
    val maxStoreSize: Int,
    private val stack: WagonCardStack
) {

    val stackSize: Int
        get() = stack.size
    val cards: List<TrainCarCard>
        get() = cardsInternal
    private val cardsInternal: LinkedList<TrainCarCard> = LinkedList()

    init {
        when {
            cards.isEmpty() -> {
                for (i in 0 until maxStoreSize) {
                    val card = stack.pop() ?: break
                    cardsInternal.add(card)
                }
            }
            cards.size in 1..maxStoreSize -> {
                if (stack.isEmpty || cards.size == maxStoreSize) {
                    cardsInternal.addAll(cards)
                } else {
                    throw IllegalArgumentException("Store size (cards) should be maxStoreSize=$maxStoreSize if stack is empty")
                }
            }
            else -> throw IllegalArgumentException("Size cards should be from 0 to maxStoreSize=$maxStoreSize")
        }
    }

    fun getCardFromStore(cardId: Int): TrainCarCard {
        val cardIdx = cardsInternal.indexOfFirst { cardId == it.id }
        if (cardIdx < 0) throw IllegalArgumentException("No card in store with id $cardId")
        return cardsInternal[cardIdx].also {
            val poppedCard = stack.pop()
            if (poppedCard != null) {
                cardsInternal[cardIdx] = poppedCard
            } else {
                cardsInternal.removeAt(cardIdx)
            }
        }
    }

    fun getCardFromStack(): TrainCarCard? = stack.pop()
}