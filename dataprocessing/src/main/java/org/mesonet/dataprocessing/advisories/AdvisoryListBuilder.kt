package org.mesonet.dataprocessiFilenamevisories

import android.nfc.FormatException
import org.mesonet.core.PerActivity
import org.mesonet.core.ThreadHandler
import org.mesonet.dataprocessing.advisories.AdvisoryModel
import javax.inject.Inject
import javax.inject.Singleton


@PerActivity
class AdvisoryListBuilder @Inject constructor(private var mThreadHandler: ThreadHandler)
{
    enum class AdvisoryDataType {
        kHeader, kContent
    }

    fun BuildList(inOriginalList: ArrayList<AdvisoryModel>?, inListener: AdvisoryListListener)
    {
        val endResult = java.util.ArrayList<Pair<AdvisoryDataType, AdvisoryData>>()

        mThreadHandler.Run("AdvisoryData", Runnable {
            var advisories = inOriginalList

            if(advisories != null) {
                var currentLevel: AdvisoryModel.AdvisoryLevel? = null
                var currentType: AdvisoryModel.AdvisoryType? = null

                advisories = Sort(advisories)

                if(advisories != null) {
                    for (i in advisories.indices) {
                        val advisoryType = advisories[i].mAdvisoryType.mAdvisoryType.toString() + " " + advisories[i].mAdvisoryType.mAdvisoryLevel
                        var counties = ""
                        var url: String? = null

                        if(advisories[i].mFilePath != null)
                            url = "https://www.mesonet.org/data/public/noaa/text/archive" + advisories[i].mFilePath!!
                        if(advisories[i].mCountyCodes != null)
                            counties = MakeCountyListString(advisories[i].mCountyCodes!!)
                        if (advisories[i].mAdvisoryType.mAdvisoryLevel != currentLevel || advisories[i].mAdvisoryType.mAdvisoryType != currentType) {
                            endResult.add(Pair(AdvisoryDataType.kHeader, object : AdvisoryData {
                                override fun AdvisoryType(): String {
                                    return advisoryType
                                }

                                override fun Counties(): String {
                                    return ""
                                }

                                override fun Url(): String? {
                                    return null
                                }

                            }))
                            currentLevel = advisories[i].mAdvisoryType.mAdvisoryLevel
                            currentType = advisories[i].mAdvisoryType.mAdvisoryType
                        }

                        endResult.add(Pair(AdvisoryDataType.kContent, object: AdvisoryData
                        {
                            override fun AdvisoryType(): String {
                                return advisoryType
                            }

                            override fun Counties(): String {
                                return counties
                            }

                            override fun Url(): String? {
                                return url
                            }
                        }))
                    }
                }
            }
        }, Runnable {
            inListener.ListComplete(endResult)
        })
    }


    internal fun Sort(inOriginal: ArrayList<AdvisoryModel>?): ArrayList<AdvisoryModel>? {
        if(inOriginal == null)
            return null

        val result = java.util.ArrayList(inOriginal)

        result.sortWith(Comparator { o1, o2 ->
            var sortResult = 0

            if(o1.mAdvisoryType.mAdvisoryLevel == null && o2.mAdvisoryType.mAdvisoryLevel != null)
                sortResult = 1

            if(o2.mAdvisoryType.mAdvisoryLevel == null && o1.mAdvisoryType.mAdvisoryLevel != null)
                sortResult = -1

            if(o1.mAdvisoryType.mAdvisoryType == null || o2.mAdvisoryType.mAdvisoryType != null)
                sortResult = 1

            if(o2.mAdvisoryType.mAdvisoryType == null || o1.mAdvisoryType.mAdvisoryType != null)
                sortResult = -1

            if(sortResult == 0)
                sortResult = o2?.mAdvisoryType?.mAdvisoryLevel?.let { o1?.mAdvisoryType?.mAdvisoryLevel?.compareTo(it)!! }!!

            if (sortResult == 0)
                sortResult = o2.mAdvisoryType.mAdvisoryType?.let { o1?.mAdvisoryType?.mAdvisoryType?.compareTo(it) }!!

            sortResult
        })

        return result
    }


    internal fun MakeCountyListString(inCounties: List<String>): String {
        val result = StringBuilder()

        for (i in inCounties.indices) {
            if (i > 0)
                result.append(", ")

            var countyName = inCounties[i]

            try{
                countyName = AdvisoryModel.County.valueOf(countyName).toString()
            }
            catch (e: IllegalArgumentException)
            {

            }


            result.append(countyName)
        }

        return result.toString()
    }


    interface AdvisoryListListener
    {
        fun ListComplete(inResult: MutableList<Pair<AdvisoryDataType, AdvisoryData>>)
    }



    interface AdvisoryData
    {
        fun AdvisoryType(): String
        fun Counties(): String
        fun Url(): String?
    }
}