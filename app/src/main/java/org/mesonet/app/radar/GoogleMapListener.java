package org.mesonet.app.radar;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;



public interface GoogleMapListener
{
    void MapFound(Context inContext, GoogleMap inMap);
}
