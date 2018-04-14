package org.mesonet.app.advisories


import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup

import org.mesonet.app.R
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.AdvisoryViewHolderBinding
import org.mesonet.app.webview.WebViewActivity

class AdvisoryViewHolder(inBinding: AdvisoryViewHolderBinding) : RecyclerViewHolder<AdvisoryModel, AdvisoryViewHolderBinding>(inBinding) {


    override fun SetData(inData: Any?) {
        if(inData != null && inData is AdvisoryModel) {
            val binding = GetBinding()

            binding?.countyListTextView!!.text = MakeCountyListString(inData.mCountyCodes!!)

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, WebViewActivity::class.java)
                intent.putExtra(WebViewActivity.kTitle, inData.mAdvisoryType.mAdvisoryType.toString() + " " + inData.mAdvisoryType.mAdvisoryLevel)
                intent.putExtra(WebViewActivity.kUrl, "https://www.mesonet.org/data/public/noaa/text/archive" + inData.mFilePath!!)
                intent.putExtra(WebViewActivity.kUseGoogleDocs, true)
                binding.root.context.startActivity(intent)
            }
        }
    }


    private fun MakeCountyListString(inCounties: List<String>): String {
        val result = StringBuilder()

        for (i in inCounties.indices) {
            if (i > 0)
                result.append(", ")

            result.append(inCounties[i])
        }

        return result.toString()
    }

    companion object {
        fun NewInstance(inParent: ViewGroup): AdvisoryViewHolder {
            return AdvisoryViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.advisory_view_holder, inParent, false) as AdvisoryViewHolderBinding)
        }
    }
}
