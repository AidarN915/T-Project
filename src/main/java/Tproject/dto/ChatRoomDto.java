package Tproject.dto;

import Tproject.model.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRoomDto  extends AuditableDto {
    private Long id;
    private String type;//"TASK" or "USER"
}
