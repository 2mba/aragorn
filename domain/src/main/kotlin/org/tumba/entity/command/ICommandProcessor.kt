package org.tumba.entity.command

import org.tumba.entity.*


interface ICommandProcessor {

    fun process(game: Game, command: ICommand)
}

class TypedBatchCommandProcessor(
    private val processors: List<TypedCommandProcessor<*>>
) : ICommandProcessor {

    override fun process(game: Game, command: ICommand) {
        processors
            .asSequence()
            .filter { it.clazz == command }
            .forEach { it.process(game, command) }
    }
}

abstract class TypedCommandProcessor<T>(val clazz: Class<T>) : ICommandProcessor {

    @Suppress("UNCHECKED_CAST")
    override fun process(game: Game, command: ICommand) {
        if (command::class.java == clazz) {
            process(game, command as T)
        }
    }

    abstract fun process(game: Game, command: T)
}

class BatchCommandProcessor(
    private val processors: List<ICommandProcessor>
) : ICommandProcessor {

    override fun process(game: Game, command: ICommand) {
        processors.forEach {
            try {
                it.process(game, command)
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
)

class PlaceTrainCarsCommandProcessor: TypedCommandProcessor<PlaceTrainCarsCommand>(PlaceTrainCarsCommand::class.java) {

    override fun process(game: Game, command: PlaceTrainCarsCommand) {
        val player = game.getPlayerById(command.playerId)
        val road = game.state.map.cityGraph.roads.firstOrNull { it.id == command.roadId }
            ?: throw IllegalArgumentException("Unknown road, id = ${command.roadId}")

        game.ensureState(game.equalTo(IntermediateGameState.PlacingTrainCars(player)))
        game.checkEnoughTrainCarsFor(player, road)
        val wagonCards = game.getTrainCarCards(player, command.wagonCardIds)

        if (game.trainCarPlacementValidator.canRoadBePlacedByTrainCarCards(road, wagonCards)) {
            placeTrainCarSafely(game, player, road, wagonCards)
        } else {
            throw IllegalTrainCarTypeException()
        }
    }

    private fun placeTrainCarSafely(game: Game, player: Player, road: Road, trainCarCards: List<TrainCarCard>) {
        val playerState = game.state.playerStates.getStateOf(player)
        playerState.trainCarCards.removeAll(trainCarCards)
        playerState.numberOfTrainCards -= road.length
        game.state.wagonPlacements.add(TrainCarPlacement(road, TrainCar(player.id)))
    }
}

