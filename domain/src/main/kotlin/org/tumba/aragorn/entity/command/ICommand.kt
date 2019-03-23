package org.tumba.aragorn.entity.command

interface ICommand

class PlaceTrainCarsCommand(
    val playerId: Int,
    val roadId: Int,
    val wagonCardIds: List<Int>
) : ICommand


class ChooseTurnTypeCommand(
    val playerId: Int,
    val turnType: TurnType
) : ICommand {

    enum class TurnType {
        PLACE_TRAIN_CARS,
        GET_DESTINATION_TICKETS,
        GET_TRAIN_CAR_CARDS
    }
}