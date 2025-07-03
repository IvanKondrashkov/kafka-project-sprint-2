package ru.practicum.serialization.blocked_users;

import ru.practicum.dto.BlockedUsers;
import org.apache.kafka.common.serialization.Serdes;

public class BlockedUsersSerdes extends Serdes.WrapperSerde<BlockedUsers>{
    public BlockedUsersSerdes() {
        super(new BlockedUsersSerializer(), new BlockedUsersDeserializer());
    }
}