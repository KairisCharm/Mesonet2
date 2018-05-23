package org.mesonet.models.site

interface MesonetSiteList
{
    fun GetSite(inStid: String): MesonetSiteListModel.MesonetSiteModel?
}