package cz.jopenspace.hazelcast.webcharts;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.websockets.core.WebSocketChannel;

import java.util.List;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;
import static io.undertow.Handlers.websocket;


public class UndertowServer {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final String WEBSOCKET_ENDPOINT_PATH = "websocket";
    private final List<WebSocketChannel> sessions;

    public UndertowServer(List<WebSocketChannel> sessions) {
        this.sessions = sessions;
    }

    public void start() {
        ResourceManager resourceManager = new ClassPathResourceManager( //FileResourceManager would be more effective (and secure?), but this allows to server static content from JAR
                UndertowServer.class.getClassLoader(), "public");

        Undertow.builder()
                .addHttpListener(PORT, HOST)
                .setHandler(
                        path(resource(resourceManager)) //serve static files by default
                                .addPrefixPath(WEBSOCKET_ENDPOINT_PATH, websocket(new ConnectionCallback(sessions)))
                        )
                .build()
                .start();
    }
}
