package org.mesonet.app.advisories


import java.util.ArrayList
import java.util.Arrays

import javax.inject.Inject

class AdvisoryParser @Inject
constructor() {


    fun ParseAdvisoryFile(inAdvisoryFile: String): List<AdvisoryModel> {
        val result = ArrayList<AdvisoryModel>()
        val splitList = inAdvisoryFile.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var i = 0
        while (i + 5 < splitList.size) {
            val advisory = AdvisoryModel()

            val splitType = splitList[i].substring(splitList[i].length - 4).split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            try {
                advisory.mAdvisoryType.mAdvisoryType = AdvisoryModel.AdvisoryType.valueOf(splitType[0])
                advisory.mAdvisoryType.mAdvisoryLevel = AdvisoryModel.AdvisoryLevel.valueOf(splitType[1])
            } catch (e: Exception) {
                e.printStackTrace()
            }

            i += 4

            advisory.mFilePath = splitList[i++]

            advisory.mCountyCodes = ArrayList()

            val splitCounties = splitList[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            (advisory.mCountyCodes as ArrayList<String>).addAll(Arrays.asList(*splitCounties))

            result.add(advisory)
            i++
        }

        return result
    }
}
