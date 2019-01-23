package org.tumba.entity

import io.mockk.mockk
import org.junit.Test
import org.tumba.TestThreeCityMap
import org.tumba.entity.command.ICommandProcessor
import org.tumba.entity.command.PlaceTrainCarsCommand
import org.tumba.entity.command.PlaceTrainCarsCommandProcessor

class PlaceTrainCardsCommandProcessorTest {

    @Test(expected = IllegalTrainCarTypeException::class)
    fun testUnsuccessfulTrainCarPlacementCausedIncorrectCards() {
        val gameState = provideGameState1()
        val commandProcessor = PlaceTrainCarsCommandProcessor(TrainCarPlacementValidator())
        // Road(0, cityMoscow, citySpb, 5, Road.Color.RED)
        val command = PlaceTrainCarsCommand(
            playerId = 0,
            roadId = 0,
            wagonCardIds = listOf(0, 1, 2, 3, 4)
        )
        commandProcessor.process(
            gameData = ICommandProcessor.GameData(gameState, GameHelper(gameState)),
            command = command
        )
    }

    private fun provideGameState1(): GameState {
        val players = listOf(
            Player(0, "Player1", Player.Color.BLACK),
            Player(1, "Player2", Player.Color.BLUE)
        )
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
                    PlayerState( // Player1, id = 0
                        numberOfTrainCars = 40,
                        points = 0,
                        destinationTicketCards = mutableListOf(),
                        trainCarCards = trainCarCardsOfPlayer1
                    ),
                    mockk()
                )
            ),
            intermediateGameState = IntermediateGameState.PlacingTrainCars(players[0]),
            cardsHolder = mockk()
        )
    }

    @Test(expected = NotEnoughTrainCarsException::class)
    fun testUnsuccessfulTrainCarPlacementCausedNotEnoughTrainCars() {
        val gameState = provideGameState2()
        val commandProcessor = PlaceTrainCarsCommandProcessor(TrainCarPlacementValidator())
        // Road(0, cityMoscow, citySpb, 5, Road.Color.RED)
        // Placing to road 0 with 5 length and RED color.
        // Should be not enough train card
        val command = PlaceTrainCarsCommand(
            playerId = 1,
            roadId = 0,
            wagonCardIds = listOf(0, 1, 2, 3, 4)
        )
        commandProcessor.process(
            gameData = ICommandProcessor.GameData(gameState, GameHelper(gameState)),
            command = command
        )
    }

    private fun provideGameState2(): GameState {
        val players = listOf(
            Player(0, "Player1", Player.Color.BLACK),
            Player(1, "Player2", Player.Color.BLUE)
        )
        val trainCarCardsOfPlayer1 = (0..4).map { TrainCarCard(it, TrainCarCard.Kind.RED) }.toMutableList()
        return GameState(
            players = players,
            map = TestThreeCityMap.map,
            trainCarPlacements = mutableListOf(),
            playerStates = PlayerStates(
                states = listOf(
                    mockk(),
                    PlayerState( // Player2, id = 1
                        numberOfTrainCars = 3,
                        points = 0,
                        destinationTicketCards = mutableListOf(),
                        trainCarCards = trainCarCardsOfPlayer1
                    )
                )
            ),
            intermediateGameState = IntermediateGameState.PlacingTrainCars(players[1]),
            cardsHolder = mockk()
        )
    }
}