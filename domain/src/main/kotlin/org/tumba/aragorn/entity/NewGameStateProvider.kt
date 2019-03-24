package org.tumba.aragorn.entity

import org.tumba.aragorn.core.*
import org.tumba.aragorn.core.Map
import org.tumba.aragorn.entity.command.BatchCommandProcessor
import org.tumba.aragorn.entity.command.ICommandProcessor
import org.tumba.aragorn.entity.command.TypedBatchCommandProcessor
import org.tumba.aragorn.entity.processor.PlaceTrainCarsCommandProcessor
import org.tumba.aragorn.entity.values.IGameConstants
import org.tumba.aragorn.entity.values.TicketToRide


internal interface IGameFactory {

    fun create(): Game
}

internal class NewGameFactory(
    private val newGameStateProvider: NewGameStateProvider,
    private val commandProcessorProvider: ICommandProcessorProvider
) : IGameFactory {

    override fun create(): Game {
        return Game(
            state = newGameStateProvider.provideGameState(),
            commandProcessor = commandProcessorProvider.provide()
        )
    }

    companion object {

        fun create(players: List<Player>): NewGameFactory {
            return NewGameFactory(
                newGameStateProvider = NewGameStateProvider.create(players),
                commandProcessorProvider = CommandProcessorProvider()
            )
        }
    }
}

internal interface IGameStateProvider {

    fun provideGameState(): GameState
}

internal class NewGameStateProvider(
    private val players: List<Player>,
    private val mapProvider: IMapProvider,
    private val playerStateProvider: IPlayerStatesProvider,
    private val trainCardsStoreProvider: IProvider<TrainCarCardStore>,
    private val destinationTicketCardsStackProvider: IProvider<CardStack<DestinationTickerCard>>
) : IGameStateProvider {

    override fun provideGameState(): GameState {
        return GameState(
            players = players,
            map = mapProvider.provide(),
            trainCarPlacements = mutableListOf(),
            playerStates = playerStateProvider.provide(),
            intermediateGameState = IntermediateGameState.Starting,
            cardsHolder = CardsHolder(
                trainCarCardStore = trainCardsStoreProvider.provide(),
                destinationTicketCardsStack = destinationTicketCardsStackProvider.provide()
            )
        )
    }

    companion object {

        fun create(players: List<Player>): NewGameStateProvider {
            return NewGameStateProvider(
                players = players,
                mapProvider = MapProvider(),
                playerStateProvider = NewGamePlayerStateProvider(players.size),
                trainCardsStoreProvider = NewGameTrainCardStoreProvider(TicketToRide),
                destinationTicketCardsStackProvider = NewGameDestinationTicketCardProvider()
            )
        }
    }
}

internal interface ICommandProcessorProvider :
    IProvider<ICommandProcessor>

internal class CommandProcessorProvider : ICommandProcessorProvider {

    override fun provide(): ICommandProcessor {
        val typedBatchCommandProcessor = TypedBatchCommandProcessor(
            processors = listOf(
                PlaceTrainCarsCommandProcessor(TrainCarPlacementValidator())
            )
        )
        return BatchCommandProcessor(
            processors = listOf(typedBatchCommandProcessor)
        )
    }
}

internal interface IPlayerStatesProvider : IProvider<PlayerStates>

internal interface IMapProvider : IProvider<Map>


internal class MapProvider : IMapProvider {

    override fun provide(): Map {
        return Map(CityGraph(listOf(), listOf()))
    }
}

internal class NewGamePlayerStateProvider(private val numberOfPlayers: Int) : IPlayerStatesProvider {

    override fun provide(): PlayerStates {
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

internal interface IProvider<T> {

    fun provide(): T
}

internal class NewGameDestinationTicketCardProvider :
    IProvider<CardStack<DestinationTickerCard>> {

    override fun provide(): CardStack<DestinationTickerCard> {
        return CardStack(
            cards = listOf(),
            droppedCards = emptyList()
        )
    }
}

internal class NewGameTrainCardStoreProvider(
    private val gameConstants: IGameConstants
) : IProvider<TrainCarCardStore> {

    override fun provide(): TrainCarCardStore {
        val cardStack = CardStack<TrainCarCard>(
            cards = listOf(),
            droppedCards = emptyList()
        )
        val cards = listOf<TrainCarCard>()
        return TrainCarCardStore(
            cards = cards,
            maxStoreSize = gameConstants.trainCarCardStoreMaxSize,
            stack = cardStack
        )
    }
}