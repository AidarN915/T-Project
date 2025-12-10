package Tproject.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class AuditableDto {
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;
    private Boolean deleted;
    private LocalDateTime deletedDate;
}
