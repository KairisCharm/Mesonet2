package org.mesonet.dataprocessing

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.PerContext
import javax.inject.Inject


@PerContext
class ConnectivityStatusProviderImpl @Inject constructor(): ConnectivityManager.NetworkCallback(), ConnectivityStatusProvider {
    private val mConnectivityStatusSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()
    var mNetworkRequest: NetworkRequest = NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()

    private var mConnectivityManager: ConnectivityManager? = null

    override fun OnCreate(inContext: Context) {
        mConnectivityManager = inContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun OnResume(inContext: Context) {
        mConnectivityStatusSubject.onNext(mConnectivityManager?.activeNetwork != null && mConnectivityManager?.activeNetworkInfo?.isConnected == true)
        mConnectivityManager?.registerNetworkCallback(mNetworkRequest, this)
    }

    override fun OnPause() {
        mConnectivityManager?.unregisterNetworkCallback(this)
    }

    override fun OnDestroy()
    {
        mConnectivityStatusSubject.onComplete()
    }


    override fun ConnectivityStatusObservable(): Observable<Boolean> {
        return mConnectivityStatusSubject.distinctUntilChanged{ it -> it }
    }



    override fun onAvailable(inNetwork: Network) {
        super.onAvailable(inNetwork)

        mConnectivityStatusSubject.onNext(true)
    }


    override fun onUnavailable() {
        super.onUnavailable()

        mConnectivityStatusSubject.onNext(false)
    }

    override fun onLost(network: Network?) {
        super.onLost(network)
        mConnectivityStatusSubject.onNext(false)
    }
}