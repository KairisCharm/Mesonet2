package org.mesonet.app.maps

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.squareup.picasso.Picasso
import org.mesonet.app.R

import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.MapsProductViewHolderBinding
import org.mesonet.app.webview.WebViewActivity
import org.mesonet.dataprocessing.maps.MapsDataProvider


class MapsProductViewHolder(inBinding: MapsProductViewHolderBinding) : RecyclerViewHolder<MapsDataProvider.GenericMapData, MapsProductViewHolderBinding>(inBinding) {


    override fun SetData(inData: MapsDataProvider.GenericMapData?)
    {
        if(inData is MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct) {
            val binding = GetBinding()

            binding?.productTitle?.text = inData.GetTitle()
            binding?.productSection?.text = inData.GetSectionTitle()

            if (inData.GetSectionTitle() == null || inData.GetSectionTitle()?.isEmpty() != false)
                binding?.productSection?.visibility = View.GONE
            else
                binding?.productSection?.visibility = View.VISIBLE

            Picasso.with(binding?.root?.context).load(inData.GetImageUrl()).into(binding?.productPreview)

            binding?.layout?.setOnClickListener {
                it.isEnabled = false
                val intent = Intent(binding.root.context, WebViewActivity::class.java)
                intent.putExtra(WebViewActivity.kTitle, inData.GetTitle())
                intent.putExtra(WebViewActivity.kUrl, inData.GetImageUrl())
                intent.putExtra(WebViewActivity.kAllowShare, true)
                intent.putExtra(WebViewActivity.kInitialZoom, 1)
                intent.putExtra(WebViewActivity.kAllowUserZoom, true)
                intent.putExtra(WebViewActivity.kUpdateInterval, 60000L)
                binding.root.context.startActivity(intent)

                it.isEnabled = true
            }
        }
    }


    companion object {
        fun NewInstance(inParent: ViewGroup): MapsProductViewHolder {
            return MapsProductViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.maps_product_view_holder, inParent, false) as MapsProductViewHolderBinding)
        }
    }
}
