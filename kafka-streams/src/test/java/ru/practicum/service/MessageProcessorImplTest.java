package ru.practicum.service;

import java.util.List;
import java.util.UUID;
import java.time.Duration;
import java.time.LocalDateTime;
import ru.practicum.dto.BlockedUsers;
import ru.practicum.dto.BlockedWords;
import ru.practicum.dto.User;
import ru.practicum.dto.Message;
import ru.practicum.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.junit.jupiter.Container;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.KafkaContainer;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Testcontainers
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageProcessorImplTest {
    @Autowired
    private MessageProcessor messageProcessor;
    private Message message;
    private User sender;
    private User recipient;

    @Container
    @ServiceConnection
    public static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.4.0")
    ).withKraft();

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.streams.application-id", () -> "message-processor");
        registry.add("spring.kafka.consumer.group-id", () -> "message-processor-id");
    }


    @BeforeEach
    void setUp() {
        sender = User.builder()
                .id(UUID.randomUUID())
                .build();
        recipient = User.builder()
                .id(UUID.randomUUID())
                .build();
        message = Message.builder()
                .id(UUID.randomUUID())
                .sender(sender)
                .recipient(recipient)
                .message("Рекламная акция, поспеши принять участие!")
                .createDt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void tearDown() {
        sender = null;
        recipient = null;
        message = null;
    }

    @Test
    void sendMessage(CapturedOutput output) {
        messageProcessor.sendMessage(message);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .untilAsserted(() -> {
                    assertTrue(output.getOut().contains(message.toString()));
                });
    }

    @Test
    void blockedUsers(CapturedOutput output) {
        BlockedUsers blockedUsers = BlockedUsers.builder()
                .recipientId(message.getRecipient().getId())
                .users(List.of(message.getSender()))
                .build();

        messageProcessor.blockedUsers(blockedUsers);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .untilAsserted(() -> {
                    assertTrue(output.getOut().contains(blockedUsers.toString()));
                });
    }

    @Test
    void blockedWords(CapturedOutput output) {
        BlockedWords blockedWords = BlockedWords.builder()
                .recipientId(message.getRecipient().getId())
                .words(List.of("Спам, Мат"))
                .build();

        messageProcessor.blockedWords(blockedWords);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .untilAsserted(() -> {
                    assertTrue(output.getOut().contains(blockedWords.toString()));
                });
    }

    @Test
    void filteredMessage(CapturedOutput output) {
        BlockedUsers blockedUsers = BlockedUsers.builder()
                .recipientId(message.getRecipient().getId())
                .users(List.of(message.getSender()))
                .build();

        messageProcessor.blockedUsers(blockedUsers);
        messageProcessor.sendMessage(message);

        final String filteredMessage = String.format("receive filtered message=%s", message);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .untilAsserted(() -> {
                    assertFalse(output.getOut().contains(filteredMessage));
                });
    }
}