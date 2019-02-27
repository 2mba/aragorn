package org.tumba.entity

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.tumba.entity.command.ICommand
import org.tumba.entity.command.ICommandProcessor

class GameExecuteCommandTest {

    @Test
    fun testExecuteCommand() {
        val gameState = provideGameState()
        val commandProcessor : ICommandProcessor = mockk(relaxUnitFun = true)
        val game = Game(gameState, commandProcessor = commandProcessor)

        val command = TestCommand1()
        game.execute(command = command)

        verify(exactly = 1) {
            commandProcessor.process(
                gameManager = any(), // TODO ICommandProcessor.GameData(any(), any()),
                command = command
            )
        }
    }

    private fun provideGameState(): GameState {
        return GameState(
            players = listOf(Player(0, "", Player.Color.BLACK)),
            map = Map(CityGraph(listOf(), listOf())),
            trainCarPlacements = mutableListOf(),
            playerStates = PlayerStates(
                states = (0..1).map {
                    PlayerState(
                        numberOfTrainCars = 40,
                        points = 0,
                        destinationTicketCards = mutableListOf(),
                        trainCarCards = mutableListOf()
                    )
                }
            ),
            intermediateGameState = IntermediateGameState.Starting,
            cardsHolder = CardsHolder(
                trainCarCardStore = mockk(),
                destinationTicketCardsStack = CardStack(listOf(), listOf())
            )
        )
    }

    private class TestCommand1 : ICommand
}