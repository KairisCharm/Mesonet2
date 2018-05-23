package org.mesonet.models.advisories

import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AdvisoryModelCreator @Inject constructor() : AdvisoryCreator {

    override fun ParseAdvisoryFile(inAdvisoryFile: String): ArrayList<Advisory> {
        val result = ArrayList<Advisory>()
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

            val splitCounties = splitList[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            advisory.mCountyCodes = ArrayList()
            advisory.mCountyCodes!!.addAll(splitCounties)

            result.add(advisory)
            i++
        }

        return result
    }
}