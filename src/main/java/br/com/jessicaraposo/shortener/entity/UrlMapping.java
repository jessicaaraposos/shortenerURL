package main.java.br.com.jessicaraposo.shortener.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "url_mapping", uniqueConstraints = {
        @UniqueConstraint(name = "uk_code", columnNames = "code"),
        @UniqueConstraint(name = "uk_alias", columnNames = "alias")
})
public class UrlMapping {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=2048)
    private String originalUrl;

    @Column(nullable=false, length=32)
    private String code;          // curto (gerado)

    @Column(length=64)
    private String alias;         // opcional (personalizado)

    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable=false)
    private long hits = 0L;

    // getters & setters
}

