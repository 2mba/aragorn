package org.tumba.entity

import org.amshove.kluent.`should be`
import org.junit.Test

import org.tumba.anyCity

class WagonPlacementValidatorTest {

    @Test
    fun testWagonPlacementBySameColor() {
        val roadColorToWagonKind = mapOf(
            Road.Color.GREEN to WagonCard.Kind.GREEN,
            Road.Color.WHITE to WagonCard.Kind.WHITE,
            Road.Color.PINK to WagonCard.Kind.PINK,
            Road.Color.BLACK to WagonCard.Kind.BLACK,
            Road.Color.BLUE to WagonCard.Kind.BLUE,
            Road.Color.YELLOW to WagonCard.Kind.YELLOW,
            Road.Color.RED to WagonCard.Kind.RED
        )
        val wagonPlacementValidator = WagonPlacementValidator()
        val lengthOfRoad = 5
        roadColorToWagonKind.forEach { roadColor, wagonKind ->
            val canBePlaced = wagonPlacementValidator.canRoadBePlacedByWagonCards(
                road = Road(
                    id = 0,
                    start = anyCity(),
                    end = anyCity(),
                    length = lengthOfRoad,
                    color = roadColor
                ),
                wagons = (0 until lengthOfRoad).map { WagonCard(it, wagonKind) }
            )
            canBePlaced `should be` true
        }
    }

    @Test
    fun testWagonPlacementOnGray() {
        val roadColorToWagonKind = listOf(
            WagonCard.Kind.GREEN,
            WagonCard.Kind.WHITE,
            WagonCard.Kind.PINK,
            WagonCard.Kind.BLACK,
            WagonCard.Kind.BLUE,
            WagonCard.Kind.YELLOW,
            WagonCard.Kind.RED
        )
            .map { Road.Color.GRAY to it }
            .let { pairs -> mapOf(*pairs.toTypedArray()) }
        val wagonPlacementValidator = WagonPlacementValidator()
        val lengthOfRoad = 5
        roadColorToWagonKind.forEach { roadColor, wagonKind ->
            val canBePlaced = wagonPlacementValidator.canRoadBePlacedByWagonCards(
                road = Road(
                    id = 0,
                    start = anyCity(),
                    end = anyCity(),
                    length = lengthOfRoad,
                    color = roadColor
                ),
                wagons = (0 until lengthOfRoad).map { WagonCard(it, wagonKind) }
            )
            canBePlaced `should be` true
        }
    }

    @Test
    fun testWagonPlacementWithMulticolor() {
        val wagonPlacementValidator = WagonPlacementValidator()
        val canBePlaced = wagonPlacementValidator.canRoadBePlacedByWagonCards(
            road = Road(
                id = 0,
                start = anyCity(),
                end = anyCity(),
                length = 5,
                color = Road.Color.RED
            ),
            wagons = listOf(
                WagonCard(0, WagonCard.Kind.MULTICOLOR),
                WagonCard(0, WagonCard.Kind.RED),
                WagonCard(0, WagonCard.Kind.RED),
                WagonCard(0, WagonCard.Kind.MULTICOLOR),
                WagonCard(0, WagonCard.Kind.MULTICOLOR)
            )
        )
        canBePlaced `should be` true
    }

    @Test
    fun testWagonNotPlacementByColor() {
        val roadColorToWagonKind = mapOf(
            Road.Color.GREEN to WagonCard.Kind.WHITE,
            Road.Color.WHITE to WagonCard.Kind.RED,
            Road.Color.PINK to WagonCard.Kind.GREEN,
            Road.Color.BLACK to WagonCard.Kind.YELLOW,
            Road.Color.BLUE to WagonCard.Kind.GREEN,
            Road.Color.YELLOW to WagonCard.Kind.WHITE,
            Road.Color.RED to WagonCard.Kind.BLUE
        )
        val wagonPlacementValidator = WagonPlacementValidator()
        val lengthOfRoad = 2
        roadColorToWagonKind.forEach { roadColor, wagonKind ->
            val canBePlaced = wagonPlacementValidator.canRoadBePlacedByWagonCards(
                road = Road(
                    id = 0,
                    start = anyCity(),
                    end = anyCity(),
                    length = lengthOfRoad,
                    color = roadColor
                ),
                wagons = (0 until lengthOfRoad).map { WagonCard(it, wagonKind) }
            )
            canBePlaced `should be` false
        }
    }

    @Test
    fun testWagonNotPlacementByColorWithMulticolor() {
        val wagonPlacementValidator = WagonPlacementValidator()
        val canBePlaced = wagonPlacementValidator.canRoadBePlacedByWagonCards(
            road = Road(
                id = 0,
                start = anyCity(),
                end = anyCity(),
                length = 5,
                color = Road.Color.RED
            ),
            wagons = listOf(
                WagonCard(0, WagonCard.Kind.MULTICOLOR),
                WagonCard(0, WagonCard.Kind.RED),
                WagonCard(0, WagonCard.Kind.BLUE),
                WagonCard(0, WagonCard.Kind.MULTICOLOR),
                WagonCard(0, WagonCard.Kind.MULTICOLOR)
            )
        )
        canBePlaced `should be` false
    }

    @Test
    fun testWagonNotPlacementByLength() {
        val wagonPlacementValidator = WagonPlacementValidator()
        var canBePlaced = wagonPlacementValidator.canRoadBePlacedByWagonCards(
            road = Road(
                id = 0,
                start = anyCity(),
                end = anyCity(),
                length = 5,
                color = Road.Color.RED
            ),
            wagons = (0 until 10).map { WagonCard(it, WagonCard.Kind.RED) }
        )
        canBePlaced `should be` false

        canBePlaced = wagonPlacementValidator.canRoadBePlacedByWagonCards(
            road = Road(
                id = 0,
                start = anyCity(),
                end = anyCity(),
                length = 10,
                color = Road.Color.RED
            ),
            wagons = (0 until 3).map { WagonCard(it, WagonCard.Kind.RED) }
        )
        canBePlaced `should be` false
    }
}
