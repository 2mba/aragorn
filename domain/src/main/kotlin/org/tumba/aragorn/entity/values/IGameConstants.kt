package org.tumba.aragorn.entity.values

internal interface IGameConstants {

    val trainCarCardStoreMaxSize: Int
}

internal object TicketToRide : IGameConstants {

    override val trainCarCardStoreMaxSize: Int = 5
}