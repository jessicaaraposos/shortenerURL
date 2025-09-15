package br.com.jessicaraposo.shortener.app;

import br.com.jessicaraposo.shortener.entity.UrlMapping;
import br.com.jessicaraposo.shortener.infra.UrlMappingRepository;

import javax.ejb.Singleton;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.net.URI;

@Singleton // uma instância no app
public class ShortenerService {

    @Inject
    UrlMappingRepository repo;

    @Inject
    ShortCodeGenerator generator;

    // PROCESSO EXCLUSIVO: uma requisição por vez
    @Lock(LockType.WRITE)
    public UrlMapping createOrFail(@NotNull String originalUrl, String alias, String baseUrl) {
        if (alias != null && !alias.trim().isEmpty()) {
            // garante exclusividade do alias
            if (repo.findByAlias(alias).isPresent())
                throw new IllegalArgumentException("Alias já está em uso.");
        }

        String code = alias != null && !alias.trim().isEmpty()
                ? alias.trim()
                : nextUniqueCode();

        UrlMapping m = new UrlMapping();
        m.setOriginalUrl(originalUrl);
        if (alias != null && !alias.trim().isEmpty()) m.setAlias(alias.trim());
        m.setCode(code);
        m = repo.save(m);
        return m;
    }

    private String nextUniqueCode() {
        String c;
        do { c = generator.generate(7); } // tamanho do token
        while (repo.findByCodeOrAlias(c).isPresent());
        return c;
    }

    public URI resolveRedirect(String token) {
        return repo.findByCodeOrAlias(token)
                .map(m -> {
                    repo.incrementHits(m);
                    return URI.create(m.getOriginalUrl());
                })
                .orElse(null);
    }
}
