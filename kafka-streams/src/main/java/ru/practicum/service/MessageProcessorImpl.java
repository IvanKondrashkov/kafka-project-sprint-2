package ru.practicum.service;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.dto.Message;
import ru.practicum.dto.BlockedUsers;
import ru.practicum.dto.BlockedWords;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProcessorImpl implements MessageProcessor {
    private final KafkaTemplate<String, Message> messageKafkaTemplate;
    private final KafkaTemplate<String, BlockedUsers> blockedUsersKafkaTemplate;
    private final KafkaTemplate<String, BlockedWords> blockedWordsKafkaTemplate;

    @Override
    public void sendMessage(Message message) {
        log.info("send message={}", message);
        messageKafkaTemplate.send("messages", UUID.randomUUID().toString(), message);
    }

    @KafkaListener(topics = "messages", groupId = "message-processor-id", containerFactory = "messageKafkaListenerContainerFactory")
    public void listenMessage(Message message) {
        log.info("receive message={}", message);
    }

    @KafkaListener(topics = "filtered-messages", groupId = "message-processor-id", containerFactory = "messageKafkaListenerContainerFactory")
    public void listenFilteredMessage(Message message) {
        log.info("receive filtered message={}", message);
    }

    @Override
    public void blockedUsers(BlockedUsers blockedUsers) {
        log.info("send blockedUsers={}", blockedUsers);
        blockedUsersKafkaTemplate.send("blocked-users", blockedUsers.getRecipientId().toString(), blockedUsers);
    }

    @KafkaListener(topics = "blocked-users", groupId = "message-processor-id", containerFactory = "blockedUsersKafkaListenerContainerFactory")
    public void listenBlockedUsers(BlockedUsers blockedUsers) {
        log.info("receive blockedUsers={}", blockedUsers);
    }

    @Override
    public void blockedWords(BlockedWords blockedWords) {
        log.info("send blockedWords={}", blockedWords);
        blockedWordsKafkaTemplate.send("blocked-words", blockedWords.getRecipientId().toString(), blockedWords);
    }

    @KafkaListener(topics = "blocked-words", groupId = "message-processor-id", containerFactory = "blockedWordsKafkaListenerContainerFactory")
    public void listenBlockedWords(BlockedWords blockedWords) {
        log.info("receive blockedWords={}", blockedWords);
    }
}