package cz.jopenspace.hazelcast.webcharts;

import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TradingChartServer {

    private final List<WebSocketChannel> sessions = new CopyOnWriteArrayList<WebSocketChannel>();
    JsonFactory factory = new JsonFactory();
    private final ObjectMapper mapper = new ObjectMapper(factory);

    public TradingChartServer() {
        new UndertowServer(sessions).start();
    }

    public void sendTrade(String symbol, long price) {
        for (WebSocketChannel channel : sessions) {
            try {
                WebSockets.sendText(mapper.writeValueAsString(new Trade(symbol, price)), channel, new io.undertow.websockets.core.WebSocketCallback<Void>() {
                    public void complete(WebSocketChannel channel, Void context) {
                    }

                    public void onError(WebSocketChannel channel, Void context, Throwable throwable) {
                        sessions.remove(channel);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
