package Tproject.dto.ws;

import lombok.Data;

@Data
public class ChatHistoryRequest {
    private Long ChatRoomId;
    private int page;
    private int size;
}

