package Tproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUploadDto extends AuditableDto {
    private Long id;
    private String url;
}
