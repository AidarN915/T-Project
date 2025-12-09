package Tproject.dto;

import lombok.Data;

@Data
public class TypingDto {
    private String username;
    private boolean isTyping;
    private Long chatRoomId;
}
