package org.tumba.aragorn.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.tumba.aragorn.entity.GameHolder
import org.tumba.aragorn.usecase.common.CreateGameUsecase
import org.tumba.aragorn.usecase.common.ICreateGameUsecase


class DomainModulesProvider {

    fun provideModules(): List<Module> {
        return listOf(
            createDomainModule(),
            createUsecaseModule()
        )
    }
}

private fun createDomainModule(): Module = module {

    single { GameHolder() }
}

private fun createUsecaseModule(): Module = module {

    factory<ICreateGameUsecase> { CreateGameUsecase(get()) }
}
