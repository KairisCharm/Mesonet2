package org.mesonet.app.widget

import org.mesonet.dataprocessing.LocationProvider
import javax.inject.Inject

class EmptyLocationProvider @Inject constructor(): LocationProvider
{
  override fun GetLocation(inLocationListener: LocationProvider.LocationListener) {
    inLocationListener.LocationUnavailable()
  }

}