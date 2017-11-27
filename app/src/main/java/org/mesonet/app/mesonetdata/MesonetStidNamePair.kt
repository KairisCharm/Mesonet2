@file:JvmName("MesonetStidNamePair")

package org.mesonet.app.mesonetdata



class MesonetStidNamePair
{
    fun Create(inSiteModel : MesonetSiteModel) : Map<String, String>
    {
        val result = HashMap<String, String>()

        inSiteModel.forEach(
        {
            result.put(it.key, it.value.mName);
        })

        return result
    }
}