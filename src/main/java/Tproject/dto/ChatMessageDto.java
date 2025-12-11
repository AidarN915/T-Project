package Tproject.dto;

import Tproject.enums.MessageType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDto  extends AuditableDto {
    private Long id;
    private Long chatRoomId;
    private String text;
    private MessageType messageType;
}
