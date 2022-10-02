package ru.practicum.main_server.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "lat", nullable = false)
    private Float lat;

    @Column(name = "lon", nullable = false)
    private Float lon;

    @Column(name = "radius")
    private Float radius;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return id.equals(location.id) && Objects.equals(name, location.name) && lat.equals(location.lat) && lon
                .equals(location.lon) && Objects.equals(radius, location.radius);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lat, lon, radius);
    }
}
