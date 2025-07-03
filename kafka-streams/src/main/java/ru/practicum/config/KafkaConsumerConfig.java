package ru.practicum.config;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import ru.practicum.dto.Message;
import ru.practicum.dto.BlockedUsers;
import ru.practicum.dto.BlockedWords;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
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
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final SslBundles sslBundles;
    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, Message> messageConsumerFactory() {
        Map<String, Object> config = kafkaProperties.buildConsumerProperties(sslBundles);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class.getName());
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
        Map<String, Object> config = kafkaProperties.buildConsumerProperties(sslBundles);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BlockedUsersDeserializer.class.getName());
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
        Map<String, Object> config = kafkaProperties.buildConsumerProperties(sslBundles);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BlockedWordsDeserializer.class.getName());
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BlockedWords> blockedWordsKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BlockedWords> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(blockedWordsConsumerFactory());
        return factory;
    }
}