package br.com.jessicaraposo.shortener.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "url_mapping", uniqueConstraints = {
        @UniqueConstraint(name = "uk_code", columnNames = "code"),
        @UniqueConstraint(name = "uk_alias", columnNames = "alias")
})
public class UrlMapping implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false, length = 32)
    private String code;      // gerado

    @Column(length = 64)
    private String alias;     // opcional

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt = new Date();

    @Column(nullable = false)
    private long hits = 0L;

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public long getHits() { return hits; }
    public void setHits(long hits) { this.hits = hits; }
}
