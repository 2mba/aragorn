package org.tumba.entity

class Game(
    val players: List<Player>,
    val map: Map,
    val wagonPlacements: MutableList<WagonsPlacement>,
    val currentPlayer: Player = players.firstOrNull() ?: throw IllegalArgumentException("No one player"),
    val playerStates: PlayerStates = PlayerStates.createInitialStates(players.size),
    val wagonCardStore: WagonCardStore,
    val wagonPlacementValidator: IWagonPlacementValidator
) {

    fun placeWagons(
        playerId: Int,
        roadId: Int,
        wagonCardIds: List<Int>
    ) {
        val player = getPlayerById(playerId)
        val road = map.cityGraph.roads.firstOrNull { it.id == roadId }
            ?: throw IllegalArgumentException("Unknown road, id = $roadId")

        player.checkOutOfTurn()
        player.checkEnoughWagons(road)
        val wagonCards = player.getWagonCards(wagonCardIds)

        if (wagonPlacementValidator.canRoadBePlacedByWagonCards(road, wagonCards)) {
            placeWagonSafely(player, road, wagonCards)
        } else {
            throw IllegalWagonTypeException()
        }
    }

    private fun placeWagonSafely(player: Player, road: Road, wagonCards: List<WagonCard>) {
        val playerState = playerStates.getStateOf(player)
        playerState.wagonCards.removeAll(wagonCards)
        playerState.numberOfWagons -= road.length
        wagonPlacements.add(WagonsPlacement(road, Wagon(player.id)))
    }

    private fun getPlayerById(id: Int) =
        players.firstOrNull { it.id == id } ?: throw IllegalArgumentException("Unknown player id $id")

    private fun Player.checkOutOfTurn() {
        if (this != currentPlayer) throw OutOfTurnException()
    }

    private fun Player.getWagonCards(wagonCardIds: List<Int>): List<WagonCard> {
        val wagonCards = playerStates.getStateOf(this).wagonCards
        return wagonCardIds
            .map { id -> wagonCards.firstOrNull { it.id == id }
                ?: throw WagonCardNotOwnedByUserException("Player $this not own cards $wagonCardIds") }
    }

    private fun Player.checkEnoughWagons(road: Road) {
        val numberOfPlayerWagons = playerStates.getStateOf(this).numberOfWagons
        if (road.length > numberOfPlayerWagons) {
            throw NotEnoughWagonsException()
        }
    }
}

class PlayerState(
    var numberOfWagons: Int,
    var travelCards: MutableList<TravelCard>,
    var wagonCards: MutableList<WagonCard>,
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
                        numberOfWagons = 40,
                        points = 0,
                        travelCards = mutableListOf(),
                        wagonCards = mutableListOf()
                    )
                }
            )
        }
    }
}

class PlayerWagons(
    private val wagons: List<Int>
) {

    fun getNumberOfWagon(playerId: Int) =
        wagons.getOrNull(playerId) ?: throw IllegalArgumentException("Unknown player id = $playerId")
}

class WagonsPlacement(val road: Road, wagon: Wagon)