package org.tumba.aragorn.entity

import org.amshove.kluent.`should be`
import org.junit.Test
import org.tumba.aragorn.anyCity

class TrainCarPlacementValidatorTest {

    @Test
    fun testWagonPlacementBySameColor() {
        val roadColorToWagonKind = mapOf(
            Road.Color.GREEN to TrainCarCard.Kind.GREEN,
            Road.Color.WHITE to TrainCarCard.Kind.WHITE,
            Road.Color.PINK to TrainCarCard.Kind.PINK,
            Road.Color.BLACK to TrainCarCard.Kind.BLACK,
            Road.Color.BLUE to TrainCarCard.Kind.BLUE,
            Road.Color.YELLOW to TrainCarCard.Kind.YELLOW,
            Road.Color.RED to TrainCarCard.Kind.RED,
            Road.Color.ORANGE to TrainCarCard.Kind.ORANGE
        )
        val wagonPlacementValidator = TrainCarPlacementValidator()
        val lengthOfRoad = 5
        roadColorToWagonKind.forEach { roadColor, wagonKind ->
            val canBePlaced = wagonPlacementValidator.canRoadBePlacedByTrainCarCards(
                road = Road(
                    id = 0,
                    start = anyCity(),
                    end = anyCity(),
                    length = lengthOfRoad,
                    color = roadColor
                ),
                trainCars = (0 until lengthOfRoad).map { TrainCarCard(it, wagonKind) }
            )
            canBePlaced `should be` true
        }
    }

    @Test
    fun testWagonPlacementOnGray() {
        val roadColorToWagonKind = listOf(
            TrainCarCard.Kind.GREEN,
            TrainCarCard.Kind.WHITE,
            TrainCarCard.Kind.PINK,
            TrainCarCard.Kind.BLACK,
            TrainCarCard.Kind.BLUE,
            TrainCarCard.Kind.YELLOW,
            TrainCarCard.Kind.RED,
            TrainCarCard.Kind.ORANGE
        )
            .map { Road.Color.GRAY to it }
            .let { pairs -> mapOf(*pairs.toTypedArray()) }
        val wagonPlacementValidator = TrainCarPlacementValidator()
        val lengthOfRoad = 5
        roadColorToWagonKind.forEach { roadColor, wagonKind ->
            val canBePlaced = wagonPlacementValidator.canRoadBePlacedByTrainCarCards(
                road = Road(
                    id = 0,
                    start = anyCity(),
                    end = anyCity(),
                    length = lengthOfRoad,
                    color = roadColor
                ),
                trainCars = (0 until lengthOfRoad).map { TrainCarCard(it, wagonKind) }
            )
            canBePlaced `should be` true
        }
    }

    @Test
    fun testWagonPlacementWithMulticolor() {
        val wagonPlacementValidator = TrainCarPlacementValidator()
        val canBePlaced = wagonPlacementValidator.canRoadBePlacedByTrainCarCards(
            road = Road(
                id = 0,
                start = anyCity(),
                end = anyCity(),
                length = 5,
                color = Road.Color.RED
            ),
            trainCars = listOf(
                TrainCarCard(0, TrainCarCard.Kind.LOCOMOTIVE),
                TrainCarCard(0, TrainCarCard.Kind.RED),
                TrainCarCard(0, TrainCarCard.Kind.RED),
                TrainCarCard(0, TrainCarCard.Kind.LOCOMOTIVE),
                TrainCarCard(0, TrainCarCard.Kind.LOCOMOTIVE)
            )
        )
        canBePlaced `should be` true
    }

    @Test
    fun testWagonNotPlacementByColor() {
        val roadColorToWagonKind = mapOf(
            Road.Color.GREEN to TrainCarCard.Kind.WHITE,
            Road.Color.WHITE to TrainCarCard.Kind.RED,
            Road.Color.PINK to TrainCarCard.Kind.GREEN,
            Road.Color.BLACK to TrainCarCard.Kind.YELLOW,
            Road.Color.BLUE to TrainCarCard.Kind.GREEN,
            Road.Color.YELLOW to TrainCarCard.Kind.WHITE,
            Road.Color.RED to TrainCarCard.Kind.BLUE
        )
        val wagonPlacementValidator = TrainCarPlacementValidator()
        val lengthOfRoad = 2
        roadColorToWagonKind.forEach { roadColor, wagonKind ->
            val canBePlaced = wagonPlacementValidator.canRoadBePlacedByTrainCarCards(
                road = Road(
                    id = 0,
                    start = anyCity(),
                    end = anyCity(),
                    length = lengthOfRoad,
                    color = roadColor
                ),
                trainCars = (0 until lengthOfRoad).map { TrainCarCard(it, wagonKind) }
            )
            canBePlaced `should be` false
        }
    }

    @Test
    fun testWagonNotPlacementByColorWithMulticolor() {
        val wagonPlacementValidator = TrainCarPlacementValidator()
        val canBePlaced = wagonPlacementValidator.canRoadBePlacedByTrainCarCards(
            road = Road(
                id = 0,
                start = anyCity(),
                end = anyCity(),
                length = 5,
                color = Road.Color.RED
            ),
            trainCars = listOf(
                TrainCarCard(0, TrainCarCard.Kind.LOCOMOTIVE),
                TrainCarCard(0, TrainCarCard.Kind.RED),
                TrainCarCard(0, TrainCarCard.Kind.BLUE),
                TrainCarCard(0, TrainCarCard.Kind.LOCOMOTIVE),
                TrainCarCard(0, TrainCarCard.Kind.LOCOMOTIVE)
            )
        )
        canBePlaced `should be` false
    }

    @Test
    fun testWagonNotPlacementByLength() {
        val wagonPlacementValidator = TrainCarPlacementValidator()
        var canBePlaced = wagonPlacementValidator.canRoadBePlacedByTrainCarCards(
            road = Road(
                id = 0,
                start = anyCity(),
                end = anyCity(),
                length = 5,
                color = Road.Color.RED
            ),
            trainCars = (0 until 10).map {
                TrainCarCard(
                    it,
                    TrainCarCard.Kind.RED
                )
            }
        )
        canBePlaced `should be` false

        canBePlaced = wagonPlacementValidator.canRoadBePlacedByTrainCarCards(
            road = Road(
                id = 0,
                start = anyCity(),
                end = anyCity(),
                length = 10,
                color = Road.Color.RED
            ),
            trainCars = (0 until 3).map {
                TrainCarCard(
                    it,
                    TrainCarCard.Kind.RED
                )
            }
        )
        canBePlaced `should be` false
    }
}
