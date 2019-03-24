package org.tumba.aragorn.entity.processor

import org.tumba.aragorn.entity.IGameManager
import org.tumba.aragorn.entity.IntermediateGameState.*
import org.tumba.aragorn.entity.command.ChooseTurnTypeCommand
import org.tumba.aragorn.entity.command.ChooseTurnTypeCommand.TurnType.*
import org.tumba.aragorn.entity.command.TypedCommandProcessor

internal class ChooseTurnTypeCommandProcessor :
    TypedCommandProcessor<ChooseTurnTypeCommand>(ChooseTurnTypeCommand::class.java) {

    override fun process(gameManager: IGameManager, command: ChooseTurnTypeCommand) {
        val player = gameManager.getPlayerById(command.playerId)
        gameManager.ensureState(gameManager.equalTo(ChoosingTurnType(player)))

        when (command.turnType) {
            PLACE_TRAIN_CARS -> {
                gameManager.state.intermediateGameState = PlacingTrainCars(player)
            }
            GET_DESTINATION_TICKETS -> {
                gameManager.state.intermediateGameState = PickingDestinationTicketCard(
                    player = player,
                    cards = listOf() // TODO get cards from stack
                )
            }
            GET_TRAIN_CAR_CARDS -> {
                gameManager.state.intermediateGameState = PickingTrainCarCard(
                    player = player,
                    numberOfPickedCards = 0
                )
            }
        }
    }

}