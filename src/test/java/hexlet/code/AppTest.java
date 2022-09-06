package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.query.QUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import io.javalin.Javalin;
import io.ebean.DB;
import io.ebean.Transaction;

import java.io.IOException;

class AppTest {

    private static Javalin app;
    private static String baseUrl;
    private static Transaction transaction;
    private static MockWebServer mockServer;

    @BeforeAll
    public static void beforeAll() {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
        mockServer = new MockWebServer();

    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    // При использовании БД запускать каждый тест в транзакции -
    // является хорошей практикой
    @BeforeEach
    void beforeEach() throws IOException {
        transaction = DB.beginTransaction();
        mockServer.shutdown();
    }

    @AfterEach
    void afterEach() {
        transaction.rollback();
    }

    @Test
    void testRoot() {
        HttpResponse<String> response = Unirest.get(baseUrl).asString();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testCreateUrl() {
        String actualName = "https://some-domain.org";

        HttpResponse<String> responsePost = Unirest
                .post(baseUrl + "/urls")
                .field("name", actualName)
                .asEmpty();
        assertThat(responsePost.getStatus()).isEqualTo(302);
        assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls");
        Url actualUrl = new QUrl()
                .name.equalTo(actualName)
                .findOne();
        assertThat(actualUrl).isNotNull();
        assertThat(actualUrl.getName()).isEqualTo(actualName);

    }

    @Test
    void testCreateDublUrl() {
        String actualName = "https://test.org";

        HttpResponse<String> responsePost = Unirest
                .post(baseUrl + "/urls")
                .field("name", actualName)
                .asEmpty();
        assertThat(responsePost.getStatus()).isEqualTo(302);
        assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/urls");
        int actualUrlCount = new QUrl()
                .name.equalTo(actualName)
                .findCount();
        assertThat(actualUrlCount).isEqualTo(1);
    }

    @Test
    void testGetUrls() {
        HttpResponse<String> response = Unirest
                .get(baseUrl + "/urls")
                .asString();
        String content = response.getBody();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(content).contains("https://test.org");
        assertThat(content).contains("https://test2.org");
        assertThat(content).doesNotContain("https://test11.org");
        HttpResponse<String> response2 = Unirest
                .get(baseUrl + "/urls?page=2")
                .asString();
        String content2 = response2.getBody();
        assertThat(response2.getStatus()).isEqualTo(200);
        assertThat(content2).contains("?page=1");
        assertThat(content2).contains("?page=2");
        assertThat(content2).contains("https://test11.org");
        assertThat(content2).doesNotContain("https://test.org");
    }

    @Test
    void testNewArticle() {
        HttpResponse<String> response = Unirest
                .get(baseUrl + "/urls/new")
                .asString();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testShowUrl() {
        HttpResponse<String> response = Unirest
                .get(baseUrl + "/urls/2")
                .asString();
        String content = response.getBody();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(content).contains("https://test2.org");
    }

    @Test
    void testCheckUrl200() throws IOException, InterruptedException {
        //Настраиваем ответ 400 на запрос https://test.org
        mockServer.start();
        mockServer.enqueue(new MockResponse().setResponseCode(200));
        //Делаем чек по mockUrl
        HttpResponse<String> response = Unirest
                .post(mockServer.url("/") + "urls/1/checks")
                .asString();
        String content = response.getBody();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testCheckUrl400() throws IOException, InterruptedException {
        //Настраиваем ответ 400 на запрос https://test.org
        MockWebServer mockServer = new MockWebServer();
        mockServer.start();
        mockServer.enqueue(new MockResponse().setResponseCode(400));
        //Делаем чек по mockUrl
        HttpResponse<String> response = Unirest
                .post(mockServer.url("/") + "urls/2/checks")
                .asString();
        String content = response.getBody();
        assertThat(response.getStatus()).isEqualTo(400);
    }
}