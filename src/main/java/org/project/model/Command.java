package org.project.model;

import jakarta.validation.constraints.Size;
import org.project.anotation.Iso8601Time;
import lombok.Data;


@Data
public class Command {
    @Size(max = 1000)
    private String description;
    private Priority priority;
    @Size(max = 100)
    private String author;
    @Iso8601Time
    private String time;
}