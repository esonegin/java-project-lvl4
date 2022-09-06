package hexlet.code;

import controllers.RootController;
import controllers.UrlController;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static io.javalin.apibuilder.ApiBuilder.*;

public class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(getPort());
        //getApp();
       /* Javalin app = Javalin.create().start(7070);
        app.get("/", ctx -> ctx.result("Hello World"));
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.info("Hello World");*/
    }

    public static Javalin getApp() {
        // Создаём приложение
        Javalin app = Javalin.create(config -> {
            // Включаем логгирование
            config.enableDevLogging();
            // Подключаем настроенный шаблонизатор к фреймворку
            JavalinThymeleaf.configure(getTemplateEngine());
        });
        addRoutes(app);
        // Обработчик before запускается перед каждым запросом
        // Устанавливаем атрибут ctx для запросов
        app.before(ctx -> {
            ctx.attribute("ctx", ctx);
        });

        return app;
    }

    private static TemplateEngine getTemplateEngine() {
        // Создаём инстанс движка шаблонизатора
        TemplateEngine templateEngine = new TemplateEngine();
        // Добавляем к нему диалекты
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.addDialect(new Java8TimeDialect());
        // Настраиваем преобразователь шаблонов, так, чтобы обрабатывались
        // шаблоны в директории /templates/
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        // Добавляем преобразователь шаблонов к движку шаблонизатора
        templateEngine.addTemplateResolver(templateResolver);

        return templateEngine;
    }

    private static void addRoutes(Javalin app) {
        // Для GET-запроса на маршрут / будет выполняться
        // обработчик welcome в контроллере RootController
        app.get("/", RootController.welcome);
        app.routes(() -> {
            path("urls", () -> {
                //GET /urls — список всех статей
                get(UrlController.listUrls);
                post(UrlController.createUrl);
                //GET /articles/new — вывод формы создания новой статьи
                get("new", UrlController.newUrl);
                get("{id}", UrlController.showUrl);
                post("{id}/checks", UrlController.urlChecks);
            });
        });
    }
}