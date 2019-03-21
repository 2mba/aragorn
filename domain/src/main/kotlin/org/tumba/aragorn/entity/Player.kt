package org.tumba.aragorn.entity

class Player(
    val id: Int,
    val name: String,
    val color: Color
) {
    enum class Color {
        RED,
        GREEN,
        BLUE,
        BLACK
    }
}