package org.tumba.aragorn

import org.tumba.aragorn.core.City
import org.tumba.aragorn.core.CityGraph
import org.tumba.aragorn.core.Map
import org.tumba.aragorn.core.Road

/**
 * Moscow(0) ---Red(0)-- - Spb(1)
 *       \             /
 *      Black(1)    Gray(2)
 *         \       /
 *          Nsk(2)
 */

@Suppress("MemberVisibilityCanBePrivate")
internal object TestThreeCityMap {
    val cityMoscow = City(0, "Moscow")
    val citySpb = City(1, "Spb")
    val cityNsk = City(2, "Nsk")

    val road1 = Road(
        0,
        cityMoscow,
        citySpb, 5, Road.Color.RED
    )
    val road2 = Road(
        1,
        cityMoscow,
        cityNsk, 4, Road.Color.BLACK
    )
    val road3 = Road(2, citySpb, cityNsk, 3, Road.Color.GRAY)

    val cityGraph = CityGraph(
        cities = listOf(
            cityMoscow,
            citySpb,
            cityNsk
        ),
        roads = listOf(
            road1,
            road2,
            road3
        )
    )
    val map = Map(cityGraph)
}
