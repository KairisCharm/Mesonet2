package org.mesonet.models.advisories

interface Advisory
{
    fun GetLevel(): AdvisoryModel.AdvisoryLevel?
    fun GetType(): AdvisoryModel.AdvisoryType?
    fun GetFilePath(): String?
    fun GetCounties(): List<String>?
}