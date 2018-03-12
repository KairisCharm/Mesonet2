package org.mesonet.app.maps;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.mesonet.app.R;
import org.mesonet.app.baseclasses.RecyclerViewHolder;
import org.mesonet.app.databinding.MapsProductViewHolderBinding;
import org.mesonet.app.webview.WebViewActivity;


public class MapsProductViewHolder extends RecyclerViewHolder<MapsProductViewHolder.MapsProductViewHolderData, MapsProductViewHolderBinding>
{
    FragmentManager mFragmentManager;

    public static MapsProductViewHolder NewInstance(FragmentManager inFragmentManager, ViewGroup inParent)
    {
        return new MapsProductViewHolder(inFragmentManager, (MapsProductViewHolderBinding)DataBindingUtil.inflate(LayoutInflater.from(inParent.getContext()), R.layout.maps_product_view_holder, inParent, false));
    }


    public MapsProductViewHolder(FragmentManager inFragmentManager, MapsProductViewHolderBinding inBinding) {
        super(inBinding);
        mFragmentManager = inFragmentManager;
    }



    @Override
    public void SetData(final MapsProductViewHolder.MapsProductViewHolderData inData) {
        if(inData != null)
        {
            final MapsProductViewHolderBinding binding = GetBinding();

            binding.productTitle.setText(inData.Product());
            binding.productSection.setText(inData.Section());

            if(inData.Section() == null || inData.Section().isEmpty())
                binding.productSection.setVisibility(View.GONE);
            else
                binding.productSection.setVisibility(View.VISIBLE);

            Picasso.with(binding.getRoot().getContext()).load(inData.Url()).into(binding.productPreview);

            binding.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(binding.getRoot().getContext(), WebViewActivity.class);
                    intent.putExtra(WebViewActivity.kTitle, inData.Product());
                    intent.putExtra(WebViewActivity.kUrl, inData.Url());
                    intent.putExtra(WebViewActivity.kAllowShare, true);
                    intent.putExtra(WebViewActivity.kInitialZoom, 1);
                    binding.getRoot().getContext().startActivity(intent);
                }
            });
        }
    }



    public interface MapsProductViewHolderData
    {
        String Product();
        String Section();
        String Url();
    }
}
