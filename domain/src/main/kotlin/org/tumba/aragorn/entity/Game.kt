package org.tumba.aragorn.entity

import org.tumba.aragorn.entity.command.ICommand
import org.tumba.aragorn.entity.command.ICommandProcessor


internal interface IGame {

    fun execute(command: ICommand)
}

internal class Game(
    private val state: IGameState,
    private val commandProcessor: ICommandProcessor
): IGame {

    private val gameManager = GameManager(state)

    override fun execute(command: ICommand) {
        commandProcessor.process(
            gameManager = gameManager,
            command = command
        )
    }
}

internal class GameManager(override val state: IGameState) :
    IGameManager {

    override fun getPlayerById(id: Int): Player {
        return state.players.firstOrNull { it.id == id } ?: throw IllegalArgumentException("Unknown player id $id")
    }

    override fun getTrainCarCards(player: Player, wagonCardIds: List<Int>): List<TrainCarCard> {
        val wagonCards = state.playerStates.getStateOf(player).trainCarCards
        return wagonCardIds
            .map { id ->
                wagonCards.firstOrNull { it.id == id }
                    ?: throw TrainCarCardNotOwnedByUserException("Player $player not own cards $wagonCardIds")
            }
    }

    override fun checkEnoughTrainCarsFor(player: Player, road: Road) {
        val numberOfPlayerWagons = state.playerStates.getStateOf(player).numberOfTrainCars
        if (road.length > numberOfPlayerWagons) {
            throw NotEnoughTrainCarsException()
        }
    }

    override fun ensureState(predicate: (IntermediateGameState) -> Boolean) {
        if (!predicate.invoke(state.intermediateGameState)) throw OutOfTurnException()
    }

    override fun equalTo(stateIntermediate: IntermediateGameState): (IntermediateGameState) -> Boolean {
        return { intermediateGameState: IntermediateGameState -> stateIntermediate == intermediateGameState }
    }
}

internal interface IGameManager {

    val state: IGameState

    fun getPlayerById(id: Int): Player

    fun getTrainCarCards(player: Player, wagonCardIds: List<Int>): List<TrainCarCard>

    fun checkEnoughTrainCarsFor(player: Player, road: Road)

    fun ensureState(predicate: (IntermediateGameState) -> Boolean)

    fun equalTo(stateIntermediate: IntermediateGameState): (IntermediateGameState) -> Boolean
}

internal interface IGameState {
    val players: List<Player>
    val map: Map
    val trainCarPlacements: MutableList<TrainCarPlacement>
    val playerStates: PlayerStates
    var intermediateGameState: IntermediateGameState
    var cardsHolder: CardsHolder
}

internal class GameState(
    override val players: List<Player>,
    override val map: Map,
    override val trainCarPlacements: MutableList<TrainCarPlacement>,
    override val playerStates: PlayerStates,
    override var intermediateGameState: IntermediateGameState,
    override var cardsHolder: CardsHolder
) : IGameState

internal class CardsHolder(
    val trainCarCardStore: TrainCarCardStore,
    val destinationTicketCardsStack: CardStack<DestinationTickerCard>
)

internal class PlayerState(
    var numberOfTrainCars: Int,
    var destinationTicketCards: MutableList<DestinationTickerCard>,
    var trainCarCards: MutableList<TrainCarCard>,
    var points: Int
)

internal class PlayerStates(private val states: List<PlayerState>) {

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

internal class TrainCarPlacement(val road: Road, val trainCar: TrainCar)

internal sealed class IntermediateGameState {

    object Starting : IntermediateGameState()

    object InitialPickingDestinationTicketCard : IntermediateGameState()

    data class ChoosingTurnType(val player: Player): IntermediateGameState()

    data class PickingTrainCarCard(val player: Player, val numberOfPickedCards: Int): IntermediateGameState()

    data class PickingDestinationTicketCard(val player: Player, val cards: List<DestinationTickerCard>): IntermediateGameState()

    data class PlacingTrainCars(val player: Player): IntermediateGameState()

    companion object
}