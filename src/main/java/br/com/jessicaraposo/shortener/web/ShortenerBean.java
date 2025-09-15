package br.com.jessicaraposo.shortener.web;

import br.com.jessicaraposo.shortener.app.ShortenerService;
import br.com.jessicaraposo.shortener.entity.UrlMapping;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.UriBuilder;
import java.io.Serializable;

@Named
@ViewScoped
public class ShortenerBean implements Serializable {

    private String originalUrl;
    private String alias;
    private String shortUrl;

    @Inject
    private ShortenerService service;

    public void shorten() {
        try {
            String base = FacesContext.getCurrentInstance()
                    .getExternalContext().getRequestContextPath(); // ex: /url-shortener
            String host = FacesContext.getCurrentInstance()
                    .getExternalContext().getRequestServerName();
            int port = FacesContext.getCurrentInstance()
                    .getExternalContext().getRequestServerPort();
            String scheme = FacesContext.getCurrentInstance()
                    .getExternalContext().getRequestScheme();

            String baseUrl = UriBuilder.fromPath(base).scheme(scheme).host(host).port(port)
                    .replacePath(base + "/").build().toString(); // http://host:8080/url-shortener/

            UrlMapping m = service.createOrFail(originalUrl, alias, baseUrl);
            this.shortUrl = baseUrl + "r/" + m.getCode();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "URL gerada com sucesso!", null));
        } catch (IllegalArgumentException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao encurtar URL.", null));
        }
    }

    // getters/setters
    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public String getShortUrl() { return shortUrl; }
}
