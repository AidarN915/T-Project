package Tproject.dto;

import Tproject.model.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRoomDto {
    private Long id;
    private String type;//"TASK" or "USER"
    private List<UserDto> users = new ArrayList<>();
}
