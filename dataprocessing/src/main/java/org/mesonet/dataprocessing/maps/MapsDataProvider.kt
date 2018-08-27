package org.mesonet.dataprocessing.maps

import io.reactivex.Observable
import org.mesonet.dataprocessing.LifecycleListener
import org.mesonet.dataprocessing.PageStateInfo
import java.io.Serializable

interface MapsDataProvider: LifecycleListener {
    companion object {
        val kAbbreviatedDisplayLimit = 4
        val kGenericSectionHeaderText = "Other"
    }

    fun GetMapsListObservable(): Observable<MutableList<MapAbbreviatedGroupDisplayData>>
    fun GetPageStateObservable(): Observable<PageStateInfo>

    interface MapAbbreviatedGroupDisplayData
    {
        fun GetTitle(): String?
        fun GetFullListSize(): Int
        fun GetProducts(): MutableList<MapFullGroupDisplayData.MapGroupSection.MapsProduct>
        fun GetMapFullGroupDisplayData(): MapFullGroupDisplayData
        fun GetGroupDisplayLimit(): Int
    }


    interface MapFullGroupDisplayData: Serializable
    {
        fun GetTitle(): String?
        fun GetSections(): HashMap<String, MapGroupSection>

        interface MapGroupSection: GenericMapData, Serializable
        {
            fun GetTitle(): String?
            fun GetTitleAsSubtext(): String?
            fun GetProducts(): HashMap<String, MapsProduct>


            interface MapsProduct: GenericMapData, Serializable
            {
                fun GetTitle(): String?
                fun GetSectionTitle(): String?
                fun GetImageUrl(): String?
            }
        }
    }


    interface GenericMapData
}