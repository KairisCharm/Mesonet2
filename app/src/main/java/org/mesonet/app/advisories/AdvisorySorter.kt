package org.mesonet.app.advisories

import java.util.ArrayList
import java.util.Comparator

import javax.inject.Inject


class AdvisorySorter @Inject
constructor() {


    fun Sort(inOriginal: List<AdvisoryModel>): List<AdvisoryModel>? {
        val result = ArrayList(inOriginal)

        result.sortWith(Comparator { o1, o2 ->
            var sortResult = o2?.mAdvisoryType?.mAdvisoryLevel?.let { o1?.mAdvisoryType?.mAdvisoryLevel?.compareTo(it) }

            if (sortResult == 0)
                sortResult = o2?.mAdvisoryType?.mAdvisoryType?.let { o1?.mAdvisoryType?.mAdvisoryType?.compareTo(it) }

            sortResult!!
        })

        return result
    }
}
