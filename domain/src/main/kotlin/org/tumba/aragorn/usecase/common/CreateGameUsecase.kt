package org.tumba.aragorn.usecase.common

import org.tumba.aragorn.core.Player
import org.tumba.aragorn.entity.GameHolder
import org.tumba.aragorn.entity.NewGameFactory
import org.tumba.aragorn.usecase.IUsecase
import java.util.*

interface ICreateGameUsecase : IUsecase<ICreateGameUsecase.Param, Unit> {

    data class Param(
        val players: List<Player>
    )
}

internal class CreateGameUsecase(
    private val gameHolder: GameHolder
) : ICreateGameUsecase {

    override fun execute(param: ICreateGameUsecase.Param) {
        if (gameHolder.game != null) {
            throw IllegalStateException("Game is already exist. Stop game before start new game.")
        }
        val factory = NewGameFactory.create(
            players = param.players,
            random = Random()
        )
        val game = factory.create()
        gameHolder.game = game
    }
}