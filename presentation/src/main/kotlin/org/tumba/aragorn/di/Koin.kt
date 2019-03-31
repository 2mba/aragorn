package org.tumba.aragorn.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun initKoin() {
    startKoin {
        modules(createModules())
    }
}

private fun createModules(): List<Module> {
    return listOf(
        DomainModulesProvider().provideModules(),
        PresentationModulesProvider().provideModules()
    ).flatten()
}