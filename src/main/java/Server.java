import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;


public class Server {
    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(
                new InetSocketAddress("127.0.0.1", 12345)
        );
        while (true) {
            try (SocketChannel socketChannel = serverChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                while (socketChannel.isConnected()) {
                    int byteCount = socketChannel.read(inputBuffer);
                    if (byteCount == -1) {
                        break;
                    }
                    final String msg = new String(
                            inputBuffer.array(), 0, byteCount, StandardCharsets.UTF_8
                    );
                    inputBuffer.clear();
                    socketChannel.write(
                            ByteBuffer.wrap(
                                    ("Без пробелов: " + msg.replaceAll("\\s+", "")
                                    ).getBytes(StandardCharsets.UTF_8)
                            )
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                serverChannel.close();
            }
        }
    }
}