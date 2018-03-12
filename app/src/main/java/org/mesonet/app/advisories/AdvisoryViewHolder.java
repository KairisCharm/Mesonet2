package org.mesonet.app.advisories;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.R;
import org.mesonet.app.baseclasses.RecyclerViewHolder;
import org.mesonet.app.databinding.AdvisoryViewHolderBinding;
import org.mesonet.app.databinding.HeaderViewHolderBinding;
import org.mesonet.app.webview.WebViewActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdvisoryViewHolder extends RecyclerViewHolder<AdvisoryModel, AdvisoryViewHolderBinding>
{
    public static AdvisoryViewHolder NewInstance(ViewGroup inParent)
    {
        return new AdvisoryViewHolder((AdvisoryViewHolderBinding) DataBindingUtil.inflate(LayoutInflater.from(inParent.getContext()), R.layout.advisory_view_holder, inParent, false));
    }

    public AdvisoryViewHolder(AdvisoryViewHolderBinding inBinding)
    {
        super(inBinding);
    }



    @Override
    public void SetData(final AdvisoryModel inData)
    {
        final AdvisoryViewHolderBinding binding = GetBinding();

        binding.countyListTextView.setText(MakeCountyListString(inData.mCountyCodes));

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(binding.getRoot().getContext(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.kTitle, inData.mAdvisoryType.mAdvisoryType + " " + inData.mAdvisoryType.mAdvisoryLevel);
                intent.putExtra(WebViewActivity.kUrl, "https://www.mesonet.org/data/public/noaa/text/archive" + inData.mFilePath);
                intent.putExtra(WebViewActivity.kUseGoogleDocs, true);
                binding.getRoot().getContext().startActivity(intent);
            }
        });
    }



    private String MakeCountyListString(List<String> inCounties)
    {
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < inCounties.size(); i++)
        {
            if(i > 0)
                result.append(", ");

            result.append(inCounties.get(i));
        }

        return result.toString();
    }
}
