package org.tumba.aragorn.entity.processor

import io.mockk.mockk
import org.amshove.kluent.`should equal`
import org.junit.Test

import org.tumba.aragorn.TestThreeCityMap
import org.tumba.aragorn.core.Player
import org.tumba.aragorn.core.TrainCarCard
import org.tumba.aragorn.entity.*
import org.tumba.aragorn.entity.IntermediateGameState.*
import org.tumba.aragorn.entity.command.ChooseTurnTypeCommand
import org.tumba.aragorn.valuesExclude

class ChooseTurnTypeCommandProcessorTest {

    private val player1 = Player(0, "Player1", Player.Color.BLACK)
    private val player2 = Player(1, "Player2", Player.Color.BLUE)

    @Test(expected = OutOfTurnException::class)
    fun testShouldThrowOutOfTurnException() {
        val states: List<IntermediateGameState> = IntermediateGameState
            .valuesExclude<IntermediateGameState.PlacingTrainCars>()

        states.forEach { state ->
            val gameState = provideGameState(intermediateGameState = state)
            val processor = ChooseTurnTypeCommandProcessor()
            val command = ChooseTurnTypeCommand(
                playerId = 0,
                turnType = ChooseTurnTypeCommand.TurnType.PLACE_TRAIN_CARS
            )
            processor.process(GameManager(gameState), command)
        }
    }

    @Test(expected = OutOfTurnException::class)
    fun testShouldThrowOutOfTurnExceptionCausedByIncorrectPlayer() {
        val gameState = provideGameState(intermediateGameState = ChoosingTurnType(player1))
        val processor = ChooseTurnTypeCommandProcessor()
        val command = ChooseTurnTypeCommand(
            playerId = 1,
            turnType = ChooseTurnTypeCommand.TurnType.PLACE_TRAIN_CARS
        )
        processor.process(GameManager(gameState), command)
    }

    @Test
    fun testChoosePlaceTrainCards() {
        val command = ChooseTurnTypeCommand(
            playerId = 1,
            turnType = ChooseTurnTypeCommand.TurnType.PLACE_TRAIN_CARS
        )
        testSelectTurnType(
            initialState = ChoosingTurnType(player2),
            command = command,
            expectedState = PlacingTrainCars(player2)
        )
    }

    @Test
    fun testChooseGetDestinationTickets() {
        val command = ChooseTurnTypeCommand(
            playerId = 0,
            turnType = ChooseTurnTypeCommand.TurnType.GET_DESTINATION_TICKETS
        )
        testSelectTurnType(
            initialState = ChoosingTurnType(player1),
            command = command,
            expectedState = PickingDestinationTicketCard(player = player1, cards = emptyList())
        )
    }

    @Test
    fun testChooseGetTrainCarCards() {
        val command = ChooseTurnTypeCommand(
            playerId = 0,
            turnType = ChooseTurnTypeCommand.TurnType.GET_TRAIN_CAR_CARDS
        )
        testSelectTurnType(
            initialState = ChoosingTurnType(player1),
            command = command,
            expectedState = PickingTrainCarCard(player = player1, numberOfPickedCards = 0)
        )
    }

    private fun testSelectTurnType(
        initialState: IntermediateGameState,
        command: ChooseTurnTypeCommand,
        expectedState: IntermediateGameState
    ) {
        val gameState = provideGameState(intermediateGameState = initialState)
        val processor = ChooseTurnTypeCommandProcessor()
        gameState.intermediateGameState `should equal` initialState

        processor.process(GameManager(gameState), command)
        gameState.intermediateGameState `should equal` expectedState
    }

    private fun provideGameState(intermediateGameState: IntermediateGameState): GameState {
        val players = listOf(player1, player2)
        val trainCarCardsOfPlayer1 = mutableListOf(
            TrainCarCard(0, TrainCarCard.Kind.GREEN),
            TrainCarCard(1, TrainCarCard.Kind.GREEN),
            TrainCarCard(2, TrainCarCard.Kind.GREEN),
            TrainCarCard(3, TrainCarCard.Kind.GREEN),
            TrainCarCard(4, TrainCarCard.Kind.GREEN)
        )
        return GameState(
            players = players,
            map = TestThreeCityMap.map,
            trainCarPlacements = mutableListOf(),
            playerStates = PlayerStates(
                states = listOf(
                    PlayerState(
                        numberOfTrainCars = 40,
                        points = 0,
                        destinationTicketCards = mutableListOf(),
                        trainCarCards = trainCarCardsOfPlayer1
                    ),
                    mockk()
                )
            ),
            intermediateGameState = intermediateGameState,
            cardsHolder = mockk()
        )
    }
}