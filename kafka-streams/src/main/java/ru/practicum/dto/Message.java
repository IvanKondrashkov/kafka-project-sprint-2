package ru.practicum.dto;

import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private UUID id;
    private User sender;
    private User recipient;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDt;
}