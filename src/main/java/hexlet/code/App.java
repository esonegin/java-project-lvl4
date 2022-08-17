package hexlet.code;

import io.javalin.Javalin;
import org.eclipse.jetty.server.Server;

public class App {

    public static void main(String[] args) {
        getApp();
    }

    public class HelloWorld {
        public static void main(String[] args) {
            Javalin app = Javalin.create().start(7070);
            app.get("/", ctx -> ctx.result("Hello World"));
        }
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create();
        app.create(config -> {
            config.enableDevLogging(); // enable extensive development logging for http and websocket
        });
        return app;
    }
}
