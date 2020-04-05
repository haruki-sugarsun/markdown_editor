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

@LoggingDecorator(
        requestLogLevel = LogLevel.INFO,            // Log every request sent to this service at INFO level.
        successfulResponseLogLevel = LogLevel.INFO  // Log every response sent from this service at INFO level.
)
public class EditorPageService {

    @Get("/index.html")
    public HttpResponse index() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/index.html"));

        return HttpResponse.of(HttpStatus.OK, MediaType.HTML_UTF_8,
                               bytes);
    }

}
