package ru.practicum.serialization.message;

import ru.practicum.dto.Message;
import org.apache.kafka.common.serialization.Serdes;

public class MessageSerdes extends Serdes.WrapperSerde<Message>{
    public MessageSerdes() {
        super(new MessageSerializer(), new MessageDeserializer());
    }
}