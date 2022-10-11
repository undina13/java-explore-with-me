package ru.practicum.main_server.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participations")

public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @ManyToOne
    private Event event;

    @ManyToOne
    private User requester;

    @Enumerated(EnumType.STRING)
    private StatusRequest status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participation that = (Participation) o;
        return id.equals(that.id) && created.equals(that.created) && event.equals(that.event) && requester
                .equals(that.requester) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, event, requester, status);
    }
}