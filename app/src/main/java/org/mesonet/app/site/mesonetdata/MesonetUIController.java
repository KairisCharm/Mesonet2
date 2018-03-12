package org.mesonet.app.site.mesonetdata;

import org.mesonet.app.dependencyinjection.PerFragment;
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
@PerFragment
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
            return "-";

        Double temp = mDataController.GetTemp();

        if(temp == null)
            return "-";

        return String.format(Locale.getDefault(),"%.0f", temp) + "°";
    }



    public String GetApparentTempString()
    {
        if(mDataController == null)
            return "-";

        Double apparentTemp = mDataController.GetApparentTemp();

        if(apparentTemp == null)
            return "-";

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



    public String GetDewpointString()
    {
        if(mDataController == null)
            return "-";

        Double dewPoint = mDataController.GetDewpoint();

        if(dewPoint == null)
            return "-";

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
            return "-";

        Double windSpd = mDataController.GetWindSpd();
        UnitConverter.CompassDirections direction = mDataController.GetWindDirection();

        if(windSpd == null || direction == null)
            return "-";

        String unit = "";

        switch (mDataController.GetUnitPreference())
        {
        case kMetric:
            unit = "mps";
            break;
        case kImperial:
            unit = "mph";
            break;
        }

        return direction.name() + " at " + String.format(Locale.getDefault(),"%.0f", windSpd) + " " + unit;
    }



    public String Get24HrRainfallString()
    {
        if(mDataController == null)
            return "-";

        Double rain24h = mDataController.Get24HrRain();

        if(rain24h == null)
            return "-";

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



    public String GetHumidityString()
    {
        if(mDataController == null)
            return "-";

        Integer humidity = mDataController.GetHumidity();

        if(humidity == null)
            return "-";

        return humidity.toString() + "%";
    }
    
    
    
    public String GetWindGustsString()
    {
        if(mDataController == null)
            return "-";

        Double windSpd = mDataController.GetMaxWind();

        if(windSpd == null)
            return "-";

        String unit = "";

        switch (mDataController.GetUnitPreference())
        {
            case kMetric:
                unit = "mps";
                break;
            case kImperial:
                unit = "mph";
                break;
        }

        return String.format(Locale.getDefault(),"%.0f", windSpd) + " " + unit;
    }



    public String GetPressureString()
    {
        if(mDataController == null)
            return "-";

        Double pressure = mDataController.GetPressure();

        if(pressure == null)
            return "-";

        String unit = "";
        String format = "";

        switch (mDataController.GetUnitPreference())
        {
            case kMetric:
                unit = "mb";
                format = "%.1f";
                break;
            case kImperial:
                unit = "inHg";
                format = "%.2f";
                break;
        }

        return String.format(Locale.getDefault(),format, pressure) + " " + unit;
    }



    public String GetTimeString()
    {
        String formattedString = "Observed at %s";

        if(mDataController == null)
            return String.format(formattedString, "-:-");

        Date time = mDataController.GetTime();

        if(time == null)
            return String.format(formattedString, "-:-");

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Chicago"));

        return String.format(formattedString, dateFormat.format(time));
    }
}
