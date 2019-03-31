package org.tumba.aragorn

import org.koin.core.KoinComponent
import org.koin.core.inject
import org.tumba.aragorn.core.Player
import org.tumba.aragorn.di.initKoin
import org.tumba.aragorn.usecase.common.ICreateGameUsecase
import org.tumba.aragorn.usecase.common.IPlaceTrainCarsUsecase

fun main(args: Array<String>) {
    initKoin()

    val createGameUsecase = object : KoinComponent {
        val usecase by inject<ICreateGameUsecase>()
    }.usecase
    val placeTrainCarUsecase = object : KoinComponent {
        val usecase by inject<IPlaceTrainCarsUsecase>()
    }.usecase

    createGameUsecase.execute(
        param = ICreateGameUsecase.Param(
            listOf(
                Player(0, "Player 1", Player.Color.BLACK)
            )
        )
    )

    placeTrainCarUsecase.execute(
        param = IPlaceTrainCarsUsecase.Param(
            playerId = 0,
            roadId = 0,
            wagonCardIds = listOf()
        )
    )
}