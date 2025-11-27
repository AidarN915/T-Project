package Tproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardUpdateDto {
    private String title;
    private List<TaskListDto> taskLists;
}
