package Tproject.dto;

import lombok.Data;

@Data
public class TaskUploadDto extends AuditableDto {
    private Long id;
    private String url;
}
