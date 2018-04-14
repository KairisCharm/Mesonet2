package org.mesonet.app.maps


abstract class BaseMapsDataProvider {
    abstract fun GetMaps(inListListener: MapsListListener)


    interface MapsListListener {
        fun ListLoaded(inMapModel: MapsModel)
    }
}
