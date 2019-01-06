package org.tumba.entity

class Game(
    val players: List<Player>,
    val map: Map,
    val wagonPlacements: MutableList<TrainCarPlacement>,
    val currentPlayer: Player = players.firstOrNull() ?: throw IllegalArgumentException("No one player"),
    val playerStates: PlayerStates = PlayerStates.createInitialStates(players.size),
    val wagonCardStore: WagonCardStore,
    val trainCarPlacementValidator: ITrainCarPlacementValidator
) {

    fun placeTrainCar(
        playerId: Int,
        roadId: Int,
        wagonCardIds: List<Int>
    ) {
        val player = getPlayerById(playerId)
        val road = map.cityGraph.roads.firstOrNull { it.id == roadId }
            ?: throw IllegalArgumentException("Unknown road, id = $roadId")

        player.checkOutOfTurn()
        player.checkEnoughTrainCarsFor(road)
        val wagonCards = player.getTrainCarCards(wagonCardIds)

        if (trainCarPlacementValidator.canRoadBePlacedByTrainCarCards(road, wagonCards)) {
            placeTrainCarSafely(player, road, wagonCards)
        } else {
            throw IllegalTrainCarTypeException()
        }
    }

    private fun placeTrainCarSafely(player: Player, road: Road, trainCarCards: List<TrainCarCard>) {
        val playerState = playerStates.getStateOf(player)
        playerState.trainCarCards.removeAll(trainCarCards)
        playerState.numberOfTrainCards -= road.length
        wagonPlacements.add(TrainCarPlacement(road, TrainCar(player.id)))
    }

    private fun getPlayerById(id: Int) =
        players.firstOrNull { it.id == id } ?: throw IllegalArgumentException("Unknown player id $id")

    private fun Player.checkOutOfTurn() {
        if (this != currentPlayer) throw OutOfTurnException()
    }

    private fun Player.getTrainCarCards(wagonCardIds: List<Int>): List<TrainCarCard> {
        val wagonCards = playerStates.getStateOf(this).trainCarCards
        return wagonCardIds
            .map { id -> wagonCards.firstOrNull { it.id == id }
                ?: throw TrainCarCardNotOwnedByUserException("Player $this not own cards $wagonCardIds") }
    }

    private fun Player.checkEnoughTrainCarsFor(road: Road) {
        val numberOfPlayerWagons = playerStates.getStateOf(this).numberOfTrainCards
        if (road.length > numberOfPlayerWagons) {
            throw NotEnoughTrainCarsException()
        }
    }
}

class PlayerState(
    var numberOfTrainCards: Int,
    var destinationTickerCards: MutableList<DestinationTickerCard>,
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
                        destinationTickerCards = mutableListOf(),
                        trainCarCards = mutableListOf()
                    )
                }
            )
        }
    }
}

class TrainCarPlacement(val road: Road, trainCar: TrainCar)