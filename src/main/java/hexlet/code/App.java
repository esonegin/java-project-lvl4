package hexlet.code;

import io.javalin.Javalin;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    public static void main(String[] args) {
        getApp();
        Javalin app = Javalin.create().start(7070);
        app.get("/", ctx -> ctx.result("Hello World"));
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.info("Hello World");
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create();
        app.start();
        app.create(config -> {
            config.enableDevLogging(); // enable extensive development logging for http and websocket
        });
        return app;
    }
}
