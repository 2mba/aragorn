package org.tumba.aragorn.entity.command

import org.tumba.aragorn.entity.IGameManager


internal interface ICommandProcessor {

    fun process(gameManager: IGameManager, command: ICommand)
}

internal class TypedBatchCommandProcessor(
    private val processors: List<TypedCommandProcessor<*>>
) : ICommandProcessor {

    override fun process(gameManager: IGameManager, command: ICommand) {
        processors
            .asSequence()
            .filter { it.clazz == command::class.java }
            .forEach { it.process(gameManager, command) }
    }
}

internal abstract class TypedCommandProcessor<T>(val clazz: Class<T>) : ICommandProcessor {

    @Suppress("UNCHECKED_CAST")
    override fun process(gameManager: IGameManager, command: ICommand) {
        if (command::class.java == clazz) {
            process(gameManager, command as T)
        }
    }

    abstract fun process(gameManager: IGameManager, command: T)
}

internal class BatchCommandProcessor(
    private val processors: List<ICommandProcessor>
) : ICommandProcessor {

    override fun process(gameManager: IGameManager, command: ICommand) {
        processors.forEach {
            try {
                it.process(gameManager, command)
            } catch (err: Throwable) {
                err.printStackTrace()
            }
        }
    }
}
