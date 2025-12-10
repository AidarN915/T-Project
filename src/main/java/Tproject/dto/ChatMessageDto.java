package Tproject.dto;

import Tproject.enums.MessageType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto  extends AuditableDto {
    private Long id;
    private Long chatRoomId;
    private String text;
    private MessageType messageType;
}
