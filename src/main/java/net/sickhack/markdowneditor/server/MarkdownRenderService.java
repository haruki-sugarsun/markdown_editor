package net.sickhack.markdowneditor.server;

import com.linecorp.armeria.common.logging.LogLevel;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.decorator.LoggingDecorator;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;



@LoggingDecorator(
        requestLogLevel = LogLevel.INFO,            // Log every request sent to this service at INFO level.
        successfulResponseLogLevel = LogLevel.INFO  // Log every response sent from this service at INFO level.
)
public class MarkdownRenderService {

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

    private String renderCommonMark(String input) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(input);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String result = renderer.render(document);

        return result;
    }

}
