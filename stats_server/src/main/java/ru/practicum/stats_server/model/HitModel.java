package ru.practicum.stats_server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hits")
public class HitModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app", length = 50)
    private String app;

    @Column(name = "uri", length = 254)
    private String uri;

    @Column(name = "ip", length = 50)
    private String ip;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
