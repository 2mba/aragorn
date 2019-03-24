package org.tumba.aragorn.usecase.common

import org.tumba.aragorn.core.Player
import org.tumba.aragorn.entity.GameHolder
import org.tumba.aragorn.entity.NewGameFactory
import org.tumba.aragorn.usecase.IUsecase

interface ICreateGameUsecase: IUsecase<ICreateGameUsecase.Param, Unit>{

    override fun execute(param: Param) {
        val gameHolder = GameHolder.get()
        if (gameHolder.game != null) {
            throw IllegalStateException("Game is already exist. Stop game before start new game.")
        }
        val factory = NewGameFactory.create(
            players = param.players
        )
        val game = factory.create()
        gameHolder.game = game
    }


    data class Param(
        val players: List<Player>
    )
}