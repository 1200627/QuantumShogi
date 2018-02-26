package quantumshogi

import javafx.fxml.FXML
import javafx.scene.control.TextField
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

class ConnectController {
    @FXML
    private lateinit var connectIpField: TextField
    @FXML
    private lateinit var connectPortField: TextField
    @FXML
    private lateinit var waitPortField: TextField

    lateinit var controller: Controller

    @FXML
    fun onConnect() {
        val channel = SocketChannel.open(InetSocketAddress(connectIpField.text, connectPortField.text.toInt()))
    }

    @FXML
    fun onWait() {
        val socketChannel = ServerSocketChannel.open()
        socketChannel.socket().bind(InetSocketAddress(waitPortField.text.toInt()))
        val channel = socketChannel.accept()

        controller.println("${channel.socket().remoteSocketAddress}が接続しました。")
    }
}
