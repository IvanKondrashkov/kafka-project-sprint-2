package ru.practicum.config;

import java.util.Map;
import java.util.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.apache.kafka.clients.admin.AdminClientConfig;

@Configuration
public class KafkaTopicConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(config);
    }

    @Bean
    public NewTopic messages() {
        return TopicBuilder.name("messages")
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic blockedUsers() {
        return TopicBuilder.name("blocked-users")
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic blockedWords() {
        return TopicBuilder.name("blocked-words")
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic filteredMessages() {
        return TopicBuilder.name("filtered-messages")
                .partitions(3)
                .replicas(2)
                .build();
    }
}