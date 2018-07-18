package org.mesonet.models.advisories

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test
import javax.inject.Inject

class AdvisoryModelTests @Inject constructor()
{
    @Test
    fun NoInitAdvisoryModelTest()
    {
        val model = AdvisoryModel()

        assertNull(model.GetFilePath())
        assertNull(model.GetCounties())
        assertNull(model.GetLevel())
        assertNull(model.GetType())

        assertEquals(model.GetFilePath(), model.mFilePath)
        assertEquals(model.GetCounties(), model.mCountyCodes)
        assertEquals(model.GetLevel(), model.mAdvisoryType.mAdvisoryLevel)
        assertEquals(model.GetType(), model.mAdvisoryType.mAdvisoryType)
    }

    @Test
    fun InitAdvisoryModelTest()
    {
        val model = AdvisoryModel()

        model.mFilePath = "filePath"
        model.mCountyCodes = ArrayList()
        model.mCountyCodes?.add(AdvisoryModel.County.OKC001.name)
        model.mCountyCodes?.add(AdvisoryModel.County.OKC003.name)
        model.mCountyCodes?.add(AdvisoryModel.County.OKC005.name)
        model.mAdvisoryType.mAdvisoryLevel = AdvisoryModel.AdvisoryLevel.A
        model.mAdvisoryType.mAdvisoryType = AdvisoryModel.AdvisoryType.AS

        assertEquals(model.GetFilePath(), model.mFilePath)
        assertEquals(model.GetCounties(), model.mCountyCodes)
        assertEquals(model.GetLevel(), model.mAdvisoryType.mAdvisoryLevel)
        assertEquals(model.GetType(), model.mAdvisoryType.mAdvisoryType)
    }


    @Test
    fun AdvisoryLevelTest()
    {
        assertEquals(AdvisoryModel.AdvisoryLevel.S.toString(), "Statement")
        assertEquals(AdvisoryModel.AdvisoryLevel.N.toString(), "Synopsis")
        assertEquals(AdvisoryModel.AdvisoryLevel.Y.toString(), "Advisory")
        assertEquals(AdvisoryModel.AdvisoryLevel.O.toString(), "Outlook")
        assertEquals(AdvisoryModel.AdvisoryLevel.A.toString(), "Watch")
        assertEquals(AdvisoryModel.AdvisoryLevel.F.toString(), "Forecast")
        assertEquals(AdvisoryModel.AdvisoryLevel.W.toString(), "Warning")
    }


