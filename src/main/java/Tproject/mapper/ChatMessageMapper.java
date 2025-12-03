package Tproject.mapper;

import Tproject.dto.ChatMessageDto;
import Tproject.model.ChatMessage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ChatMessageMapper {
    public abstract ChatMessageDto toDto(ChatMessage chatMessage);
    public abstract List<ChatMessageDto> toListDto(List<ChatMessage> chatMessages);

    @AfterMapping
    protected void enrich(@MappingTarget ChatMessageDto dto, ChatMessage chatMessage){
        dto.setChatRoomId(chatMessage.getChatRoom().getId());
    }
}
