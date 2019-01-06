package org.tumba.entity

abstract class GameException(message: String = "") : RuntimeException(message)

class OutOfTurnException : GameException()

abstract class WagonCannotBePlacedException(message: String = ""): GameException(message)

class WagonCardNotOwnedByUserException(message: String = "") : WagonCannotBePlacedException(message)

class IllegalWagonTypeException(message: String = "") : WagonCannotBePlacedException(message)

class NotEnoughWagonsException(message: String = "") : WagonCannotBePlacedException(message)