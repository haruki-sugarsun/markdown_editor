package net.sickhack.markdowneditor.server;

import java.nio.file.*;
import java.nio.charset.Charset;
import java.io.IOException;

import com.linecorp.armeria.common.logging.LogLevel;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.decorator.LoggingDecorator;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpHeaders;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;

@LoggingDecorator(
        requestLogLevel = LogLevel.INFO,            // Log every request sent to this service at INFO level.
        successfulResponseLogLevel = LogLevel.INFO  // Log every response sent from this service at INFO level.
)
public class MarkdownRenderService {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private final String targetFilePath;

    MarkdownRenderService(String targetFilePath) {
        this.targetFilePath = targetFilePath;
    }

    @Get("/bypath/{body}")
    public String pathVar(@Param String body) {
        return renderCommonMark(body);
    }

    @Get("/byquery")
    public String query(@Param("t") String body) {
        return renderCommonMark(body);
    }

    @Post("/bypost")
    public String postBody(String body) {
        return renderCommonMark(body);
    }

    @Post("/bypost_and_save")
    public HttpResponse postBodyToSave(String body) throws IOException {
        if (!Files.isWritable(Paths.get(targetFilePath))) {
            logger.error("Given file needs to be writable to use preview & save.");
            return HttpResponse.of(HttpStatus.PRECONDITION_FAILED,
                                   MediaType.HTML_UTF_8,
                                   "Given file needs to be writable to use preview & save.");
        }

        Files.write(Paths.get(targetFilePath),
                    body.getBytes(Charset.forName("UTF-8")),
                    StandardOpenOption.WRITE);

        return HttpResponse.of(HttpStatus.OK,
                               MediaType.PLAIN_TEXT_UTF_8,
                               renderCommonMark(body));
    }

    private String renderCommonMark(String input) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(input);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String result = renderer.render(document);

        return result;
    }

}
