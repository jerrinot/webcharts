package cz.jopenspace.hazelcast.webcharts;

import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import org.xnio.ChannelListener;

import java.nio.channels.Channel;
import java.util.List;

class ConnectionCallback implements WebSocketConnectionCallback {
    private final List<WebSocketChannel> sessions;

    public ConnectionCallback(List<WebSocketChannel> sessions) {
        this.sessions = sessions;
    }

    @Override
    public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
        sessions.add(channel);
        channel.getCloseSetter().set(new ChannelListener<Channel>() {
            @Override
            public void handleEvent(Channel channel) {
                sessions.remove(channel);
            }
        });
        channel.resumeReceives();
    }
}