package org.mesonet.dataprocessing.maps

import android.util.Pair
import android.view.View
import com.google.gson.Gson
import org.mesonet.core.ThreadHandler
import org.mesonet.network.DataDownloader
import java.net.ContentHandler
import java.net.HttpURLConnection
import java.util.*
import javax.inject.Inject


class MapsDataProvider @Inject constructor() {
    enum class MapViewHolderTypes {
        kHeader, kProduct, kGroup
    }

    @Inject
    internal lateinit var mDataDownloader: DataDownloader

    @Inject
    internal lateinit var mThreadHandler: ThreadHandler

    private var mMapsModel: MapsModel? = null
    private var mTaskId: UUID? = null


    fun StartUpdates(inGroup: Int? = null, inListListener: MapsDataProvider.MapsListListener) {
        var result: Pair<MutableList<Any>?, String?>? = null

        mThreadHandler.Run("MapsData", Runnable {
            mDataDownloader.StartDownloads("http://content.mesonet.org/mesonet/mobile-app/products.json", object : DataDownloader.DownloadCallback {
                override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                    if (inResponseCode >= HttpURLConnection.HTTP_OK && inResponseCode <= HttpURLConnection.HTTP_PARTIAL) {
                        val newMapModel = Gson().fromJson(inResult, MapsModel::class.java)

                        if(mMapsModel == null || !mMapsModel?.equals(newMapModel)!!) {
                            mMapsModel = newMapModel

                            mThreadHandler.Run("MapsData", Runnable {
                                result = LoadMapsList(inGroup)
                            }, Runnable {
                                if(result != null)
                                    inListListener.ListLoaded(result?.first, result?.second)
                            })
                        }
                    }
                }

                override fun DownloadFailed() {

                }
            }, 3600000)

            result = LoadMapsList(inGroup)

        }, Runnable {
            if(result != null)
                inListListener.ListLoaded(result?.first, result?.second)
        })
    }


    internal fun SectionMaps(inSection: MapsModel.SectionModel?, inProducts: Map<String, MapsModel.ProductModel>?, inShowSection: Boolean): List<Any> {
        val result = ArrayList<Any>()
        for (k in 0 until inSection?.GetProducts()!!.size) {
            result.add(object : MapsProductData {
                override fun Product(): String {
                    return inProducts?.get(inSection.GetProducts()!![k])?.GetTitle()!!
                }

                override fun Section(): String? {
                    return if (!inShowSection) null else inSection.GetTitle()

                }

                override fun Url(): String {
                    return inProducts?.get(inSection.GetProducts()!![k])?.GetUrl()!!
                }
            })
        }

        return result
    }


    internal fun LoadMapsList(inGroup: Int? = null): Pair<MutableList<Any>?, String?>?
    {
        val result = ArrayList<Any>()
        var groupName: String? = null

        if(mMapsModel == null)
            return null
        else {

            val main = mMapsModel?.GetMain()
            val sections = mMapsModel?.GetSections()
            val products = mMapsModel?.GetProducts()

            if (main != null) {
                if (inGroup != null && inGroup >= 0 && inGroup < main.size) {
                    val sectionIds = main.get(inGroup).GetSections()

                    groupName = main.get(inGroup).GetTitle()

                    for (i in sectionIds?.indices!!) {
                        val section = sections?.get(sectionIds[i])

                        if (section != null) {
                            if (section.GetTitle() != null && !section.GetTitle()!!.isEmpty())
                                result.add(section.GetTitle()!!)

                            result.addAll(SectionMaps(section, products, false))
                        }
                    }
                } else {
                    for (i in main.indices) {

                        val mainSections = main.get(i).GetSections()

                        if (mainSections != null && mainSections.size > 0) {
                            main[i].GetTitle()?.let { result.add(it) }

                            val sectionProducts = ArrayList<Any>()
                            for (j in mainSections.indices) {
                                if (sections?.containsKey(mainSections[j])!!) {
                                    val section = sections.get(mainSections[j])

                                    sectionProducts.addAll(SectionMaps(section, products, true))
                                }
                            }

                            result.add(Pair<Int, List<Any>>(i, sectionProducts))
                        }
                    }
                }
            }
        }

        return Pair(result, groupName)
    }



    fun StopUpdates()
    {
        if(mTaskId != null)
            mDataDownloader.StopDownloads(mTaskId!!)
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
