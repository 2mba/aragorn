package org.tumba.entity

interface ITrainCarPlacementValidator {

    fun canRoadBePlacedByTrainCarCards(road: Road, trainCars: List<TrainCarCard>): Boolean
}

class TrainCarPlacementValidator : ITrainCarPlacementValidator {

    override fun canRoadBePlacedByTrainCarCards(road: Road, trainCars: List<TrainCarCard>): Boolean {
        if (trainCars.size != road.length) return false
        return when (road.color) {
            Road.Color.RED,
            Road.Color.YELLOW,
            Road.Color.BLUE,
            Road.Color.BLACK,
            Road.Color.PINK,
            Road.Color.WHITE,
            Road.Color.ORANGE,
            Road.Color.GREEN -> {
                val wagonKind = roadColorToTrainCarCardKindMatching.getValue(road.color)
                trainCars.replaceLocomotiveCardBy(wagonKind).all { it.kind == wagonKind }
            }
            Road.Color.GRAY -> trainCars.groupBy { it.kind }.values.maxBy { it.size }?.size == road.length
        }
    }

    private fun List<TrainCarCard>.replaceLocomotiveCardBy(wagonKind: TrainCarCard.Kind): List<TrainCarCard>{
        return map { card ->
            if (card.kind == TrainCarCard.Kind.LOCOMOTIVE) {
                TrainCarCard(0, wagonKind)
            } else {
                card
            }
        }
    }

    companion object {

        private val roadColorToTrainCarCardKindMatching = mapOf(
            Road.Color.RED to TrainCarCard.Kind.RED,
            Road.Color.YELLOW to TrainCarCard.Kind.YELLOW,
            Road.Color.BLUE to TrainCarCard.Kind.BLUE,
            Road.Color.BLACK to TrainCarCard.Kind.BLACK,
            Road.Color.PINK to TrainCarCard.Kind.PINK,
            Road.Color.WHITE to TrainCarCard.Kind.WHITE,
            Road.Color.GREEN to TrainCarCard.Kind.GREEN,
            Road.Color.ORANGE to TrainCarCard.Kind.ORANGE
        )
    }
}