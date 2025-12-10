package Tproject.dto.ws;

import lombok.Data;

@Data
public class SendMessageRequest {
    private Long chatRoomId;
    private String text;
}
