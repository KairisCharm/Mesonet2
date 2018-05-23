package org.mesonet.models.advisories

import java.util.ArrayList

interface AdvisoryCreator
{
    fun ParseAdvisoryFile(inAdvisoryFile: String): ArrayList<Advisory>
}