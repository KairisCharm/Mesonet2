package org.mesonet.app.maps.traditional

import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import org.mesonet.app.R
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.TraditionalMapsViewHolderBinding
import org.mesonet.app.webview.WebViewActivity
import org.mesonet.models.maps.MapsList

class TraditionalMapsProductViewHolder(inBinding: TraditionalMapsViewHolderBinding) : RecyclerViewHolder<MapsList.Product, TraditionalMapsViewHolderBinding>(inBinding) {
    companion object {
        fun NewInstance(inParent: ViewGroup): TraditionalMapsProductViewHolder {
            return TraditionalMapsProductViewHolder(DataBindingUtil.inflate(LayoutInflater.from(inParent.context), R.layout.traditional_maps_view_holder, inParent, false))
        }
    }
    override fun SetData(inData: MapsList.Product?) {
        val binding = GetBinding()

        binding?.title = inData?.GetTitle()

        binding?.layout?.setOnClickListener{
            it.isEnabled = false
            val intent = Intent(binding.root.context, WebViewActivity::class.java)
            intent.putExtra(WebViewActivity.kTitle, inData?.GetTitle())
            intent.putExtra(WebViewActivity.kUrl, inData?.GetUrl())
            intent.putExtra(WebViewActivity.kAllowShare, true)
            intent.putExtra(WebViewActivity.kInitialZoom, 1)
            intent.putExtra(WebViewActivity.kAllowUserZoom, true)
            intent.putExtra(WebViewActivity.kUpdateInterval, 60000L)
            binding.root.context.startActivity(intent)

            it.isEnabled = true
        }
    }
}