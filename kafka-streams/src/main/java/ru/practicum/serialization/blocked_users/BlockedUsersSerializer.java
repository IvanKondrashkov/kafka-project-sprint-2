package ru.practicum.serialization.blocked_users;

import ru.practicum.dto.BlockedUsers;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.errors.SerializationException;

@Slf4j
public class BlockedUsersSerializer implements Serializer<BlockedUsers> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, BlockedUsers data) {
        try {
            if (data == null) {
                return null;
            }
            log.info("Serializing blocked users={}", data);
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception ex) {
            throw new SerializationException("Error serializing blocked users!");
        }
    }
}