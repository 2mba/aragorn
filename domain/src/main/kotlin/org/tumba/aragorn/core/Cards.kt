package org.tumba.aragorn.core

class DestinationTickerCard(
    val id: Int,
    val from: City,
    val to: City
) : ICard {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DestinationTickerCard

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}

class TrainCarCard(
    val id: Int,
    val kind: Kind
) : ICard {
    enum class Kind {
        RED,
        YELLOW,
        BLUE,
        BLACK,
        PINK,
        WHITE,
        GREEN,
        ORANGE,
        LOCOMOTIVE
    }
}

interface ICard