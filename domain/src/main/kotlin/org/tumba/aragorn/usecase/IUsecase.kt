package org.tumba.aragorn.usecase

interface IUsecase<Param, R> {

    fun execute(param: Param): R

    object NoParam
}

