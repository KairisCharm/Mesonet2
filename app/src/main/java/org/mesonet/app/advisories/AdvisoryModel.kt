package org.mesonet.app.advisories

class AdvisoryModel {

    internal var mAdvisoryType = AdvisoryTypeModel()
    internal var mFilePath: String? = null
    internal var mCountyCodes: List<String>? = null

    internal enum class AdvisoryLevel {
        S, N, Y, O, A, F, W;

        override fun toString(): String {
            return when (this) {
                S -> "Statement"
                N -> "Synopsis"
                Y -> "Advisory"
                O -> "Outlook"
                A -> "Watch"
                F -> "Forecast"
                W -> "Warning"
            }
        }
    }

    internal enum class AdvisoryType {
        RB, HF, LS, SW, BW, UP, TS, SU, TR, LO, HY,
        AS, HU, SM, TI, LE, SI, CF, MA, LB, SE, GL, SC, AF, TY, DS, ZF,
        LW, HI, SN, DU, SB, FR, FZ, FG, BS, HZ, SR, HT, EH, WC, EC, IP,
        WW, WS, HS, ZR, WI, HW, EW, FA, FL, FF, FW, IS, BZ, SV, TO;


        override fun toString(): String {

            when (this) {
                RB -> return "Small Craft for Rough Bar"
                HF -> return "Hurricane Force Wind"
                LS -> return "Lakeshore Flood"
                SW -> return "Small Craft for Hazardous Seas"
                BW -> return "Brisk Wind"
                UP -> return "Ice Accretion"
                TS -> return "Tsunami"
                SU -> return "High Surf"
                TR -> return "Tropical Storm"
                LO -> return "Low Water"
                HY -> return "Hydrologic"
                AS -> return "Air Stagnation"
                HU -> return "Hurricane"
                SM -> return "Dense Smoke"
                TI -> return "Inland Tropical Storm"
                LE -> return "Lake Effect Snow"
                SI -> return "Small Craft for Winds"
                CF -> return "Coastal Flood"
                MA -> return "Marine"
                LB -> return "Lake Effect Snow and Blowing Snow"
                SE -> return "Hazardous Seas"
                GL -> return "Gale"
                SC -> return "Small Craft"
                AF -> return "Ashfall"
                TY -> return "Typhoon"
                DS -> return "Dust Storm"
                ZF -> return "Freezing Fog"
                LW -> return "Lake Wind"
                HI -> return "Inland Hurricane"
                SN -> return "Snow"
                DU -> return "Blowing Dust"
                SB -> return "Snow and Blowing Snow"
                FR -> return "Frost"
                FZ -> return "Freeze"
                FG -> return "Dense Fog"
                BS -> return "Blowing Snow"
                HZ -> return "Hard Freeze"
                SR -> return "Storm"
                HT -> return "Heat"
                EH -> return "Excessive Heat"
                WC -> return "Wind Chill"
                EC -> return "Extreme Cold"
                IP -> return "Sleet"
                WW -> return "Winter Weather"
                WS -> return "Winter Storm"
                HS -> return "Heavy Snow"
                ZR -> return "Freezing Rain"
                WI -> return "Wind"
                HW -> return "High Wind"
                EW -> return "Extreme Wind"
                FA -> return "Areal Flood"
                FL -> return "Flood"
                FF -> return "Flash Flood"
                FW -> return "Fire Weather"
                IS -> return "Ice Storm"
                BZ -> return "Blizzard"
                SV -> return "Severe Thunderstorm"
                TO -> return "Tornado"
            }
        }
    }

    inner class AdvisoryTypeModel {
        internal var mAdvisoryLevel: AdvisoryLevel? = null
        internal var mAdvisoryType: AdvisoryType? = null
    }
}
