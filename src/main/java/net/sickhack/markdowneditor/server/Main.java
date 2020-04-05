package net.sickhack.markdowneditor.server;

import java.nio.file.*;

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
        Getopt options = new Getopt("testprog", args, "f:");

        String targetFilePath = null;
        boolean isDebug = false;

        int c;
        while ( (c = options.getopt()) != -1) {
            switch (c) {
            case 'f':
                targetFilePath = options.getOptarg();
                logger.info("Target file is given: {}", targetFilePath);
                break;
            default:
                // TODO: Improve help message.
                System.err.println("Usage: -f <file path>");
                System.exit(1);
            }
        }

        // Validate the options.
        if (targetFilePath != null) {
            if (!Files.isReadable(Paths.get(targetFilePath))) {
                logger.error("Given file needs to be readble.");
                System.exit(1);
            }
            if (!Files.isWritable(Paths.get(targetFilePath))) {
                logger.warn("Given file should be writable.");
            }
        }

        final Server server = newServer(8080, targetFilePath);

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
    static Server newServer(int port, String targetFilePath) {
        final ServerBuilder sb = Server.builder();
        // TODO: Remove Unused files.
        // TODO: Avoid passing the `targetFilePath` everywhere. We may use DI or centralized configuration.
        return sb.http(port)
                 .annotatedService("/render", new MarkdownRenderService(targetFilePath))
                 .annotatedService("/editor", new EditorPageService(targetFilePath))
                 .build();
    }
}
