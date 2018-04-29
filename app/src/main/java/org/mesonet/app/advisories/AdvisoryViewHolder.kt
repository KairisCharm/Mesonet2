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
import org.mesonet.dataprocessiFilenamevisories.AdvisoryListBuilder

class AdvisoryViewHolder(inBinding: AdvisoryViewHolderBinding) : RecyclerViewHolder<Pair<AdvisoryListBuilder.AdvisoryDataType, AdvisoryListBuilder.AdvisoryData>, AdvisoryViewHolderBinding>(inBinding) {


    override fun SetData(inData: Any?) {
        if(inData != null && inData is Pair<*,*> && inData.first is AdvisoryListBuilder.AdvisoryDataType && inData.second is AdvisoryListBuilder.AdvisoryData) {
            val binding = GetBinding()

            if(binding != null) {
                val advisoryData = inData.second as AdvisoryListBuilder.AdvisoryData
                binding.countyListTextView.text = advisoryData.Counties()

                binding.root.setOnClickListener {
                    val intent = Intent(binding.root.context, WebViewActivity::class.java)
                    intent.putExtra(WebViewActivity.kTitle, advisoryData.AdvisoryType())
                    intent.putExtra(WebViewActivity.kUrl, advisoryData.Url())
                    intent.putExtra(WebViewActivity.kUseGoogleDocs, true)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }



    companion object {
        fun NewInstance(inParent: ViewGroup): AdvisoryViewHolder {
            return AdvisoryViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.advisory_view_holder, inParent, false) as AdvisoryViewHolderBinding)
        }
    }
}