    @Test
    fun AdvisoryTypeTest()
    {
        assertEquals(AdvisoryModel.AdvisoryType.RB.toString(), "Small Craft for Rough Bar")
        assertEquals(AdvisoryModel.AdvisoryType.HF.toString(), "Hurricane Force Wind")
        assertEquals(AdvisoryModel.AdvisoryType.LS.toString(), "Lakeshore Flood")
        assertEquals(AdvisoryModel.AdvisoryType.SW.toString(), "Small Craft for Hazardous Seas")
        assertEquals(AdvisoryModel.AdvisoryType.BW.toString(), "Brisk Wind")
        assertEquals(AdvisoryModel.AdvisoryType.UP.toString(), "Ice Accretion")
        assertEquals(AdvisoryModel.AdvisoryType.TS.toString(), "Tsunami")
        assertEquals(AdvisoryModel.AdvisoryType.SU.toString(), "High Surf")
        assertEquals(AdvisoryModel.AdvisoryType.TR.toString(), "Tropical Storm")
        assertEquals(AdvisoryModel.AdvisoryType.LO.toString(), "Low Water")
        assertEquals(AdvisoryModel.AdvisoryType.HY.toString(), "Hydrologic")
        assertEquals(AdvisoryModel.AdvisoryType.AS.toString(), "Air Stagnation")
        assertEquals(AdvisoryModel.AdvisoryType.HU.toString(), "Hurricane")
        assertEquals(AdvisoryModel.AdvisoryType.SM.toString(), "Dense Smoke")
        assertEquals(AdvisoryModel.AdvisoryType.TI.toString(), "Inland Tropical Storm")
        assertEquals(AdvisoryModel.AdvisoryType.LE.toString(), "Lake Effect Snow")
        assertEquals(AdvisoryModel.AdvisoryType.SI.toString(), "Small Craft for Winds")
        assertEquals(AdvisoryModel.AdvisoryType.CF.toString(), "Coastal Flood")
        assertEquals(AdvisoryModel.AdvisoryType.MA.toString(), "Marine")
        assertEquals(AdvisoryModel.AdvisoryType.LB.toString(), "Lake Effect Snow and Blowing Snow")
        assertEquals(AdvisoryModel.AdvisoryType.SE.toString(), "Hazardous Seas")
        assertEquals(AdvisoryModel.AdvisoryType.GL.toString(), "Gale")
        assertEquals(AdvisoryModel.AdvisoryType.SC.toString(), "Small Craft")
        assertEquals(AdvisoryModel.AdvisoryType.AF.toString(), "Ashfall")
        assertEquals(AdvisoryModel.AdvisoryType.TY.toString(), "Typhoon")
        assertEquals(AdvisoryModel.AdvisoryType.DS.toString(), "Dust Storm")
        assertEquals(AdvisoryModel.AdvisoryType.ZF.toString(), "Freezing Fog")
        assertEquals(AdvisoryModel.AdvisoryType.LW.toString(), "Lake Wind")
        assertEquals(AdvisoryModel.AdvisoryType.HI.toString(), "Inland Hurricane")
        assertEquals(AdvisoryModel.AdvisoryType.SN.toString(), "Snow")
        assertEquals(AdvisoryModel.AdvisoryType.DU.toString(), "Blowing Dust")
        assertEquals(AdvisoryModel.AdvisoryType.SB.toString(), "Snow and Blowing Snow")
        assertEquals(AdvisoryModel.AdvisoryType.FR.toString(), "Frost")
        assertEquals(AdvisoryModel.AdvisoryType.FZ.toString(), "Freeze")
        assertEquals(AdvisoryModel.AdvisoryType.FG.toString(), "Dense Fog")
        assertEquals(AdvisoryModel.AdvisoryType.BS.toString(), "Blowing Snow")
        assertEquals(AdvisoryModel.AdvisoryType.HZ.toString(), "Hard Freeze")
        assertEquals(AdvisoryModel.AdvisoryType.SR.toString(), "Storm")
        assertEquals(AdvisoryModel.AdvisoryType.HT.toString(), "Heat")
        assertEquals(AdvisoryModel.AdvisoryType.EH.toString(), "Excessive Heat")
        assertEquals(AdvisoryModel.AdvisoryType.WC.toString(), "Wind Chill")
        assertEquals(AdvisoryModel.AdvisoryType.EC.toString(), "Extreme Cold")
        assertEquals(AdvisoryModel.AdvisoryType.IP.toString(), "Sleet")
        assertEquals(AdvisoryModel.AdvisoryType.WW.toString(), "Winter Weather")
        assertEquals(AdvisoryModel.AdvisoryType.WS.toString(), "Winter Storm")
        assertEquals(AdvisoryModel.AdvisoryType.HS.toString(), "Heavy Snow")
        assertEquals(AdvisoryModel.AdvisoryType.ZR.toString(), "Freezing Rain")
        assertEquals(AdvisoryModel.AdvisoryType.WI.toString(), "Wind")
        assertEquals(AdvisoryModel.AdvisoryType.HW.toString(), "High Wind")
        assertEquals(AdvisoryModel.AdvisoryType.EW.toString(), "Extreme Wind")
        assertEquals(AdvisoryModel.AdvisoryType.FA.toString(), "Areal Flood")
        assertEquals(AdvisoryModel.AdvisoryType.FL.toString(), "Flood")
        assertEquals(AdvisoryModel.AdvisoryType.FF.toString(), "Flash Flood")
        assertEquals(AdvisoryModel.AdvisoryType.FW.toString(), "Fire Weather")
        assertEquals(AdvisoryModel.AdvisoryType.IS.toString(), "Ice Storm")
        assertEquals(AdvisoryModel.AdvisoryType.BZ.toString(), "Blizzard")
        assertEquals(AdvisoryModel.AdvisoryType.SV.toString(), "Severe Thunderstorm")
        assertEquals(AdvisoryModel.AdvisoryType.TO.toString(), "Tornado")
    }


