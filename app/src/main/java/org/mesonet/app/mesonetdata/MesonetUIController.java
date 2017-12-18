package org.mesonet.app.mesonetdata;

import org.mesonet.app.dependencyinjection.DaggerMesonetApplicationComponent;
import org.mesonet.app.dependencyinjection.PerActivity;
import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.formulas.UnitConverter;
import org.mesonet.app.userdata.Preferences;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;

import javax.inject.Inject;


//TODO TESTS!!!!!
@PerChildFragment
public class MesonetUIController extends Observable implements Observer
{
    MesonetDataController mDataController;



    @Inject
    public MesonetUIController(MesonetDataController inMesonetDataController)
    {
        mDataController = inMesonetDataController;
        mDataController.addObserver(this);
    }



    @Override
    public void update(Observable observable, Object o) {
        setChanged();
        notifyObservers();
    }



    public String GetAirTempString()
    {
        if(mDataController == null)
            return "_";

        Double temp = mDataController.GetTemp();

        if(temp == null)
            return "_";

        return String.format(Locale.getDefault(),"%.0f", temp) + "°";
    }



    public String GetApparentTempString()
    {
        if(mDataController == null)
            return "_";

        Double apparentTemp = mDataController.GetApparentTemp();

        if(apparentTemp == null)
            return "_";

        String unit = "";

        Preferences.UnitPreference unitPreference = mDataController.GetUnitPreference();

        if(unitPreference != null)
        {
            switch (unitPreference)
            {
            case kMetric:
                unit = "C";
                break;
            case kImperial:
                unit = "F";
                break;
            }
        }

        return String.format(Locale.getDefault(),"%.0f", apparentTemp) + "°" + unit;
    }



    public String GetDewPointString()
    {
        if(mDataController == null)
            return "_";

        Double dewPoint = mDataController.GetDewPoint();

        if(dewPoint == null)
            return "_";

        String unit = "";

        switch (mDataController.GetUnitPreference())
        {
        case kMetric:
            unit = "C";
            break;
        case kImperial:
            unit = "F";
            break;
        }

        return String.format(Locale.getDefault(),"%.0f", dewPoint) + "°" + unit;
    }



    public String GetWindString ()
    {
        if(mDataController == null)
            return "_";

        Double windSpd = mDataController.GetWindSpd();
        UnitConverter.CompassDirections direction = mDataController.GetWindDirection();

        if(windSpd == null || direction == null)
            return "_";

        String unit = "";

        switch (mDataController.GetUnitPreference())
        {
        case kMetric:
            unit = "mps";
            break;
        case kImperial:
            unit = "MPH";
            break;
        }

        return direction.name() + " at " + String.format(Locale.getDefault(),"%.0f", windSpd) + " " + unit;
    }



    public String Get24HrRainfallString()
    {
        if(mDataController == null)
            return "_";

        Double rain24h = mDataController.Get24HrRain();

        if(rain24h == null)
            return "_";

        String unit = "";

        switch (mDataController.GetUnitPreference())
        {
        case kMetric:
            unit = "mm";
            break;
        case kImperial:
            unit = "in";
            break;
        }

        return String.format(Locale.getDefault(),"%.2f", rain24h) + " " + unit;
    }



    public String GetTimeString()
    {
        String formattedString = "Observed at %s";

        if(mDataController == null)
            return String.format(formattedString, "_:_");

        Date time = mDataController.GetTime();

        if(time == null)
            return String.format(formattedString, "_:_");

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Chicago"));

        return String.format(formattedString, dateFormat.format(time));
    }
}
