package org.tumba.entity

class TravelCard(
    val id: Int,
    val from: City,
    val to: City
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TravelCard

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}

class WagonCard(
    val id: Int,
    val kind: Kind
) {
    enum class Kind {
        RED,
        YELLOW,
        BLUE,
        BLACK,
        PINK,
        WHITE,
        GREEN,
        MULTICOLOR
    }
}

