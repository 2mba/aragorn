package org.tumba.aragorn.usecase.common

import org.tumba.aragorn.entity.GameHolder
import org.tumba.aragorn.entity.command.PlaceTrainCarsCommand
import org.tumba.aragorn.usecase.IUsecase

interface IPlaceTrainCarsUsecase : IUsecase<IPlaceTrainCarsUsecase.Param, Unit> {

    data class Param(
        val playerId: Int,
        val roadId: Int,
        val wagonCardIds: List<Int>
    )
}

internal class PlaceTrainCarsUsecase(
    private val gameHolder: GameHolder
) : IPlaceTrainCarsUsecase {

    override fun execute(param: IPlaceTrainCarsUsecase.Param) {
        val game = gameHolder.game
            ?: throw IllegalStateException("No existing game")
        game.execute(
            command = PlaceTrainCarsCommand(
                playerId = param.playerId,
                roadId = param.roadId,
                wagonCardIds = param.wagonCardIds
            )
        )
    }
}