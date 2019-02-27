package org.tumba.entity

import org.tumba.entity.command.ICommandProcessor


interface IGameFactory {

    fun create(): Game
}

class NewGameFactory(
    private val newGameStateProvider: NewGameStateProvider,
    private val commandProcessorProvider: CommandProcessorProvider
) : IGameFactory {

    override fun create(): Game {
        return Game(
            state = newGameStateProvider.provideGameState(),
            commandProcessor = commandProcessorProvider.provideCommandProcessor()
        )
    }
}

interface IGameStateProvider {

    fun provideGameState(): GameState
}

class NewGameStateProvider(
    private val players: List<Player>,
    private val mapProvider: IMapProvider,
    private val playerStateProvider: IPlayerStatesProvider
) : IGameStateProvider {

    override fun provideGameState(): GameState {
        return GameState(
            players = players,
            map = mapProvider.provideMapProvider(),
            trainCarPlacements = mutableListOf(),
            playerStates = playerStateProvider.providePlayerStates(),
            intermediateGameState = IntermediateGameState.Starting,
            cardsHolder = CardsHolder(
                trainCarCardStore = TODO(),
                destinationTicketCardsStack = TODO()
            )
        )
    }
}

interface ICommandProcessorProvider {

    fun provideCommandProcessor() : ICommandProcessor
}

class CommandProcessorProvider : ICommandProcessorProvider {

    override fun provideCommandProcessor(): ICommandProcessor {
        TODO()
    }
}

interface IPlayerStatesProvider {

    fun providePlayerStates(): PlayerStates
}

interface IMapProvider {

    fun provideMapProvider(): Map
}

class NewGamePlayerStateProvider(private val numberOfPlayers: Int) : IPlayerStatesProvider {

    override fun providePlayerStates(): PlayerStates {
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

interface IProvider<T> {

    fun provide(): T
}

class NewGameDestinationTicketCardProvider : IProvider<CardStack<DestinationTickerCard>> {

    override fun provide(): CardStack<DestinationTickerCard> {
        TODO()
    }
}

class NewGameTrainCardStoreProvider : IProvider<TrainCarCardStore> {

    override fun provide(): TrainCarCardStore {
        return TrainCarCardStore(TODO(), TODO(), TODO())
    }
}

