package Tproject.dto;

import Tproject.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatRoomDto  extends AuditableDto {
    private Long id;
    private String type;//"TASK" or "USER"
}
