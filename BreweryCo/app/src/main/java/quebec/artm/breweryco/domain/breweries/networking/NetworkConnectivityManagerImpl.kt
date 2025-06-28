package quebec.artm.breweryco.domain.breweries.networking

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Using the native component such as Broadcast Receiver, this will allows to observe network status change.
 * Every time the network switch from Wifi to mobile to offline or vice versa, the observable will call onNext()
 * that will trigger a status change. So the Observer will react to the status and behave in consequence,
 * such as re-fetching the remote data then update the local database and reloading the ui with the fresh new dataset.
 */
interface NetworkConnectivityManager {
    fun setConnectionType(connectionType: ConnectionType)

    fun getConnectionType(): ConnectionType

    fun getConnectionTypeStateFlow(): StateFlow<ConnectionType>

    enum class ConnectionType {
        TYPE_WIFI,
        TYPE_MOBILE,
        TYPE_ONLINE,
        TYPE_NO_INTERNET,
    }
}


class NetworkConnectivityManagerImpl(connectivityManager: ConnectivityManager) :
    NetworkConnectivityManager {
    private val _connectionTypeStateFlow =
        MutableStateFlow(NetworkConnectivityManager.ConnectionType.TYPE_ONLINE)

    init {

        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        connectivityManager.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    setConnectionType(NetworkConnectivityManager.ConnectionType.TYPE_ONLINE)
                }

                override fun onLost(network: Network) {
                    setConnectionType(NetworkConnectivityManager.ConnectionType.TYPE_NO_INTERNET)
                }

                override fun onUnavailable() {
                    setConnectionType(NetworkConnectivityManager.ConnectionType.TYPE_NO_INTERNET)
                }
            },
        )
    }

    override fun setConnectionType(connectionType: NetworkConnectivityManager.ConnectionType) {
        _connectionTypeStateFlow.value = connectionType
    }

    override fun getConnectionType(): NetworkConnectivityManager.ConnectionType {
        return _connectionTypeStateFlow.value
    }

    override fun getConnectionTypeStateFlow(): StateFlow<NetworkConnectivityManager.ConnectionType> {
        return _connectionTypeStateFlow
    }
}
