package org.mesonet.dataprocessing.maps

import android.os.Parcel
import android.os.Parcelable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import org.mesonet.models.maps.MapsList
import org.mesonet.network.DataDownloader
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MapsDataProvider @Inject constructor(internal var mDataDownloader: DataDownloader): Observable<MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>>()
{
    companion object {
        val kAbbreviatedDisplayLimit = 4
    }


    private var mLastUpdate = 0L


    private var mMapsList: MapsList? = null


    init {
        subscribeOn(Schedulers.computation())
    }


    override fun subscribeActual(inObserver: Observer<in MutableList<MapAbbreviatedGroupDisplayData>>) {
        if(mMapsList != null)
            inObserver.onNext(LoadMapsList(mMapsList))

        if((mLastUpdate - Date().time) > 300000) {
            mDataDownloader.GetMaps().observeOn(Schedulers.computation()).map {
                mMapsList = it
                LoadMapsList(mMapsList)
            }.subscribe(inObserver)
        }
    }


    internal fun LoadMapsList(inMapsList: MapsList?): MutableList<MapAbbreviatedGroupDisplayData>
    {
        val allGroups = inMapsList?.GetMain()
        val allSections = inMapsList?.GetSections()
        val allProducts = inMapsList?.GetProducts()

        var groupsWithSectionsWithGoodProducts = ArrayList<MapFullGroupDisplayDataImpl>()
        var sectionsWithGoodProducts = HashMap<String, MapFullGroupDisplayDataImpl.MapGroupSectionImpl>()
        var goodProducts = HashMap<String, MapFullGroupDisplayDataImpl.MapGroupSectionImpl.MapsProductImpl>()

        var abbreviatedGroups = ArrayList<MapAbbreviatedGroupDisplayData>()

        val orphanedGoodProducts = ArrayList<String>()
        val orphanedSectionsWithGoodProducts = ArrayList<String>()

        if(allProducts != null)
        {
            goodProducts = HashMap(allProducts.filter { !it.value.GetUrl().isNullOrBlank() }.mapValues {
                MapFullGroupDisplayDataImpl.MapGroupSectionImpl.MapsProductImpl(it.value.GetTitle(), null, it.value.GetUrl())
            })

            orphanedGoodProducts.addAll(goodProducts.keys)
        }

        if(goodProducts.isNotEmpty())
        {
            if(allSections != null) {
                sectionsWithGoodProducts = HashMap(allSections.filter { it.value.GetProducts().intersect(goodProducts.keys).isNotEmpty()}
                                                              .mapValues {
                                                                  MapFullGroupDisplayDataImpl.MapGroupSectionImpl(it.value.GetTitle(), HashMap(it.value.GetProducts().filter { goodProducts.keys.contains(it) }
                                                                                                                                                                            .associate { it to goodProducts[it]!!}))
                                                              })

                sectionsWithGoodProducts.forEach{ orphanedGoodProducts.removeAll(it.value.GetProducts().keys) }
            }
        }

        if(sectionsWithGoodProducts.isNotEmpty())
        {
            if(allGroups != null)
            {
                groupsWithSectionsWithGoodProducts = ArrayList(allGroups.filter { it.GetSections().intersect(sectionsWithGoodProducts.keys).isNotEmpty() }.map {
                    MapFullGroupDisplayDataImpl(it.GetTitle(), HashMap(it.GetSections().filter{sectionsWithGoodProducts.keys.contains(it)}.associate { it to sectionsWithGoodProducts[it]!! }))
                })

                groupsWithSectionsWithGoodProducts.forEach{ orphanedSectionsWithGoodProducts.removeAll(it.GetSections().keys)}
            }
        }

        if(orphanedGoodProducts.isNotEmpty())
        {
            val uncategorizedKey = "uncategorized"
            sectionsWithGoodProducts[uncategorizedKey] = MapFullGroupDisplayDataImpl.MapGroupSectionImpl("", HashMap(orphanedGoodProducts.associate { it to goodProducts[it]!! }))
            orphanedSectionsWithGoodProducts.add(uncategorizedKey)
        }

        if(orphanedSectionsWithGoodProducts.isNotEmpty())
        {
            groupsWithSectionsWithGoodProducts.add(MapFullGroupDisplayDataImpl("Uncategorized", HashMap(orphanedSectionsWithGoodProducts.associate { it to sectionsWithGoodProducts[it]!! })))
        }

        for(group in groupsWithSectionsWithGoodProducts)
        {
            val groupProducts = ArrayList<MapFullGroupDisplayData.MapGroupSection.MapsProduct>()

            for(section in group.GetSections())
            {
                for(product in section.value.GetProducts()) {
                    groupProducts.add(MapFullGroupDisplayDataImpl.MapGroupSectionImpl.MapsProductImpl(product.value.GetTitle(), section.value.GetTitle(), product.value.GetImageUrl()))
                    if (groupProducts.size >= kAbbreviatedDisplayLimit)
                        break
                }

                if (groupProducts.size >= kAbbreviatedDisplayLimit)
                    break
            }

            abbreviatedGroups.add(MapAbbreviatedGroupDisplayDataImpl(group.GetTitle(), groupProducts, group))
        }

        return abbreviatedGroups
    }



    class MapAbbreviatedGroupDisplayDataImpl(var mTitle: String? = null,
                                                     var mProducts: MutableList<MapFullGroupDisplayData.MapGroupSection.MapsProduct> = ArrayList(),
                                                     var mMapFullGroupDisplayData: MapFullGroupDisplayData): MapAbbreviatedGroupDisplayData
    {
        override fun GetTitle(): String?
        {
            return mTitle
        }


        override fun GetProducts(): MutableList<MapFullGroupDisplayData.MapGroupSection.MapsProduct>
        {
            return mProducts
        }


        override fun GetMapFullGroupDisplayData(): MapFullGroupDisplayData {
            return mMapFullGroupDisplayData
        }


        override fun GetGroupDisplayLimit(): Int {
            return kAbbreviatedDisplayLimit
        }
    }


    private class MapFullGroupDisplayDataImpl(val mTitle: String? = null,
                                              val mSections: HashMap<String, MapFullGroupDisplayData.MapGroupSection> = HashMap()): MapFullGroupDisplayData
    {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readSerializable() as HashMap<String, MapFullGroupDisplayData.MapGroupSection>)

        override fun GetTitle(): String? {
            return mTitle
        }

        override fun GetSections(): HashMap<String, MapFullGroupDisplayData.MapGroupSection> {
            return mSections
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(mTitle)
            parcel.writeSerializable(mSections)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<MapFullGroupDisplayDataImpl> {
            override fun createFromParcel(parcel: Parcel): MapFullGroupDisplayDataImpl {
                return MapFullGroupDisplayDataImpl(parcel)
            }

            override fun newArray(size: Int): Array<MapFullGroupDisplayDataImpl?> {
                return arrayOfNulls(size)
            }
        }
        class MapGroupSectionImpl(var mTitle: String? = null,
                                  var mProducts: HashMap<String, MapFullGroupDisplayData.MapGroupSection.MapsProduct> = HashMap()): MapFullGroupDisplayData.MapGroupSection
        {

            constructor(parcel: Parcel) : this(
                    parcel.readString(),
                    parcel.readSerializable() as HashMap<String, MapFullGroupDisplayData.MapGroupSection.MapsProduct>)

            override fun GetTitle(): String?
            {
                return mTitle
            }


            override fun GetProducts(): HashMap<String, MapFullGroupDisplayData.MapGroupSection.MapsProduct>
            {
                return mProducts
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {

                parcel.writeString(mTitle)
                parcel.writeSerializable(mProducts)
            }

            override fun describeContents(): Int {
                return 0
            }
            companion object CREATOR : Parcelable.Creator<MapGroupSectionImpl> {

                override fun createFromParcel(parcel: Parcel): MapGroupSectionImpl {
                    return MapGroupSectionImpl(parcel)
                }
                override fun newArray(size: Int): Array<MapGroupSectionImpl?> {
                    return arrayOfNulls(size)
                }


            }

            class MapsProductImpl(var mTitle: String? = null, var mSectionTitle: String? = null, var mImageUrl: String? = null): MapFullGroupDisplayData.MapGroupSection.MapsProduct
            {

                constructor(parcel: Parcel) : this(
                        parcel.readString(),
                        parcel.readString(),
                        parcel.readString())

                override fun GetTitle(): String? {
                    return mTitle
                }

                override fun GetSectionTitle(): String? {
                    return mSectionTitle
                }

                override fun GetImageUrl(): String? {
                    return mImageUrl
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeString(mTitle)
                    parcel.writeString(mSectionTitle)
                    parcel.writeString(mImageUrl)
                }
                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<MapsProductImpl> {
                    override fun createFromParcel(parcel: Parcel): MapsProductImpl {
                        return MapsProductImpl(parcel)
                    }
                    override fun newArray(size: Int): Array<MapsProductImpl?> {
                        return arrayOfNulls(size)
                    }

                }
            }

        }
    }


    interface MapAbbreviatedGroupDisplayData
    {
        fun GetTitle(): String?
        fun GetProducts(): MutableList<MapFullGroupDisplayData.MapGroupSection.MapsProduct>
        fun GetMapFullGroupDisplayData(): MapFullGroupDisplayData
        fun GetGroupDisplayLimit(): Int
    }


    interface MapFullGroupDisplayData: Parcelable
    {
        fun GetTitle(): String?
        fun GetSections(): HashMap<String, MapGroupSection>

        interface MapGroupSection: GenericMapData, Parcelable
        {
            fun GetTitle(): String?
            fun GetProducts(): HashMap<String, MapsProduct>


            interface MapsProduct: GenericMapData, Parcelable
            {
                fun GetTitle(): String?
                fun GetSectionTitle(): String?
                fun GetImageUrl(): String?
            }
        }
    }


    interface GenericMapData
}
