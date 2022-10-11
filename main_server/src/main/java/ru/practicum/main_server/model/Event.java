package ru.practicum.main_server.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation", nullable = false)
    private String annotation;

    @ManyToOne
    private Category category;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne
    private User initiator;

    @ManyToOne()
    private Location location;

    @Column(name = "paid", nullable = false)
    private boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "title", length = 254, nullable = false)
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return paid == event.paid && requestModeration == event.requestModeration && id
                .equals(event.id) && Objects.equals(annotation, event.annotation) && Objects
                .equals(category, event.category) && Objects
                .equals(createdOn, event.createdOn) && Objects.equals(description, event.description) && eventDate
                .equals(event.eventDate) && initiator.equals(event.initiator) && Objects
                .equals(location, event.location) && Objects
                .equals(participantLimit, event.participantLimit) && Objects
                .equals(publishedOn, event.publishedOn) && state == event.state && Objects
                .equals(title, event.title);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(id, annotation, category, createdOn, description, eventDate, initiator, location, paid, participantLimit, publishedOn, requestModeration, state, title);
    }
}
