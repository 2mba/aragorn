package org.tumba.aragorn.entity.processor

import org.tumba.aragorn.entity.*
import org.tumba.aragorn.entity.command.PlaceTrainCarsCommand
import org.tumba.aragorn.entity.command.TypedCommandProcessor

class PlaceTrainCarsCommandProcessor(
    private val trainCarPlacementValidator: ITrainCarPlacementValidator
): TypedCommandProcessor<PlaceTrainCarsCommand>(PlaceTrainCarsCommand::class.java) {

    override fun process(gameManager: IGameManager, command: PlaceTrainCarsCommand) {
        val player = gameManager.getPlayerById(command.playerId)
        val road = gameManager.state.map.cityGraph.roads.firstOrNull { it.id == command.roadId }
            ?: throw IllegalArgumentException("Unknown road, id = ${command.roadId}")

        gameManager.ensureState(gameManager.equalTo(IntermediateGameState.PlacingTrainCars(player)))
        gameManager.checkEnoughTrainCarsFor(player, road)
        val wagonCards = gameManager.getTrainCarCards(player, command.wagonCardIds)

        if (trainCarPlacementValidator.canRoadBePlacedByTrainCarCards(road, wagonCards)) {
            placeTrainCarSafely(gameManager.state, player, road, wagonCards)
        } else {
            throw IllegalTrainCarTypeException()
        }
    }

    private fun placeTrainCarSafely(state: IGameState, player: Player, road: Road, trainCarCards: List<TrainCarCard>) {
        val playerState = state.playerStates.getStateOf(player)
        playerState.trainCarCards.removeAll(trainCarCards)
        playerState.numberOfTrainCars -= road.length
        state.trainCarPlacements.add(
            TrainCarPlacement(
                road,
                TrainCar(player.id)
            )
        )
    }
}