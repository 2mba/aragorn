package org.tumba.entity

import org.tumba.entity.command.ICommand
import org.tumba.entity.command.ICommandProcessor


class Game(
    private var state: GameState,
    private val commandProcessor: ICommandProcessor
) {

    private val gameHelper = GameHelper(state)

    fun execute(command: ICommand) {
        commandProcessor.process(
            gameData = ICommandProcessor.GameData(state, gameHelper),
            command = command
        )
    }

    /*fun chooseTurnType(playerId: Int, type: TurnType) {
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
    }*/
}

interface IGameHelper {

    fun getPlayerById(id: Int): Player

    fun getTrainCarCards(player: Player, wagonCardIds: List<Int>): List<TrainCarCard>

    fun checkEnoughTrainCarsFor(player: Player, road: Road)

    fun ensureState(predicate: (IntermediateGameState) -> Boolean)

    fun equalTo(stateIntermediate: IntermediateGameState): (IntermediateGameState) -> Boolean
}

class GameHelper(private val gameState: GameState) : IGameHelper {

    override fun getPlayerById(id: Int): Player {
        return gameState.players.firstOrNull { it.id == id } ?: throw IllegalArgumentException("Unknown player id $id")
    }

    override fun getTrainCarCards(player: Player, wagonCardIds: List<Int>): List<TrainCarCard> {
        val wagonCards = gameState.playerStates.getStateOf(player).trainCarCards
        return wagonCardIds
            .map { id ->
                wagonCards.firstOrNull { it.id == id }
                    ?: throw TrainCarCardNotOwnedByUserException("Player $player not own cards $wagonCardIds")
            }
    }

    override fun checkEnoughTrainCarsFor(player: Player, road: Road) {
        val numberOfPlayerWagons = gameState.playerStates.getStateOf(player).numberOfTrainCars
        if (road.length > numberOfPlayerWagons) {
            throw NotEnoughTrainCarsException()
        }
    }

    override fun ensureState(predicate: (IntermediateGameState) -> Boolean) {
        if (!predicate.invoke(gameState.intermediateGameState)) throw OutOfTurnException()
    }

    override fun equalTo(stateIntermediate: IntermediateGameState): (IntermediateGameState) -> Boolean {
        return { intermediateGameState: IntermediateGameState -> stateIntermediate == intermediateGameState }
    }
}

class GameState(
    val players: List<Player>,
    val map: Map,
    val trainCarPlacements: List<TrainCarPlacement>,
    val playerStates: PlayerStates = PlayerStates.createInitialStates(players.size),
    var intermediateGameState: IntermediateGameState,
    var cardsHolder: CardsHolder
)

class CardsHolder(
    val trainCarCardStore: TrainCarCardStore,
    val destinationTicketCardsStack: CardStack<DestinationTickerCard>
)

class PlayerState(
    var numberOfTrainCars: Int,
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
                        numberOfTrainCars = 40,
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