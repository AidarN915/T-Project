package Tproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class AuditableEntity {

    @CreatedDate
    @Column(updatable = false, name = "created_date")
    private LocalDateTime createdDate;

    @CreatedBy
    @Column(updatable = false, name = "created_by")
    private String createdBy;

    @LastModifiedDate
    @Column(updatable = false, name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    public void markAsDeleted() {
        this.deleted = true;
        this.deletedDate = LocalDateTime.now();
    }
}
