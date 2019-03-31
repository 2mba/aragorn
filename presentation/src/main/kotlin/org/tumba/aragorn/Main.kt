package org.tumba.aragorn

import org.koin.core.KoinComponent
import org.koin.core.inject
import org.tumba.aragorn.di.initKoin
import org.tumba.aragorn.usecase.common.ICreateGameUsecase

fun main(args: Array<String>) {
    initKoin()

    val createGameUsecase = object : KoinComponent {
        val usecase by inject<ICreateGameUsecase>()
    }.usecase

    createGameUsecase.execute(
        param = ICreateGameUsecase.Param(listOf())
    )
}