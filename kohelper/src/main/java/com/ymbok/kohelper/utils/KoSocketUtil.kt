package com.ymbok.kohelper.utils

import okhttp3.*
import okio.ByteString


class KoWebSocketUtil : WebSocketListener {

    private val TAG = "KoWebSocketUtil "

    private var wsUrl: String? = null

    private var webSocket: WebSocket? = null

    private var status: ConnectStatus? = null

    private val client = OkHttpClient.Builder().build()

    constructor(wsUrl: String) {
        this.wsUrl = wsUrl
    }

    fun getStatus(): ConnectStatus? {
        return status
    }

    fun connect() {
        val request: Request = Request.Builder().url(wsUrl!!).build()
        webSocket = client.newWebSocket(request, this)
        status = ConnectStatus.Connecting
    }

    fun reConnect() {
        if (webSocket != null) {
            webSocket = client.newWebSocket(webSocket!!.request(), this)
            status = ConnectStatus.Connecting
        }
    }

    fun send(text: String) {
        if (webSocket != null) {
            logI(TAG,"sendMessage:$text")
            webSocket!!.send(text)
        }
    }

    fun cancel() {
        webSocket?.cancel()
    }

    fun close() {
        webSocket?.close(1000, null)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        logI(TAG,"onOpen")
        status = ConnectStatus.Open
        socketCallBack?.onOpen()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        //logI(TAG,"onMessage: $text")
        socketCallBack?.onMessage(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        status = ConnectStatus.Closing
        logI(TAG,"onClosing")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        logI(TAG,"onClosed")
        status = ConnectStatus.Closed
        socketCallBack?.onClose()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable,response: Response?) {
        super.onFailure(webSocket, t, response)
        logI(TAG,"onFailure: $t")
        t.printStackTrace()
        status = ConnectStatus.Canceled
        socketCallBack?.onConnectError(t)
    }


    private var socketCallBack: WebSocketCallBack? = null

    fun setSocketCallBack(callBack: WebSocketCallBack?) {
        socketCallBack = callBack
    }

    fun removeSocketCallBack() {
        socketCallBack = null
    }

}
open class WebSocketCallBack() {
    open fun onConnectError(t: Throwable){

    }
    open fun onOpen(){}
    open fun onClose(){}
    open fun onMessage(text: String){}
}
enum class ConnectStatus {
    Connecting,  // the initial state of each web socket.
    Open,  // the web socket has been accepted by the remote peer
    Closing,  // one of the peers on the web socket has initiated a graceful shutdown
    Closed,  //  the web socket has transmitted all of its messages and has received all messages from the peer
    Canceled // the web socket connection failed
}