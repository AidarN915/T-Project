package Tproject.mapper;

import Tproject.dto.ChatRoomDto;
import Tproject.model.ChatRoom;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ChatRoomMapper {
    public abstract ChatRoomDto toDto(ChatRoom chatRoom);
    public abstract List<ChatRoomDto> toListDto(List<ChatRoom> chatRoomList);
}
