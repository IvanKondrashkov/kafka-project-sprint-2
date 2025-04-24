package ru.practicum.serialization.blocked_words;

import ru.practicum.dto.BlockedWords;
import lombok.extern.slf4j.Slf4j;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.errors.SerializationException;

@Slf4j
public class BlockedWordsDeserializer implements Deserializer<BlockedWords> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BlockedWords deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                return null;
            }
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), BlockedWords.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing blocked words!");
        }
    }
}