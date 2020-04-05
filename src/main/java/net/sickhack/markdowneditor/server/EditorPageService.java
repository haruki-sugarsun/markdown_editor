package net.sickhack.markdowneditor.server;

import com.linecorp.armeria.common.logging.LogLevel;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.decorator.LoggingDecorator;

import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpHeaders;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;


import java.io.BufferedReader;
import java.io.Reader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

// TODO: Properly read the resources from classpath.
@LoggingDecorator(
        requestLogLevel = LogLevel.INFO,            // Log every request sent to this service at INFO level.
        successfulResponseLogLevel = LogLevel.INFO  // Log every response sent from this service at INFO level.
)
public class EditorPageService {

    private final String targetFilePath;

    EditorPageService(String targetFilePath) {
        this.targetFilePath = targetFilePath;
    }

    @Get("/index.html")
    public HttpResponse index() throws IOException {
        String baseIndexHtml = readFile("src/main/resources/index.html");
        String exampleMd; // TODO: variable name is not appropriate. Have better abstraction and configuration.
        if (targetFilePath == null) {
            exampleMd =  readFile("src/main/resources/example.md");
        } else {
            exampleMd =  readFile(targetFilePath);
        }

        String result = baseIndexHtml.replace("___EXAMPLE_MD___", exampleMd);

        return HttpResponse.of(HttpStatus.OK, MediaType.HTML_UTF_8,
                               result);
    }

    private String readFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        // TODO: Consider charset. maybe force UTF-8.
        String strContent = new String(bytes);
        return strContent;
    }

}
