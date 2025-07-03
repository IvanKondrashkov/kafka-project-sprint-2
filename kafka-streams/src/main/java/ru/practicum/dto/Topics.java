package ru.practicum.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Topics {
    public static final String MESSAGES_TOPIC = "messages";
    public static final String BLOCKED_USERS_TOPIC = "blocked-users";
    public static final String BLOCKED_WORDS_TOPIC = "blocked-words";
    public static final String FILTERED_MESSAGES_TOPIC = "filtered-messages";
}