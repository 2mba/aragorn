package org.tumba.aragorn.entity.values

interface IGameConstants {

    val trainCarCardStoreMaxSize: Int
}

object TicketToRide: IGameConstants {

    override val trainCarCardStoreMaxSize: Int = 5
}