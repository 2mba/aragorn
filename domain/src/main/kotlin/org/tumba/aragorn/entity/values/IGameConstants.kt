package org.tumba.aragorn.entity.values

import org.tumba.aragorn.core.TrainCarCard

internal interface IGameConstants {

    val trainCarCardStoreMaxSize: Int

    val trainCarCards: List<TrainCarCard>
}

internal object TicketToRide : IGameConstants {

    override val trainCarCardStoreMaxSize: Int = 5

    override val trainCarCards: List<TrainCarCard> = TrainCarCard.Kind
        .values().let { kinds ->
            var id = 0
            kinds
                .map { kind ->
                    (0..20).map {
                        TrainCarCard(
                            id = id++,
                            kind = kind
                        )
                    }
                }
                .flatten()
        }
}