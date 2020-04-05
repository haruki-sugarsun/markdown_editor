package net.sickhack.markdowneditor.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.getopt.Getopt;


import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.docs.DocService;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        // Options
        Getopt options = new Getopt("testprog", args, "ab:c::df:");

        String targetFilePath = null;
        boolean isDebug = false;

        int c;
        while ( (c = options.getopt()) != -1) {
            switch (c) {                
            case 'd':
                isDebug = true;
                break;
            case 'n':
                targetFilePath = options.getOptarg();
                break;
            default:
                System.err.println("引数指定の誤り：未知の引数が指定");
            }
        }

        final Server server = newServer(8080);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop().join();
            logger.info("Server has been stopped.");
        }));

        server.start().join();

        logger.info("Server has been started. Serving DocService at http://127.0.0.1:{}/docs",
                    server.activeLocalPort());
    }

    /**
     * Returns a new {@link Server} instance configured with annotated HTTP services.
     *
     * @param port the port that the server is to be bound to
     */
    static Server newServer(int port) {
        final ServerBuilder sb = Server.builder();
        return sb.http(port)
                 .annotatedService("/pathPattern", new PathPatternService())
                 .annotatedService("/injection", new InjectionService())
                 .annotatedService("/messageConverter", new MessageConverterService())
                 .annotatedService("/exception", new ExceptionHandlerService())
                 .annotatedService("/render", new MarkdownRenderService())
                .annotatedService("/editor", new EditorPageService())
                 .serviceUnder("/docs", new DocService())
                 .build();
    }
}
