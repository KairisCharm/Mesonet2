package org.mesonet.app.radar;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import org.mesonet.app.dependencyinjection.PerFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;



@PerFragment
public class GoogleMapController implements GoogleMapListener, Observer
{
    private float mTransparency = 0;

    RadarDataController mRadarDataController;

    PlayPauseState mPlayPauseState = new PlayPauseState();

    @Inject
    RadarSiteDataProvider mRadarSiteDataProvider;

    @Inject
    VincentysFormulae mGreatCircleFormulas;


    private GoogleMapProvider mMapProvider = null;

    private Handler mPlayHandler = new Handler();



    @Inject
    public GoogleMapController(RadarDataController inRadarDataController)
    {
        mRadarDataController = inRadarDataController;
        mRadarDataController.addObserver(this);
    }



    public void SetProvider(GoogleMapProvider inMapProvider)
    {
        mMapProvider = inMapProvider;

        UpdateRadarImage();
    }



    public float GetTransparency()
    {
        return mTransparency;
    }



    public void SetTransparency(float inTransparency)
    {
        mTransparency = inTransparency;
        SetVisibleImage(mSelectedImage);
    }



    private List<GroundOverlay> mGroundOverlays = new ArrayList<>(6);

    private int mSelectedImage = 0;



    public void GetOverlays(Context inContext, final GoogleMap inMap)
    {
        final RadarModel.RadarDetailModel radarDetails = mRadarSiteDataProvider.GetRadarDetail();
        final List<RadarImageModel> radarImages = mRadarDataController.GetRadarImageDetails();

        if(radarImages != null && inMap != null && inContext != null)
        {
            inMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(radarDetails.GetLatitude(), radarDetails.GetLongitude()), 7)));

            for (int i = 0; i < radarImages.size(); i++)
            {
                final int index = i;
                radarImages.get(i).GetImage(inContext, new RadarDataController.ImageLoadedListener() {
                    @Override
                    public void BitmapLoaded(final Bitmap inResult) {
                        if (inResult != null) {
                            final BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(inResult);

                            float transparency = mTransparency;
                            if (index != mSelectedImage) {
                                transparency = 1;
                            }

                            LatLng position = new LatLng(radarDetails.GetLatitude(), radarDetails.GetLongitude());

                            double halfLatDelta = radarDetails.GetLatDelta() / 4;
                            double halfLonDelta = radarDetails.GetLonDelta() / 4;

                            double v = mGreatCircleFormulas.LatLonDegreesToDistance(radarDetails.GetLatitude(), radarDetails.GetLatitude(), radarDetails.GetLongitude() - halfLonDelta, radarDetails.GetLongitude() + halfLonDelta);
                            double v1 = mGreatCircleFormulas.LatLonDegreesToDistance(radarDetails.GetLatitude() - halfLatDelta, radarDetails.GetLatitude() + halfLatDelta, radarDetails.GetLongitude(), radarDetails.GetLongitude());

                            if (index >= mGroundOverlays.size() || mGroundOverlays.get(index) == null) {
                                GroundOverlayOptions options = new GroundOverlayOptions().image(bitmapDescriptor)
                                        .position(position, radarDetails.GetRange())
                                        .transparency(transparency);

                                while (index >= mGroundOverlays.size())
                                    mGroundOverlays.add(null);

                                mGroundOverlays.set(index, inMap.addGroundOverlay(options));
                            } else {
                                mGroundOverlays.get(index).setImage(bitmapDescriptor);
                                mGroundOverlays.get(index).setPosition(position);
                                mGroundOverlays.get(index).setDimensions(radarDetails.GetRange());
                                mGroundOverlays.get(index).setTransparency(transparency);
                            }

                            inResult.recycle();
                        } else {
                            mGroundOverlays.get(index).setImage(null);
                        }
                    }
                });
            }

            if(mMapProvider != null && radarImages.size() > mSelectedImage)
                mMapProvider.SetTime(radarImages.get(mSelectedImage).GetTimeString());

//            Play();
        }
    }



    public String GetRadarName()
    {
        return mRadarSiteDataProvider.GetRadarName();
    }



    private void UpdateRadarImage()
    {
        if(mMapProvider != null) {
            mMapProvider.GetMap(this);
        }
    }



    public PlayPauseState GetPlayPauseState()
    {
        return mPlayPauseState;
    }



    public void TogglePlay()
    {
        mPlayPauseState.SetPlayState(!mPlayPauseState.GetIsPlaying());

        if(mPlayPauseState.mIsPlaying) {
            mPlayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mPlayPauseState.GetIsPlaying())
                    {
                        int newIndex = mSelectedImage - 1;

                        if (newIndex < 0)
                            newIndex = mGroundOverlays.size() - 1;

                        SetVisibleImage(newIndex);
                        mPlayHandler.postDelayed(this, 500);
                    }
                }
            }, 500);
        }
        else
        {
            SetVisibleImage(0);
        }
    }



    private void SetVisibleImage(int inIndex)
    {
        mSelectedImage = inIndex;

        if(mSelectedImage < 0 || mSelectedImage >= mGroundOverlays.size())
            mSelectedImage = 0;

        for (int i = 0; i < mGroundOverlays.size(); i++) {
            if (mGroundOverlays.get(i) != null) {
                float transparency = 1;
                if (i == mSelectedImage)
                    transparency = mTransparency;

                mGroundOverlays.get(i).setTransparency(transparency);
            }
        }

        mMapProvider.SetTime(mRadarDataController.GetRadarImageDetails().get(mSelectedImage).GetTimeString());
    }



    @Override
    public void update(Observable observable, Object o)
    {
        UpdateRadarImage();
    }



    @Override
    public void MapFound(Context inContext, GoogleMap inMap)
    {
        GetOverlays(inContext, inMap);
    }



    public class PlayPauseState extends Observable
    {
        private boolean mIsPlaying;



        void SetPlayState(boolean inIsPlaying)
        {
            mIsPlaying = inIsPlaying;

            setChanged();
            notifyObservers();
        }



        @Override
        public void addObserver(Observer inObserver)
        {
            super.addObserver(inObserver);
            setChanged();
            notifyObservers();
        }



        public boolean GetIsPlaying()
        {
            return mIsPlaying;
        }
    }



    public interface GoogleMapProvider
    {
        void GetMap(GoogleMapListener inListener);
        void SetTime(String inTimeString);
    }
}
