package org.tumba.aragorn.entity.command

import org.tumba.aragorn.entity.IGameManager


interface ICommandProcessor {

    fun process(gameManager: IGameManager, command: ICommand)
}

class TypedBatchCommandProcessor(
    private val processors: List<TypedCommandProcessor<*>>
) : ICommandProcessor {

    override fun process(gameManager: IGameManager, command: ICommand) {
        processors
            .asSequence()
            .filter { it.clazz == command }
            .forEach { it.process(gameManager, command) }
    }
}

abstract class TypedCommandProcessor<T>(val clazz: Class<T>) : ICommandProcessor {

    @Suppress("UNCHECKED_CAST")
    override fun process(gameManager: IGameManager, command: ICommand) {
        if (command::class.java == clazz) {
            process(gameManager, command as T)
        }
    }

    abstract fun process(gameManager: IGameManager, command: T)
}

class BatchCommandProcessor(
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

class PlaceTrainCarsCommand(
    val playerId: Int,
    val roadId: Int,
    val wagonCardIds: List<Int>
): ICommand