    @Test
    fun CountiesTest()
    {
        assertEquals(AdvisoryModel.County.OKC001.toString(), "Adair")
        assertEquals(AdvisoryModel.County.OKC003.toString(), "Alfalfa")
        assertEquals(AdvisoryModel.County.OKC005.toString(), "Atoka")
        assertEquals(AdvisoryModel.County.OKC007.toString(), "Beaver")
        assertEquals(AdvisoryModel.County.OKC009.toString(), "Beckham")
        assertEquals(AdvisoryModel.County.OKC011.toString(), "Blaine")
        assertEquals(AdvisoryModel.County.OKC013.toString(), "Bryan")
        assertEquals(AdvisoryModel.County.OKC015.toString(), "Caddo")
        assertEquals(AdvisoryModel.County.OKC017.toString(), "Canadian")
        assertEquals(AdvisoryModel.County.OKC019.toString(), "Carter")
        assertEquals(AdvisoryModel.County.OKC021.toString(), "Cherokee")
        assertEquals(AdvisoryModel.County.OKC023.toString(), "Choctaw")
        assertEquals(AdvisoryModel.County.OKC025.toString(), "Cimarron")
        assertEquals(AdvisoryModel.County.OKC027.toString(), "Cleveland")
        assertEquals(AdvisoryModel.County.OKC029.toString(), "Coal")
        assertEquals(AdvisoryModel.County.OKC031.toString(), "Comanche")
        assertEquals(AdvisoryModel.County.OKC033.toString(), "Cotton")
        assertEquals(AdvisoryModel.County.OKC035.toString(), "Craig")
        assertEquals(AdvisoryModel.County.OKC037.toString(), "Creek")
        assertEquals(AdvisoryModel.County.OKC039.toString(), "Custer")
        assertEquals(AdvisoryModel.County.OKC041.toString(), "Delaware")
        assertEquals(AdvisoryModel.County.OKC043.toString(), "Dewey")
        assertEquals(AdvisoryModel.County.OKC045.toString(), "Ellis")
        assertEquals(AdvisoryModel.County.OKC047.toString(), "Garfield")
        assertEquals(AdvisoryModel.County.OKC049.toString(), "Garvin")
        assertEquals(AdvisoryModel.County.OKC051.toString(), "Grady")
        assertEquals(AdvisoryModel.County.OKC053.toString(), "Grant")
        assertEquals(AdvisoryModel.County.OKC055.toString(), "Greer")
        assertEquals(AdvisoryModel.County.OKC057.toString(), "Harmon")
        assertEquals(AdvisoryModel.County.OKC059.toString(), "Harper")
        assertEquals(AdvisoryModel.County.OKC061.toString(), "Haskell")
        assertEquals(AdvisoryModel.County.OKC063.toString(), "Hughes")
        assertEquals(AdvisoryModel.County.OKC065.toString(), "Jackson")
        assertEquals(AdvisoryModel.County.OKC067.toString(), "Jefferson")
        assertEquals(AdvisoryModel.County.OKC069.toString(), "Johnston")
        assertEquals(AdvisoryModel.County.OKC071.toString(), "Kay")
        assertEquals(AdvisoryModel.County.OKC073.toString(), "Kingfisher")
        assertEquals(AdvisoryModel.County.OKC075.toString(), "Kiowa")
        assertEquals(AdvisoryModel.County.OKC077.toString(), "Latimer")
        assertEquals(AdvisoryModel.County.OKC079.toString(), "Le Flore")
        assertEquals(AdvisoryModel.County.OKC081.toString(), "Lincoln")
        assertEquals(AdvisoryModel.County.OKC083.toString(), "Logan")
        assertEquals(AdvisoryModel.County.OKC085.toString(), "Love")
        assertEquals(AdvisoryModel.County.OKC087.toString(), "McClain")
        assertEquals(AdvisoryModel.County.OKC089.toString(), "McCurtain")
        assertEquals(AdvisoryModel.County.OKC091.toString(), "McIntosh")
        assertEquals(AdvisoryModel.County.OKC093.toString(), "Major")
        assertEquals(AdvisoryModel.County.OKC095.toString(), "Marshall")
        assertEquals(AdvisoryModel.County.OKC097.toString(), "Mayes")
        assertEquals(AdvisoryModel.County.OKC099.toString(), "Murray")
        assertEquals(AdvisoryModel.County.OKC101.toString(), "Muskogee")
        assertEquals(AdvisoryModel.County.OKC103.toString(), "Noble")
        assertEquals(AdvisoryModel.County.OKC105.toString(), "Nowata")
        assertEquals(AdvisoryModel.County.OKC107.toString(), "Okfuskee")
        assertEquals(AdvisoryModel.County.OKC109.toString(), "Oklahoma")
        assertEquals(AdvisoryModel.County.OKC111.toString(), "Okmulgee")
        assertEquals(AdvisoryModel.County.OKC113.toString(), "Osage")
        assertEquals(AdvisoryModel.County.OKC115.toString(), "Ottawa")
        assertEquals(AdvisoryModel.County.OKC117.toString(), "Pawnee")
        assertEquals(AdvisoryModel.County.OKC119.toString(), "Payne")
        assertEquals(AdvisoryModel.County.OKC121.toString(), "Pittsburg")
        assertEquals(AdvisoryModel.County.OKC123.toString(), "Pontotoc")
        assertEquals(AdvisoryModel.County.OKC125.toString(), "Pottawatomie")
        assertEquals(AdvisoryModel.County.OKC127.toString(), "Pushmataha")
        assertEquals(AdvisoryModel.County.OKC129.toString(), "Roger Mills")
        assertEquals(AdvisoryModel.County.OKC131.toString(), "Rogers")
        assertEquals(AdvisoryModel.County.OKC133.toString(), "Seminole")
        assertEquals(AdvisoryModel.County.OKC135.toString(), "Sequoyah")
        assertEquals(AdvisoryModel.County.OKC137.toString(), "Stephens")
        assertEquals(AdvisoryModel.County.OKC139.toString(), "Texas")
        assertEquals(AdvisoryModel.County.OKC141.toString(), "Tillman")
        assertEquals(AdvisoryModel.County.OKC143.toString(), "Tulsa")
        assertEquals(AdvisoryModel.County.OKC145.toString(), "Wagoner")
        assertEquals(AdvisoryModel.County.OKC147.toString(), "Washington")
        assertEquals(AdvisoryModel.County.OKC149.toString(), "Washita")
        assertEquals(AdvisoryModel.County.OKC151.toString(), "Woods")
        assertEquals(AdvisoryModel.County.OKC153.toString(), "Woodward")

        assertEquals(AdvisoryModel.County.OKZ001.toString(), "Cimarron")
        assertEquals(AdvisoryModel.County.OKZ002.toString(), "Texas")
        assertEquals(AdvisoryModel.County.OKZ003.toString(), "Beaver")
        assertEquals(AdvisoryModel.County.OKZ004.toString(), "Harper")
        assertEquals(AdvisoryModel.County.OKZ005.toString(), "Woods")
        assertEquals(AdvisoryModel.County.OKZ006.toString(), "Alfalfa")
        assertEquals(AdvisoryModel.County.OKZ007.toString(), "Grant")
        assertEquals(AdvisoryModel.County.OKZ008.toString(), "Kay")
        assertEquals(AdvisoryModel.County.OKZ009.toString(), "Ellis")
        assertEquals(AdvisoryModel.County.OKZ010.toString(), "Woodward")
        assertEquals(AdvisoryModel.County.OKZ011.toString(), "Major")
        assertEquals(AdvisoryModel.County.OKZ012.toString(), "Garfield")
        assertEquals(AdvisoryModel.County.OKZ013.toString(), "Noble")
        assertEquals(AdvisoryModel.County.OKZ014.toString(), "Roger Mills")
        assertEquals(AdvisoryModel.County.OKZ015.toString(), "Dewey")
        assertEquals(AdvisoryModel.County.OKZ016.toString(), "Custer")
        assertEquals(AdvisoryModel.County.OKZ017.toString(), "Blaine")
        assertEquals(AdvisoryModel.County.OKZ018.toString(), "Kingfisher")
        assertEquals(AdvisoryModel.County.OKZ019.toString(), "Logan")
        assertEquals(AdvisoryModel.County.OKZ020.toString(), "Payne")
        assertEquals(AdvisoryModel.County.OKZ021.toString(), "Beckham")
        assertEquals(AdvisoryModel.County.OKZ022.toString(), "Washita")
        assertEquals(AdvisoryModel.County.OKZ023.toString(), "Caddo")
        assertEquals(AdvisoryModel.County.OKZ024.toString(), "Canadian")
        assertEquals(AdvisoryModel.County.OKZ025.toString(), "Oklahoma")
        assertEquals(AdvisoryModel.County.OKZ026.toString(), "Lincoln")
        assertEquals(AdvisoryModel.County.OKZ027.toString(), "Grady")
        assertEquals(AdvisoryModel.County.OKZ028.toString(), "McClain")
        assertEquals(AdvisoryModel.County.OKZ029.toString(), "Cleveland")
        assertEquals(AdvisoryModel.County.OKZ030.toString(), "Pottawatomie")
        assertEquals(AdvisoryModel.County.OKZ031.toString(), "Seminole")
        assertEquals(AdvisoryModel.County.OKZ032.toString(), "Hughes")
        assertEquals(AdvisoryModel.County.OKZ033.toString(), "Harmon")
        assertEquals(AdvisoryModel.County.OKZ034.toString(), "Greer")
        assertEquals(AdvisoryModel.County.OKZ035.toString(), "Kiowa")
        assertEquals(AdvisoryModel.County.OKZ036.toString(), "Jackson")
        assertEquals(AdvisoryModel.County.OKZ037.toString(), "Tillman")
        assertEquals(AdvisoryModel.County.OKZ038.toString(), "Comanche")
        assertEquals(AdvisoryModel.County.OKZ039.toString(), "Stephens")
        assertEquals(AdvisoryModel.County.OKZ040.toString(), "Garvin")
        assertEquals(AdvisoryModel.County.OKZ041.toString(), "Murray")
        assertEquals(AdvisoryModel.County.OKZ042.toString(), "Pontotoc")
        assertEquals(AdvisoryModel.County.OKZ043.toString(), "Coal")
        assertEquals(AdvisoryModel.County.OKZ044.toString(), "Cotton")
        assertEquals(AdvisoryModel.County.OKZ045.toString(), "Jefferson")
        assertEquals(AdvisoryModel.County.OKZ046.toString(), "Carter")
        assertEquals(AdvisoryModel.County.OKZ047.toString(), "Johnston")
        assertEquals(AdvisoryModel.County.OKZ048.toString(), "Atoka")
        assertEquals(AdvisoryModel.County.OKZ049.toString(), "Pushmataha")
        assertEquals(AdvisoryModel.County.OKZ050.toString(), "Love")
        assertEquals(AdvisoryModel.County.OKZ051.toString(), "Marshall")
        assertEquals(AdvisoryModel.County.OKZ052.toString(), "Bryan")
        assertEquals(AdvisoryModel.County.OKZ053.toString(), "Choctaw")
        assertEquals(AdvisoryModel.County.OKZ054.toString(), "Osage")
        assertEquals(AdvisoryModel.County.OKZ055.toString(), "Washington")
        assertEquals(AdvisoryModel.County.OKZ056.toString(), "Nowata")
        assertEquals(AdvisoryModel.County.OKZ057.toString(), "Craig")
        assertEquals(AdvisoryModel.County.OKZ058.toString(), "Ottawa")
        assertEquals(AdvisoryModel.County.OKZ059.toString(), "Pawnee")
        assertEquals(AdvisoryModel.County.OKZ060.toString(), "Tulsa")
        assertEquals(AdvisoryModel.County.OKZ061.toString(), "Rogers")
        assertEquals(AdvisoryModel.County.OKZ062.toString(), "Mayes")
        assertEquals(AdvisoryModel.County.OKZ063.toString(), "Delaware")
        assertEquals(AdvisoryModel.County.OKZ064.toString(), "Creek")
        assertEquals(AdvisoryModel.County.OKZ065.toString(), "Okfuskee")
        assertEquals(AdvisoryModel.County.OKZ066.toString(), "Okmulgee")
        assertEquals(AdvisoryModel.County.OKZ067.toString(), "Wagoner")
        assertEquals(AdvisoryModel.County.OKZ068.toString(), "Cherokee")
        assertEquals(AdvisoryModel.County.OKZ069.toString(), "Adair")
        assertEquals(AdvisoryModel.County.OKZ070.toString(), "Muskogee")
        assertEquals(AdvisoryModel.County.OKZ071.toString(), "McIntosh")
        assertEquals(AdvisoryModel.County.OKZ072.toString(), "Sequoyah")
        assertEquals(AdvisoryModel.County.OKZ073.toString(), "Pittsburg")
        assertEquals(AdvisoryModel.County.OKZ074.toString(), "Haskell")
        assertEquals(AdvisoryModel.County.OKZ075.toString(), "Latimer")
        assertEquals(AdvisoryModel.County.OKZ076.toString(), "Le Flore")
        assertEquals(AdvisoryModel.County.OKZ077.toString(), "McCurtain")
    }
}