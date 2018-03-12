package org.mesonet.app.radar;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import org.mesonet.app.R;

import java.util.Observable;
import java.util.Observer;



public class PlayPauseButton extends FloatingActionButton implements Observer
{
    private GoogleMapController.PlayPauseState mPlayPauseState;


    public PlayPauseButton(Context inContext, AttributeSet inAttrs)
    {
        super(inContext, inAttrs);
    }



    public void SetPlayPauseState(GoogleMapController.PlayPauseState inPlayPauseState)
    {
        mPlayPauseState = inPlayPauseState;
        mPlayPauseState.addObserver(this);
    }



    @Override
    public void update(Observable o, Object arg)
    {
        if(mPlayPauseState.GetIsPlaying())
            setImageResource(R.drawable.ic_pause_white_36dp);
        else
            setImageResource(R.drawable.ic_play_arrow_white_36dp);
    }
}