package ru.practicum.service;

import ru.practicum.dto.Message;
import ru.practicum.dto.BlockedUsers;
import ru.practicum.dto.BlockedWords;

public interface MessageProcessor {
    void sendMessage(Message message);
    void listenMessage(Message message);
    void listenFilteredMessage(Message message);
    void blockedUsers(BlockedUsers blockedUsers);
    void listenBlockedUsers(BlockedUsers blockedUsers);
    void blockedWords(BlockedWords blockedWords);
    void listenBlockedWords(BlockedWords blockedWords);
}