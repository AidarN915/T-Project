package Tproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDto extends AuditableDto {
    private Long id;
    private String title;
    private List<TaskListDto> taskLists;
}
