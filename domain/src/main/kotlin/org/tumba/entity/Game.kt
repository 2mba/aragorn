package org.tumba.entity

import org.tumba.entity.command.ICommand
import org.tumba.entity.command.ICommandProcessor


class Game(
    val trainCarPlacementValidator: ITrainCarPlacementValidator,
    val state: GameState,
    private val commandProcessor: ICommandProcessor
) {

    fun execute(command: ICommand) {
        commandProcessor.process(this, command)
    }

    fun chooseTurnType(playerId: Int, type: TurnType) {
        val player = getPlayerById(playerId)
        ensureState(equalTo(IntermediateGameState.ChoosingTurnType(player)))

        when (type) {
            TurnType.PLACE_TRAIN_CARS -> {
                state.intermediateGameState = IntermediateGameState.PlacingTrainCars(player)
            }
            TurnType.GET_DESTINATION_TICKETS -> {
                state.intermediateGameState = IntermediateGameState.PickingDestinationTicketCard(player, listOf())
            }
            TurnType.GET_TRAIN_CAR_CARDS -> {
                state.intermediateGameState = IntermediateGameState.PickingTrainCarCard(player, 0)
            }
        }
    }

    fun getPlayerById(id: Int): Player {
        return state.players.firstOrNull { it.id == id } ?: throw IllegalArgumentException("Unknown player id $id")
    }

    fun getTrainCarCards(player: Player, wagonCardIds: List<Int>): List<TrainCarCard> {
        val wagonCards = state.playerStates.getStateOf(player).trainCarCards
        return wagonCardIds
            .map { id -> wagonCards.firstOrNull { it.id == id }
                ?: throw TrainCarCardNotOwnedByUserException("Player $player not own cards $wagonCardIds") }
    }

    fun checkEnoughTrainCarsFor(player: Player, road: Road) {
        val numberOfPlayerWagons = state.playerStates.getStateOf(player).numberOfTrainCards
        if (road.length > numberOfPlayerWagons) {
            throw NotEnoughTrainCarsException()
        }
    }

    fun ensureState(predicate: (IntermediateGameState) -> Boolean) {
        if (!predicate.invoke(state.intermediateGameState)) throw OutOfTurnException()
    }

    fun equalTo(stateIntermediate: IntermediateGameState): (IntermediateGameState) -> Boolean {
        return { intermediateGameState: IntermediateGameState -> stateIntermediate == intermediateGameState }
    }
}

class GameState(
    val players: List<Player>,
    val map: Map,
    val wagonPlacements: MutableList<TrainCarPlacement>,
    val playerStates: PlayerStates = PlayerStates.createInitialStates(players.size),
    var intermediateGameState: IntermediateGameState,
    var cardsHolder: CardsHolder
)

class CardsHolder(
    val trainCarCardStore: TrainCarCardStore,
    val destinationTicketCardsStack: CardStack<DestinationTickerCard>
)

class PlayerState(
    var numberOfTrainCards: Int,
    var destinationTicketCards: MutableList<DestinationTickerCard>,
    var trainCarCards: MutableList<TrainCarCard>,
    var points: Int
)

class PlayerStates(private val states: List<PlayerState>) {

    fun getStateOf(player: Player): PlayerState {
        return states.getOrNull(player.id) ?: throw IllegalArgumentException("Illegal player $player")
    }

    companion object {

        fun createInitialStates(numberOfPlayers: Int): PlayerStates {
            return PlayerStates(
                states = (0..numberOfPlayers).map {
                    PlayerState(
                        numberOfTrainCards = 40,
                        points = 0,
                        destinationTicketCards = mutableListOf(),
                        trainCarCards = mutableListOf()
                    )
                }
            )
        }
    }
}

enum class TurnType {
    PLACE_TRAIN_CARS,
    GET_DESTINATION_TICKETS,
    GET_TRAIN_CAR_CARDS
}


class TrainCarPlacement(val road: Road, trainCar: TrainCar)

sealed class IntermediateGameState {

    class Starting: IntermediateGameState()

    class InitialPickingDestinationTicketCard: IntermediateGameState()

    data class ChoosingTurnType(val player: Player): IntermediateGameState()

    data class PickingTrainCarCard(val player: Player, val numberOfPickedCards: Int): IntermediateGameState()

    data class PickingDestinationTicketCard(val player: Player, val cards: List<DestinationTickerCard>): IntermediateGameState()

    data class PlacingTrainCars(val player: Player): IntermediateGameState()
}