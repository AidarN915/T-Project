package Tproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDto {
    private Long id;
    private String title;
    private List<TaskListDto> taskLists;
}
