package org.tumba.entity

interface IWagonPlacementValidator {

    fun canRoadBePlacedByWagonCards(road: Road, wagons: List<WagonCard>): Boolean
}

class WagonPlacementValidator : IWagonPlacementValidator {

    override fun canRoadBePlacedByWagonCards(road: Road, wagons: List<WagonCard>): Boolean {
        if (wagons.size != road.length) return false
        return when (road.color) {
            Road.Color.RED,
            Road.Color.YELLOW,
            Road.Color.BLUE,
            Road.Color.BLACK,
            Road.Color.PINK,
            Road.Color.WHITE,
            Road.Color.GREEN -> {
                val wagonKind = roadColorToWagonKindMatching.getValue(road.color)
                wagons.replaceMulticolorCardBy(wagonKind).all { it.kind == wagonKind }
            }
            Road.Color.GRAY -> wagons.groupBy { it.kind }.values.maxBy { it.size }?.size == road.length
        }
    }

    private fun List<WagonCard>.replaceMulticolorCardBy(wagonKind: WagonCard.Kind): List<WagonCard>{
        return map { card ->
            if (card.kind == WagonCard.Kind.MULTICOLOR) {
                WagonCard(0, wagonKind)
            } else {
                card
            }
        }
    }

    companion object {

        private val roadColorToWagonKindMatching = mapOf(
            Road.Color.RED to WagonCard.Kind.RED,
            Road.Color.YELLOW to WagonCard.Kind.YELLOW,
            Road.Color.BLUE to WagonCard.Kind.BLUE,
            Road.Color.BLACK to WagonCard.Kind.BLACK,
            Road.Color.PINK to WagonCard.Kind.PINK,
            Road.Color.WHITE to WagonCard.Kind.WHITE,
            Road.Color.GREEN to WagonCard.Kind.GREEN
        )
    }
}