package ru.practicum.config;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import ru.practicum.dto.Message;
import ru.practicum.dto.BlockedUsers;
import ru.practicum.dto.BlockedWords;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.practicum.serialization.blocked_users.BlockedUsersSerializer;
import ru.practicum.serialization.blocked_words.BlockedWordsSerializer;
import ru.practicum.serialization.message.MessageSerializer;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    private final SslBundles sslBundles;
    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, Message> messageProducerFactory() {
        Map<String, Object> config = kafkaProperties.buildProducerProperties(sslBundles);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Message> messageKafkaTemplate() {
        return new KafkaTemplate<>(messageProducerFactory());
    }

    @Bean
    public ProducerFactory<String, BlockedUsers> blockedUsersProducerFactory() {
        Map<String, Object> config = kafkaProperties.buildProducerProperties(sslBundles);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BlockedUsersSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, BlockedUsers> blockedUsersKafkaTemplate() {
        return new KafkaTemplate<>(blockedUsersProducerFactory());
    }

    @Bean
    public ProducerFactory<String, BlockedWords> blockedWordsProducerFactory() {
        Map<String, Object> config = kafkaProperties.buildProducerProperties(sslBundles);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BlockedWordsSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, BlockedWords> blockedWordsKafkaTemplate() {
        return new KafkaTemplate<>(blockedWordsProducerFactory());
    }
}