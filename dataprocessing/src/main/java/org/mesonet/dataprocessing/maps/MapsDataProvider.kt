package org.mesonet.dataprocessing.maps

import io.reactivex.Observable
import java.io.Serializable

interface MapsDataProvider {
    companion object {
        val kAbbreviatedDisplayLimit = 4
        val kGenericSectionHeaderText = "Other"
    }

    fun GetMapsListObservable(): Observable<MutableList<MapAbbreviatedGroupDisplayData>>

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