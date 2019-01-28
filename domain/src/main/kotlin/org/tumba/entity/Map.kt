package org.tumba.entity

import kotlin.IllegalArgumentException
import kotlin.math.min


class TrainCar(val playerId: Int)

class City(
    val id: Int,
    val name: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as City

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "City(id=$id, name='$name')"
    }


}

interface ICityGraph {

    val cities: List<City>

    val roads: List<Road>

    fun getRoadsOf(city: City): List<Road> {
        return roads
            .asSequence()
            .filter { it.start == city || it.end == city }
            .toList()
    }

}

class Road(
    val id: Int,
    val start: City,
    val end: City,
    val length: Int,
    val color: Color
) {

    enum class Color {
        RED,
        YELLOW,
        BLUE,
        BLACK,
        PINK,
        WHITE,
        GREEN,
        ORANGE,
        GRAY
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Road

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "Road(id=$id, start=$start, end=$end, length=$length, color=$color)"
    }
}

class RoadBuilder(var id: Int = 0) {

    fun road(
        start: City,
        end: City,
        length: Int,
        color: Road.Color
    ): Road {
        return Road(
            id = id,
            start = start,
            end = end,
            length = length,
            color = color
        ).also {
            id++
        }
    }
}

fun roadBuilder(startId: Int = 0, block: RoadBuilder.() -> Unit) {
    block.invoke(RoadBuilder(id = startId))
}

class CityGraph(
    override val cities: List<City>,
    override val roads: List<Road>
) : ICityGraph {

    private val matrix: List<List<Road>>

    init {
        val sortedCities = cities.sortedBy { it.id }
        sortedCities.forEachIndexed { index, city ->
            if (index != city.id) throw IllegalArgumentException("Id's should be from 0 to N")
        }
        if (roads.distinct().size != roads.size) throw IllegalArgumentException("Roads id's should be distinct")

        matrix = sortedCities
            .sortedBy { it.id }
            .asSequence()
            .map { city ->
                roads
                    .filter { it.start == city || it.end == city }
                    .sortedBy { min(it.start.id, it.end.id) }
            }
            .toList()
    }

    override fun getRoadsOf(city: City): List<Road> {
        return matrix.getOrNull(city.id) ?: throw IllegalArgumentException("Unknown city with id: ${city.id}")
    }
}

class Map(
    val cityGraph: ICityGraph
)