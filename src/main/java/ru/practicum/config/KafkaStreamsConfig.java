package ru.practicum.config;

import java.util.*;
import ru.practicum.dto.User;
import ru.practicum.dto.Message;
import ru.practicum.dto.BlockedUsers;
import ru.practicum.dto.BlockedWords;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import ru.practicum.serialization.blocked_users.BlockedUsersSerdes;
import ru.practicum.serialization.blocked_words.BlockedWordsSerdes;
import ru.practicum.serialization.message.MessageSerdes;

@EnableKafka
@EnableKafkaStreams
@Configuration
public class KafkaStreamsConfig {
    @Value(value = "${spring.kafka.streams.application-id}")
    private String applicationId;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value(value = "${spring.kafka.streams.properties[request.timeout.ms]}")
    private Integer requestTimeoutMs;


    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, MessageSerdes.class.getName());
        config.put(StreamsConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        return new KafkaStreamsConfiguration(config);
    }

    @Bean
    public BlockedUsersSerdes blockedUsersSerdes() {
        return new BlockedUsersSerdes();
    }

    @Bean
    public BlockedWordsSerdes blockedWordsSerdes() {
        return new BlockedWordsSerdes();
    }

    @Bean
    public MessageSerdes messageSerdes() {
        return new MessageSerdes();
    }

    @Bean
    public KStream<String, Message> kStream(StreamsBuilder builder) {
        KTable<String, BlockedUsers> blockedUsers = builder.stream("blocked-users", Consumed.with(Serdes.String(), blockedUsersSerdes()))
                .toTable(
                        Materialized.<String, BlockedUsers>as(Stores.persistentKeyValueStore("blocked-users-store"))
                                .withKeySerde(Serdes.String())
                                .withValueSerde(blockedUsersSerdes())
                );
        KTable<String, BlockedWords> blockedWords = builder.stream("blocked-words", Consumed.with(Serdes.String(), blockedWordsSerdes()))
                .toTable(
                        Materialized.<String, BlockedWords>as(Stores.persistentKeyValueStore("blocked-words-store"))
                                .withKeySerde(Serdes.String())
                                .withValueSerde(blockedWordsSerdes())
                );

        KStream<String, Message> messages = builder.stream("messages", Consumed.with(Serdes.String(), messageSerdes()));
        messages.map((recipient, message) -> new KeyValue<>(message.getRecipient().getId().toString(), message))
                .leftJoin(blockedUsers, (message, blockedList) -> {
                    if (Objects.nonNull(blockedList)) {
                        List<String> blockedIds = blockedList.getUsers().stream()
                                .map(User::getId)
                                .map(UUID::toString)
                                .toList();
                        if (blockedIds.contains(message.getSender().getId().toString())) {
                            return null;
                        }
                        return message;
                    }
                    return message;
                })
                .filter(((key, value) -> value != null))
                .leftJoin(blockedWords, (message, blockedList) -> {
                    if (Objects.nonNull(blockedList)) {
                        for (String word : blockedList.getWords()) {
                            if (message.getMessage().contains(word)) {
                                var maskedMessage = message.getMessage().replaceAll(word, "*".repeat(word.length()));
                                message.setMessage(maskedMessage);
                            }
                        }
                    }
                    return message;
                })
                .to("filtered-messages");
        return messages;
    }
}