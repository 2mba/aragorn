package org.tumba.aragorn.entity

abstract class GameException(message: String = "") : RuntimeException(message)

class OutOfTurnException : GameException()

class OutOfStateException : GameException()

abstract class TrainCarCannotBePlacedException(message: String = ""): GameException(message)

class TrainCarCardNotOwnedByUserException(message: String = "") : TrainCarCannotBePlacedException(message)

class IllegalTrainCarTypeException(message: String = "") : TrainCarCannotBePlacedException(message)

class NotEnoughTrainCarsException(message: String = "") : TrainCarCannotBePlacedException(message)