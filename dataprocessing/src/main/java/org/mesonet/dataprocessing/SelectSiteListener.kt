package org.mesonet.dataprocessing

import android.content.Context


interface SelectSiteListener {
    fun SetResult(inContext: Context, inResult: String)
}