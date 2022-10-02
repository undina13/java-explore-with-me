package ru.practicum.main_server.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;

    private List<EventShortDto> events;

    @NotNull
    private String title;

    private boolean pinned;
}
