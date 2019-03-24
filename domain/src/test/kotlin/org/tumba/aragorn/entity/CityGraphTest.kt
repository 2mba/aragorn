package org.tumba.aragorn.entity

import org.amshove.kluent.`should equal`
import org.junit.Test
import org.tumba.aragorn.`should contains same`
import org.tumba.aragorn.assertThrown
import org.tumba.aragorn.core.City
import org.tumba.aragorn.core.CityGraph
import org.tumba.aragorn.core.Road
import org.tumba.aragorn.core.RoadBuilder

class CityGraphTest {

    @Test
    fun testGetRoads2CityAnd2Road() {
        val moscow = City(0, "Moscow")
        val spb = City(1, "Spb")
        val novosibirsk = City(2, "Novosibirsk")
        val cities = listOf(moscow, spb, novosibirsk)

        val road1 = Road(0, moscow, spb, 4, Road.Color.BLACK)
        val road2 = Road(1, moscow, novosibirsk, 4, Road.Color.BLACK)
        val roads = listOf(road1, road2)

        val city = CityGraph(cities, roads)

        city.getRoadsOf(moscow) `should contains same` listOf(road1, road2)
        city.getRoadsOf(spb) `should contains same` listOf(road1)
        city.getRoadsOf(novosibirsk) `should contains same` listOf(road2)
    }

    @Test
    fun testGetRoadsWithDoubleRoad() {
        val moscow = City(0, "Moscow")
        val spb = City(1, "Spb")
        val novosibirsk = City(2, "Novosibirsk")
        val cities = listOf(moscow, spb, novosibirsk)

        val road1 = Road(0, moscow, spb, 4, Road.Color.BLACK)
        val road2 = Road(1, moscow, novosibirsk, 4, Road.Color.BLACK)
        val road3 = Road(2, moscow, spb, 3, Road.Color.RED)
        val roads = listOf(road1, road2, road3)

        val city = CityGraph(cities, roads)

        city.getRoadsOf(moscow) `should contains same` listOf(road1, road2, road3)
        city.getRoadsOf(spb) `should contains same` listOf(road1, road3)
        city.getRoadsOf(novosibirsk) `should contains same` listOf(road2)
    }

    @Test
    fun testShouldBeExceptionIfNoLinearCitiesIds() {
        val city1 = City(0, "")
        val city2 = City(0, "")
        val cities = listOf(city1, city2)

        assertThrown<IllegalArgumentException> {
            CityGraph(cities, emptyList())
        }
    }

    @Test
    fun testRoadBuilder() {
        val road1 = Road(
            3,
            City(0, ""),
            City(0, ""),
            1,
            Road.Color.BLACK
        )
        val road2 = Road(
            4,
            City(0, ""),
            City(0, ""),
            2,
            Road.Color.BLACK
        )

        val roadBuilder = RoadBuilder(3)

        val road1a = roadBuilder.road(
            City(0, ""),
            City(0, ""), 1, Road.Color.BLACK
        )
        val road2a = roadBuilder.road(
            City(0, ""),
            City(0, ""), 2, Road.Color.BLACK
        )

        road1 `should equal` road1a
        road2 `should equal` road2a
    }
}