package ru.practicum.serialization.blocked_words;

import ru.practicum.dto.BlockedWords;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.errors.SerializationException;

@Slf4j
public class BlockedWordsSerializer implements Serializer<BlockedWords> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, BlockedWords data) {
        try {
            if (data == null) {
                return null;
            }
            log.info("Serializing blocked words={}", data);
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception ex) {
            throw new SerializationException("Error serializing blocked words!");
        }
    }
}