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



    public static Handler urlChecks = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        Url url = new QUrl()
                .id.equalTo(id)
                .findOne();
        ctx.attribute("url", url);
        ctx.render("urls/show.html");
    };
}
