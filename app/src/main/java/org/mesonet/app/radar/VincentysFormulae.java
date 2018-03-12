package org.mesonet.app.radar;


import javax.inject.Inject;

public class VincentysFormulae
{
    public final static double kSemiMajorAxis = 6378137.0;
    public final static double kSemiMinorAxis = 6356752.314245;
    public final static double kEarthFlattening = 1 / 298.257223563;



    @Inject
    public VincentysFormulae(){}



    public double DegreesToRadians(double inDegrees)
    {
        return inDegrees * (Math.PI / 180);
    }



    public double HaversineFunction(double inRadians)
    {
        return (1 - Math.cos(inRadians)) / 2;
    }



    public VincentyConvergenceData Converge(double inLonDifference, double inLat1, double inLat2)
    {
        VincentyConvergenceData data = new VincentyConvergenceData();
        data.λ = inLonDifference;

        return Converge(data, inLonDifference, inLat1, inLat2, 0);
    }



    public VincentyConvergenceData Converge(VincentyConvergenceData inData, double inLonDifference, double inLat1, double inLat2, int inAttempt) throws IllegalArgumentException
    {
        if(inAttempt >= 1000)
            throw new IllegalArgumentException("Failed to converge while calculating lat/lon distances");

        double tanU1 = (1-kEarthFlattening) * Math.tan(inLat1);
        double cosU1 = 1 / Math.sqrt((1 + tanU1*tanU1));
        double sinU1 = tanU1 * cosU1;

        double tanU2 = (1-kEarthFlattening) * Math.tan(inLat2);
        double cosU2 = 1 / Math.sqrt((1 + tanU2*tanU2));
        double sinU2 = tanU2 * cosU2;

        double iterationCheck = 0;

        boolean antimeridian = Math.abs(inLonDifference) > Math.PI;

        inData.sinλ = Math.sin(inData.λ);
        inData.cosλ = Math.cos(inData.λ);
        inData.sinSqσ = (cosU2*inData.sinλ) * (cosU2*inData.sinλ) + (cosU1*sinU2-sinU1*cosU2*inData.cosλ) * (cosU1*sinU2-sinU1*cosU2*inData.cosλ);
        if (inData.sinSqσ != 0) {
            inData.sinσ = Math.sqrt(inData.sinSqσ);
            inData.cosσ = sinU1 * sinU2 + cosU1 * cosU2 * inData.cosλ;
            inData.σ = Math.atan2(inData.sinσ, inData.cosσ);
            inData.sinα = cosU1 * cosU2 * inData.sinλ / inData.sinσ;
            inData.cosSqα = 1 - inData.sinα * inData.sinα;
            inData.cos2σM = (inData.cosSqα != 0) ? (inData.cosσ - 2 * sinU1 * sinU2 / inData.cosSqα) : 0; // equatorial line: cosSqα=0 (§6)
            inData.C = kEarthFlattening / 16 * inData.cosSqα * (4 + kEarthFlattening * (4 - 3 * inData.cosSqα));
            inData.λʹ = inData.λ;
            inData.λ = inLonDifference + (1 - inData.C) * kEarthFlattening * inData.sinα * (inData.σ + inData.C * inData.sinσ * (inData.cos2σM + inData.C * inData.cosσ * (-1 + 2 * inData.cos2σM * inData.cos2σM)));
            iterationCheck = antimeridian ? Math.abs(inData.λ) - Math.PI : Math.abs(inData.λ);
        }
        if (iterationCheck > Math.PI) throw new IllegalArgumentException("Failed to converge while calculating lat/lon distances");

        if(Math.abs(inData.λ-inData.λʹ) > 1e-12)
            return Converge(inData, inLonDifference, inLat1, inLat2, inAttempt + 1);

        return inData;
    }



    public double CalculateDistance(VincentyConvergenceData inConvergenceData)
    {
        double uSq = inConvergenceData.cosSqα * (kSemiMajorAxis*kSemiMajorAxis - kSemiMinorAxis*kSemiMinorAxis) / (kSemiMinorAxis*kSemiMinorAxis);
        double A = 1 + uSq/16384*(4096+uSq*(-768+uSq*(320-175*uSq)));
        double B = uSq/1024 * (256+uSq*(-128+uSq*(74-47*uSq)));
        double Δσ = B*inConvergenceData.sinσ*(inConvergenceData.cos2σM+B/4*(inConvergenceData.cosσ*(-1+2*inConvergenceData.cos2σM*inConvergenceData.cos2σM)-
                B/6*inConvergenceData.cos2σM*(-3+4*inConvergenceData.sinσ*inConvergenceData.sinσ)*(-3+4*inConvergenceData.cos2σM*inConvergenceData.cos2σM)));

        return kSemiMinorAxis*A*(inConvergenceData.σ-Δσ);
    }



    public double LatLonDegreesToDistance(double inLat1, double inLat2, double inLon1, double inLon2)
    {
        return LatLonRadiansToDistance(DegreesToRadians(inLat1), DegreesToRadians(inLat2), DegreesToRadians(inLon1), DegreesToRadians(inLon2));
    }



    public double LatLonRadiansToDistance(double inLat1, double inLat2, double inLon1, double inLon2)
    {
        double lonDifference = inLon2 - inLon1;
        return CalculateDistance(Converge(lonDifference, inLat1, inLat2));
    }



    public static class VincentyConvergenceData
    {
        double sinλ;
        double cosλ;
        double sinSqσ;
        double sinσ;
        double cosσ;
        double σ;
        double sinα;
        double cosSqα;
        double cos2σM;
        double C;
        double λʹ;
        double λ;
    }
}
