package org.mesonet.models.site.mesonetdata

interface MesonetDataCreator
{
    fun CreateMesonetData(inMesonetDataString: String): MesonetData?
}