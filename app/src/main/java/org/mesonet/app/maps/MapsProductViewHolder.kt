package org.mesonet.app.maps

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.squareup.picasso.Picasso

import org.mesonet.app.R
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.MapsProductViewHolderBinding
import org.mesonet.app.webview.WebViewActivity
import org.mesonet.dataprocessing.maps.MapsDataProvider


class MapsProductViewHolder(private var mFragmentManager: FragmentManager, inBinding: MapsProductViewHolderBinding) : RecyclerViewHolder<MapsDataProvider.MapsProductData, MapsProductViewHolderBinding>(inBinding) {

    override fun SetData(inData: Any?) {
        if (inData != null && inData is MapsDataProvider.MapsProductData) {
            val binding = GetBinding()

            binding?.productTitle?.text = inData.Product()
            binding?.productSection?.text = inData.Section()

            if (inData.Section() == null || inData.Section()!!.isEmpty())
                binding?.productSection?.visibility = View.GONE
            else
                binding?.productSection?.visibility = View.VISIBLE

            Picasso.with(binding?.root?.getContext()).load(inData.Url()).into(binding?.productPreview)

            binding?.layout?.setOnClickListener(View.OnClickListener {
                val intent = Intent(binding.root.context, WebViewActivity::class.java)
                intent.putExtra(WebViewActivity.kTitle, inData.Product())
                intent.putExtra(WebViewActivity.kUrl, inData.Url())
                intent.putExtra(WebViewActivity.kAllowShare, true)
                intent.putExtra(WebViewActivity.kInitialZoom, 1)
                intent.putExtra(WebViewActivity.kAllowUserZoom, true)
                binding.root.context.startActivity(intent)
            })
        }
    }


    companion object {

        fun NewInstance(inFragmentManager: FragmentManager, inParent: ViewGroup): MapsProductViewHolder {
            return MapsProductViewHolder(inFragmentManager, DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.maps_product_view_holder, inParent, false) as MapsProductViewHolderBinding)
        }
    }
}
