package ru.practicum.serialization.blocked_words;

import ru.practicum.dto.BlockedWords;
import org.apache.kafka.common.serialization.Serdes;

public class BlockedWordsSerdes extends Serdes.WrapperSerde<BlockedWords>{
    public BlockedWordsSerdes() {
        super(new BlockedWordsSerializer(), new BlockedWordsDeserializer());
    }
}