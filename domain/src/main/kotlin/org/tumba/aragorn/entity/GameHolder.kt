package org.tumba.aragorn.entity

internal class GameHolder private constructor(){

    var game: Game? = null

    companion object {

        private val instance: GameHolder by lazy { GameHolder() }

        fun get(): GameHolder = instance
    }
}