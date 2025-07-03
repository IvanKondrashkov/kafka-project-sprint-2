package ru.practicum.serialization.blocked_users;

import ru.practicum.dto.BlockedUsers;
import lombok.extern.slf4j.Slf4j;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.errors.SerializationException;

@Slf4j
public class BlockedUsersDeserializer implements Deserializer<BlockedUsers> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BlockedUsers deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                return null;
            }
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), BlockedUsers.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing blocked users!");
        }
    }
}