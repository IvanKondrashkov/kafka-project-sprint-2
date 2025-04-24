package ru.practicum.config;

import java.util.Map;
import java.util.HashMap;
import ru.practicum.dto.Message;
import ru.practicum.dto.BlockedUsers;
import ru.practicum.dto.BlockedWords;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import ru.practicum.serialization.message.MessageDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.practicum.serialization.blocked_users.BlockedUsersDeserializer;
import ru.practicum.serialization.blocked_words.BlockedWordsDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value(value = "${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffset;
    @Value(value = "${spring.kafka.consumer.properties[session.timeout.ms]}")
    private Integer sessionTimeoutMs;
    @Value(value = "${spring.kafka.consumer.properties[heartbeat.interval.ms]}")
    private Integer heartbeatIntervalMs;

    @Bean
    public ConsumerFactory<String, Message> messageConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class.getName());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatIntervalMs);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffset);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Message> messageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messageConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BlockedUsers> blockedUsersConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BlockedUsersDeserializer.class.getName());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatIntervalMs);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffset);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BlockedUsers> blockedUsersKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BlockedUsers> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(blockedUsersConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BlockedWords> blockedWordsConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BlockedWordsDeserializer.class.getName());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatIntervalMs);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffset);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BlockedWords> blockedWordsKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BlockedWords> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(blockedWordsConsumerFactory());
        return factory;
    }
}