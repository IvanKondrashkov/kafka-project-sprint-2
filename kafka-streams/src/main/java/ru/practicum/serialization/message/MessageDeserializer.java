package ru.practicum.serialization.message;

import ru.practicum.dto.Message;
import lombok.extern.slf4j.Slf4j;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.errors.SerializationException;

@Slf4j
public class MessageDeserializer implements Deserializer<Message> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Message deserialize(String topic, byte[] data) {
        objectMapper.registerModule(new JavaTimeModule());

        try {
            if (data == null){
                return null;
            }
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), Message.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing message!");
        }
    }
}