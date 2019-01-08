package org.tumba.entity

import java.util.*

class CardStack<T : ICard>(
    cards: List<T>,
    droppedCards: List<T>,
    private val random: Random = Random()
) {

    private val stack: Deque<T> = ArrayDeque<T>().apply { addAll(cards) }
    private val droppedCards: MutableList<T> = mutableListOf<T>().apply { addAll(droppedCards) }

    val size: Int
        get() = stack.size

    val isEmpty: Boolean
        get() = stack.isEmpty()

    fun pop(): T? {
        return if (stack.isNotEmpty()) stack.pop() else throw IllegalStateException("Stack is empty")
    }

    fun dropCard(card: T) {
        droppedCards.add(card)
    }

    fun reset() {
        if (isEmpty) {
            stack.addAll(droppedCards.shuffled(random))
        } else {
            throw IllegalStateException("Stack card is not empty")
        }
    }
}

class TrainCarCardStore(
    cards: List<TrainCarCard>,
    val maxStoreSize: Int,
    private val stack: CardStack<TrainCarCard>
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

    fun dropCard(card: TrainCarCard) {
        stack.dropCard(card)
    }
}