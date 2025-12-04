package Tproject.dto;

import lombok.Data;

@Data
public class ChatHistoryRequest {
    private Long ChatRoomId;
    private int page;
    private int size;
}

