package Tproject.dto;

import Tproject.enums.MessageType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private Long id;
    private Long chatRoomId;
    private String text;
    private MessageType messageType;
    private LocalDateTime createdDate;
    private UserDto sender;
}
