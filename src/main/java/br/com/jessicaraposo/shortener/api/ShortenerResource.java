package br.com.jessicaraposo.shortener.api;

import  br.com.jessicaraposo.shortener.app.ShortenerService;
import  br.com.jessicaraposo.shortener.entity.UrlMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Path("/shorten")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShortenerResource {

    @Inject
    ShortenerService service;

    @Context
    HttpServletRequest request;

    public static class ShortenRequest {
        public String originalUrl;
        public String alias; // opcional
    }

    @POST
    public Response create(ShortenRequest body, @Context UriInfo uriInfo) {
        if (body == null || body.originalUrl == null || body.originalUrl.trim().isEmpty())
            throw new BadRequestException("originalUrl é obrigatório");

        String baseUrl = uriInfo.getBaseUri().resolve("../").normalize().toString(); // ex: http://host:8080/url-shortener/
        UrlMapping m = service.createOrFail(body.originalUrl.trim(), body.alias, baseUrl);

        String shortUrl = baseUrl + "api/r/" + m.getCode();
        Map<String, Object> resp = new HashMap<>();
        resp.put("shortUrl", shortUrl);
        resp.put("code", m.getCode());
        return Response.created(URI.create(shortUrl)).entity(resp).build();
    }
}
