package Tproject.dto;

import lombok.Data;

@Data
public class SendMessageRequest {
    private Long chatRoomId;
    private String text;
}
