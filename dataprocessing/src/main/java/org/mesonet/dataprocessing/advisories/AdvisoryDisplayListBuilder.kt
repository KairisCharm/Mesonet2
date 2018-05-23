package org.mesonet.dataprocessiFilenamevisories

import org.mesonet.core.PerActivity
import org.mesonet.core.ThreadHandler
import org.mesonet.models.advisories.Advisory
import org.mesonet.models.advisories.AdvisoryModel
import javax.inject.Inject


@PerActivity
class AdvisoryDisplayListBuilder @Inject constructor(internal var mThreadHandler: ThreadHandler)
{
    enum class AdvisoryDataType {
        kHeader, kContent
    }

    fun BuildList(inOriginalList: ArrayList<Advisory>?, inListener: AdvisoryListListener)
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
                        var advisoryType = ""

                        if(advisories[i].GetType() != null)
                            advisoryType = advisories[i].GetType().toString()

                        if(advisories[i].GetLevel() != null)
                        {
                            if(!advisoryType.isEmpty())
                                advisoryType += " "

                            advisoryType += advisories[i].GetLevel()
                        }

                        if(advisoryType.isEmpty())
                            advisoryType = "Other"

                        var counties = ""
                        var url: String? = null

                        if(advisories[i].GetFilePath() != null)
                            url = "https://www.mesonet.org/data/public/noaa/text/archive" + advisories[i].GetFilePath()!!
                        if(advisories[i].GetCounties() != null)
                            counties = MakeCountyListString(advisories[i].GetCounties()!!)
                        if(counties.isEmpty())
                            counties = "Unknown Location"
                        if (advisories[i].GetLevel() != currentLevel || advisories[i].GetType() != currentType) {
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
                            currentLevel = advisories[i].GetLevel()
                            currentType = advisories[i].GetType()
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


    internal fun Sort(inOriginal: ArrayList<Advisory>?): ArrayList<Advisory>? {
        if(inOriginal == null)
            return null

        val result = java.util.ArrayList(inOriginal)

        result.sortWith(Comparator { o1, o2 ->
            var sortResult = 0

            if (o1.GetLevel() == null && o2.GetLevel() != null)
                sortResult = 1

            if (sortResult == 0 && o2.GetLevel() == null && o1.GetLevel() != null)
                sortResult = -1

            if (sortResult == 0 && o2.GetLevel() != null)
                sortResult = o2?.GetLevel()!!.compareTo(o1?.GetLevel()!!)

            if (sortResult == 0 && o1.GetType() == null && o2.GetType() != null)
                sortResult = 1

            if (sortResult == 0 && o2.GetType() == null && o1.GetType() != null)
                sortResult = -1

            if (sortResult == 0 && o2.GetType() != null)
                sortResult = o2?.GetType()!!.compareTo(o1?.GetType()!!)

            sortResult
        })

        return result
    }


    internal fun MakeCountyListString(inCounties: List<String>): String {
        val result = StringBuilder()

        for (i in inCounties.indices) {

            var countyName = inCounties[i]

            try{
                countyName = AdvisoryModel.County.valueOf(countyName).toString()
            }
            catch (e: IllegalArgumentException)
            {

            }

            if(!countyName.isEmpty()) {
                if (i > 0)
                    result.append(", ")

                result.append(countyName)
            }
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