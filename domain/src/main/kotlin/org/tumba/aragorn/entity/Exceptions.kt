package org.tumba.aragorn.entity

internal abstract class GameException(message: String = "") : RuntimeException(message)

internal class OutOfTurnException : GameException()

internal class OutOfStateException : GameException()

internal abstract class TrainCarCannotBePlacedException(message: String = "") : GameException(message)

internal class TrainCarCardNotOwnedByUserException(message: String = "") : TrainCarCannotBePlacedException(message)

internal class IllegalTrainCarTypeException(message: String = "") : TrainCarCannotBePlacedException(message)

internal class NotEnoughTrainCarsException(message: String = "") : TrainCarCannotBePlacedException(message)