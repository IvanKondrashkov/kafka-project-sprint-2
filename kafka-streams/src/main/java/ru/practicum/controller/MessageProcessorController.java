package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.dto.Message;
import ru.practicum.dto.BlockedUsers;
import ru.practicum.dto.BlockedWords;
import ru.practicum.service.MessageProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageProcessorController {
    private final MessageProcessor messageProcessor;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void sendMessage(@RequestBody Message message) {
        log.info("send request /messages {}", message);
        messageProcessor.sendMessage(message);
    }

    @PostMapping("/block/users")
    @ResponseStatus(HttpStatus.OK)
    public void blockedUsers(@RequestBody BlockedUsers blockedUsers) {
        log.info("send request /messages/block/users {}", blockedUsers);
        messageProcessor.blockedUsers(blockedUsers);
    }

    @PostMapping("/block/words")
    @ResponseStatus(HttpStatus.OK)
    public void blockedWords(@RequestBody BlockedWords blockedWords) {
        log.info("send request /messages/block/words {}", blockedWords);
        messageProcessor.blockedWords(blockedWords);
    }
}