package org.mesonet.models.maps


interface MapsList
{
    fun GetMain(): List<Group>?
    fun GetSections(): Map<String, GroupSection>
    fun GetProducts(): Map<String, Product>


    interface Group
    {
        fun GetSections(): List<String>
        fun GetTitle(): String?
    }


    interface GroupSection
    {
        fun GetTitle(): String?
        fun GetProducts(): List<String>
    }


    interface Product
    {
        fun GetUrl(): String?
        fun GetTitle(): String?
    }
}