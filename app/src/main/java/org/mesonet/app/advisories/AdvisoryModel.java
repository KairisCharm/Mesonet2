package org.mesonet.app.advisories;


import java.util.List;

public class AdvisoryModel
{
    enum AdvisoryLevel { S, N, Y, O, A, F, W;

        @Override
        public String toString() {
            switch(this) {
                case S: return "Statement";
                case N: return "Synopsis";
                case Y: return "Advisory";
                case O: return "Outlook";
                case A: return "Watch";
                case F: return "Forecast";
                case W: return "Warning";
            }

            return super.toString();
        }
    }

    enum AdvisoryType {RB, HF, LS, SW, BW, UP, TS, SU, TR, LO, HY,
    AS, HU, SM, TI, LE, SI, CF, MA, LB, SE, GL, SC, AF, TY, DS, ZF,
    LW, HI, SN, DU, SB, FR, FZ, FG, BS, HZ, SR, HT, EH, WC, EC, IP,
    WW, WS, HS, ZR, WI, HW, EW, FA, FL, FF, FW, IS, BZ, SV, TO;


        @Override
        public String toString() {

            switch (this)
            {
                case RB: return "Small Craft for Rough Bar";
                case HF: return "Hurricane Force Wind";
                case LS: return "Lakeshore Flood";
                case SW: return "Small Craft for Hazardous Seas";
                case BW: return "Brisk Wind";
                case UP: return "Ice Accretion";
                case TS: return "Tsunami";
                case SU: return "High Surf";
                case TR: return "Tropical Storm";
                case LO: return "Low Water";
                case HY: return "Hydrologic";
                case AS: return "Air Stagnation";
                case HU: return "Hurricane";
                case SM: return "Dense Smoke";
                case TI: return "Inland Tropical Storm";
                case LE: return "Lake Effect Snow";
                case SI: return "Small Craft for Winds";
                case CF: return "Coastal Flood";
                case MA: return "Marine";
                case LB: return "Lake Effect Snow and Blowing Snow";
                case SE: return "Hazardous Seas";
                case GL: return "Gale";
                case SC: return "Small Craft";
                case AF: return "Ashfall";
                case TY: return "Typhoon";
                case DS: return "Dust Storm";
                case ZF: return "Freezing Fog";
                case LW: return "Lake Wind";
                case HI: return "Inland Hurricane";
                case SN: return "Snow";
                case DU: return "Blowing Dust";
                case SB: return "Snow and Blowing Snow";
                case FR: return "Frost";
                case FZ: return "Freeze";
                case FG: return "Dense Fog";
                case BS: return "Blowing Snow";
                case HZ: return "Hard Freeze";
                case SR: return "Storm";
                case HT: return "Heat";
                case EH: return "Excessive Heat";
                case WC: return "Wind Chill";
                case EC: return "Extreme Cold";
                case IP: return "Sleet";
                case WW: return "Winter Weather";
                case WS: return "Winter Storm";
                case HS: return "Heavy Snow";
                case ZR: return "Freezing Rain";
                case WI: return "Wind";
                case HW: return "High Wind";
                case EW: return "Extreme Wind";
                case FA: return "Areal Flood";
                case FL: return "Flood";
                case FF: return "Flash Flood";
                case FW: return "Fire Weather";
                case IS: return "Ice Storm";
                case BZ: return "Blizzard";
                case SV: return "Severe Thunderstorm";
                case TO: return "Tornado";
            }

            return super.toString();
        }
    }

    AdvisoryTypeModel mAdvisoryType = new AdvisoryTypeModel();
    String mFilePath;
    List<String> mCountyCodes;

    public class AdvisoryTypeModel
    {
        AdvisoryLevel mAdvisoryLevel;
        AdvisoryType mAdvisoryType;
    }
}
