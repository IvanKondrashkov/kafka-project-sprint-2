package ru.practicum.dto;

import lombok.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockedWords {
    private UUID recipientId;
    private List<String> words;
}