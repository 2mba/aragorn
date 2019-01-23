package org.tumba.entity.command

import org.tumba.entity.*


interface ICommandProcessor {

    fun process(gameData: GameData, command: ICommand)

    data class GameData(
         val gameState: GameState,
         val gameHelper: GameHelper
    )
}

class TypedBatchCommandProcessor(
    private val processors: List<TypedCommandProcessor<*>>
) : ICommandProcessor {

    override fun process(gameData: ICommandProcessor.GameData, command: ICommand) {
        processors
            .asSequence()
            .filter { it.clazz == command }
            .forEach { it.process(gameData, command) }
    }
}

abstract class TypedCommandProcessor<T>(val clazz: Class<T>) : ICommandProcessor {

    @Suppress("UNCHECKED_CAST")
    override fun process(gameData: ICommandProcessor.GameData, command: ICommand) {
        if (command::class.java == clazz) {
            process(gameData, command as T)
        }
    }

    abstract fun process(gameData: ICommandProcessor.GameData, command: T)
}

class BatchCommandProcessor(
    private val processors: List<ICommandProcessor>
) : ICommandProcessor {

    override fun process(gameData: ICommandProcessor.GameData, command: ICommand) {
        processors.forEach {
            try {
                it.process(gameData, command)
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

class PlaceTrainCarsCommandProcessor(
    private val trainCarPlacementValidator: ITrainCarPlacementValidator
): TypedCommandProcessor<PlaceTrainCarsCommand>(PlaceTrainCarsCommand::class.java) {

    override fun process(gameData: ICommandProcessor.GameData, command: PlaceTrainCarsCommand) {
        val player = gameData.gameHelper.getPlayerById(command.playerId)
        val road = gameData.gameState.map.cityGraph.roads.firstOrNull { it.id == command.roadId }
            ?: throw IllegalArgumentException("Unknown road, id = ${command.roadId}")

        gameData.gameHelper.ensureState(gameData.gameHelper.equalTo(IntermediateGameState.PlacingTrainCars(player)))
        gameData.gameHelper.checkEnoughTrainCarsFor(player, road)
        val wagonCards = gameData.gameHelper.getTrainCarCards(player, command.wagonCardIds)

        if (trainCarPlacementValidator.canRoadBePlacedByTrainCarCards(road, wagonCards)) {
            placeTrainCarSafely(gameData.gameState, player, road, wagonCards)
        } else {
            throw IllegalTrainCarTypeException()
        }
    }

    private fun placeTrainCarSafely(state: GameState, player: Player, road: Road, trainCarCards: List<TrainCarCard>) {
        val playerState = state.playerStates.getStateOf(player)
        playerState.trainCarCards.removeAll(trainCarCards)
        playerState.numberOfTrainCars -= road.length
        state.trainCarPlacements.add(TrainCarPlacement(road, TrainCar(player.id)))
    }
}

