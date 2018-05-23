package org.mesonet.dataprocessing.maps

import android.util.Pair
import com.google.gson.Gson
import org.mesonet.core.ThreadHandler
import org.mesonet.models.maps.MapsModel
import org.mesonet.network.DataDownloader
import java.net.HttpURLConnection
import java.util.*
import javax.inject.Inject


class MapsDataProvider @Inject constructor(private var mThreadHandler: ThreadHandler) {
    enum class MapViewHolderTypes {
        kHeader, kProduct, kGroup
    }

    internal var mDataDownloader: DataDownloader

    private var mMapsModel: MapsModel? = null


    init {
        mDataDownloader = DataDownloader(mThreadHandler)
    }


    fun Download(inGroup: Int? = null, inListListener: MapsDataProvider.MapsListListener) {
        var result: Pair<MutableList<Any>?, String?>? = null

        mThreadHandler.Run("MapsData", Runnable {
            mDataDownloader.SingleUpdate("http://content.mesonet.org/mesonet/mobile-app/products.json", object : DataDownloader.DownloadCallback {
                override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                    if (inResponseCode >= HttpURLConnection.HTTP_OK && inResponseCode <= HttpURLConnection.HTTP_PARTIAL) {
                        val newMapModel = Gson().fromJson(inResult, MapsModel::class.java)

                        if(mMapsModel == null || !mMapsModel?.equals(newMapModel)!!) {
                            mMapsModel = newMapModel

                            mThreadHandler.Run("MapsData", Runnable {
                                result = LoadMapsList(inGroup, mMapsModel)
                            }, Runnable {
                                if(result != null)
                                    inListListener.ListLoaded(result?.first, result?.second)
                                else
                                    inListListener.ListLoaded(null, null)
                            })
                        }
                    }
                }

                override fun DownloadFailed() {

                }
            })

            result = LoadMapsList(inGroup, mMapsModel)

        }, Runnable {
            if(result != null)
                inListListener.ListLoaded(result?.first, result?.second)
        })
    }


    internal fun SectionMaps(inSection: MapsModel.SectionModel?, inProducts: Map<String, MapsModel.ProductModel>?, inShowSection: Boolean): List<Any> {
        val result = ArrayList<Any>()

        if(inSection != null && inSection.GetProducts() != null && inSection.GetProducts()!!.isNotEmpty() && inProducts != null && inProducts.isNotEmpty()) {
            for (k in 0 until inSection.GetProducts()!!.size) {

                if(inProducts.containsKey(inSection.GetProducts()!![k])) {
                    result.add(object : MapsProductData {
                        override fun Product(): String {
                            return inProducts[inSection.GetProducts()!![k]]?.GetTitle()!!
                        }

                        override fun Section(): String? {
                            return if (!inShowSection) null else inSection.GetTitle()

                        }

                        override fun Url(): String {
                            return inProducts[inSection.GetProducts()!![k]]?.GetUrl()!!
                        }
                    })
                }
            }
        }

        return result
    }


    internal fun LoadMapsList(inGroup: Int? = null, inMapsModel: MapsModel?): Pair<MutableList<Any>?, String?>?
    {
        val result = ArrayList<Any>()
        var groupName: String? = null

        if(inMapsModel == null)
            return null
        else {

            val main = inMapsModel.GetMain()
            val sections = inMapsModel.GetSections()
            val products = inMapsModel.GetProducts()

            if (main != null && main.isNotEmpty()) {
                if (inGroup != null && inGroup >= 0 && inGroup < main.size) {
                    val sectionIds = main[inGroup].GetSections()

                    groupName = main[inGroup].GetTitle()

                    if(sectionIds != null && sectionIds.isNotEmpty() && sections != null && sections.isEmpty()) {
                        for (i in sectionIds.indices) {
                            if(sections.containsKey(sectionIds[i])) {
                                val section = sections[sectionIds[i]]

                                if (section != null) {
                                    val sectionResults = SectionMaps(section, products, false)
                                    if (sectionResults.isNotEmpty()) {
                                        if (section.GetTitle() != null && !section.GetTitle()!!.isEmpty())
                                            result.add(section.GetTitle()!!)

                                        result.addAll(sectionResults)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (i in main.indices) {

                        val mainSections = main[i].GetSections()

                        if (mainSections != null && mainSections.isNotEmpty()) {

                            val sectionProducts = ArrayList<Any>()
                            if(sections != null) {
                                for (j in mainSections.indices) {
                                    val section = sections[mainSections[j]]
                                    val sectionList = SectionMaps(section, products, true)
                                    if (sectionList.isNotEmpty() && sections.containsKey(mainSections[j]) && sections[mainSections[j]] != null) {

                                        sectionProducts.addAll(sectionList)
                                    }
                                }

                                if (sectionProducts.isNotEmpty()) {
                                    main[i].GetTitle()?.let { result.add(it) }
                                    result.add(Pair<Int, List<Any>>(i, sectionProducts))
                                }
                            }
                        }
                    }
                }
            }
        }

        if(result.isEmpty())
            return null

        return Pair(result, groupName)
    }


    interface MapsListListener {
        fun ListLoaded(inMapsList: MutableList<Any>?, inGroupName: String? = null)
    }


    interface MapsProductData {
        fun Product(): String
        fun Section(): String?
        fun Url(): String
    }
}
