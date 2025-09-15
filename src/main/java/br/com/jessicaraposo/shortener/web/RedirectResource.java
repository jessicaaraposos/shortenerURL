package br.com.jessicaraposo.shortener.web;

import br.com.jessicaraposo.shortener.app.ShortenerService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;

@Path("/r")
public class RedirectResource {

    @Inject
    ShortenerService service;

    @GET
    @Path("{token}")
    public Response go(@PathParam("token") String token) {
        URI target = service.resolveRedirect(token);
        if (target == null) throw new NotFoundException();
        return Response.seeOther(target).build(); // 303 (ou use .status(301) se preferir)
    }
}
