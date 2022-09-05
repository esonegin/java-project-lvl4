package controllers;


import hexlet.code.model.Url;
import hexlet.code.model.query.QUrl;
import io.ebean.PagedList;
import io.javalin.http.Handler;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public final class UrlController {

    public static Handler listUrls = ctx -> {
        int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
        int rowsPerPage = 10;
        int offset = (page - 1) * rowsPerPage;
        PagedList<Url> pagedUrls = new QUrl()
                .setFirstRow(offset)
                .setMaxRows(rowsPerPage)
                .orderBy()
                .id.asc()
                .findPagedList();
        List<Url> urls = pagedUrls.getList();
        ctx.attribute("urls", urls);
        ctx.attribute("page", page);
        ctx.render("urls/index.html");
    };

    public static Handler newUrl = ctx -> {
        List<Url> urls = new QUrl().findList();
        ctx.attribute("urls", urls);
        ctx.render("urls/index.html");
    };

    public static Handler showUrl = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        Url url = new QUrl()
                .id.equalTo(id)
                .findOne();
        ctx.attribute("url", url);
        ctx.render("urls/show.html");
    };

    public static Handler createUrl = ctx -> {
        String title = ctx.formParam("name");
        URL aURL = new URL(title);
        String result = aURL.getProtocol() + "://" + aURL.getHost();
        if (aURL.getPort() != -1) {
            result += ":" + aURL.getPort();
        }
        Url url = new Url(result);
        List<Url> urls = new QUrl().findList();
        ArrayList names = new ArrayList();
        for(Url u : urls){
            names.add(u.getName());
        }
        if(names.contains(url.getName())){
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.redirect("/urls");
        }
        else {
            url.save();
            ctx.sessionAttribute("flash", "Url успешно добавлен");
            ctx.redirect("/urls");
        }
    };

    /*public static Handler showArticle = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        Article article = new QArticle()
                .id.equalTo(id)
                .findOne();
        List<Category> categories = new QCategory()
                .id.equalTo(id)
                .findList();
        ctx.attribute("article", article);
        ctx.attribute("categories", categories);
        ctx.render("articles/show.html");
    };

    public static Handler editArticle = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        Article article = new QArticle()
                .id.equalTo(id)
                .findOne();
        List<Category> categories = new QCategory()
                .findList();
        ctx.attribute("article", article);
        ctx.attribute("categories", categories);
        ctx.render("articles/edit.html");
    };

    public static Handler updateArticle = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        new QArticle()
                .id.equalTo(id)
                .asUpdate()
                .set("title", ctx.formParam("title"))
                .set("body", ctx.formParam("body"))
                .update();
        Article article = new QArticle()
                .id.equalTo(id)
                .findOne();
        ctx.attribute("article", article);
        ctx.sessionAttribute("flash", "Статья успешно изменена");
        ctx.redirect("/urls");
    };

    public static Handler deleteArticle = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        Article article = new QArticle()
                .id.equalTo(id)
                .findOne();
        ctx.sessionAttribute("flash", "Статья успешно удалена");
        ctx.attribute("article", article);
        ctx.attribute("title", article.getTitle());
        ctx.render("articles/delete.html");
    };

    public static Handler destroyArticle = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        new QArticle()
                .id.equalTo(id)
                .delete();
       *//* Article article = new QArticle()
                .id.equalTo(id)
                .findOne();
        ctx.attribute("article", article);*//*
        ctx.attribute("flash", "Article update complete");
        ctx.redirect("/urls");
    };*/
}
