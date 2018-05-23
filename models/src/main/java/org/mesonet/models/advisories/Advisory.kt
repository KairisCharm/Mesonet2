package org.mesonet.models.advisories

interface Advisory
{
    fun GetLevel(): AdvisoryModel.AdvisoryLevel?
    fun GetType(): AdvisoryModel.AdvisoryType?
    fun GetFilePath(): String?
    fun GetCounties(): List<String>?

//    fun SetLevel(inLevel: AdvisoryModel.AdvisoryLevel)
//    fun SetType(inType: AdvisoryModel.AdvisoryType)
//    fun SetFilePath(inFilePath: String)
//    fun SetCounties(inCounties: List<String>)
}