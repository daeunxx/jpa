package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.Getter;

@MappedSuperclass
@Getter
public class JpaBaseEntity {

  @Column(updatable = false)
  private LocalDateTime createdDate;

  private LocalDateTime updatedDate;

  @PrePersist
  public void prePersist() {
    this.createdDate = LocalDateTime.now();
    this.updatedDate = LocalDateTime.now();
  }

  public void preUpdate() {
    this.updatedDate = LocalDateTime.now();
  }
}
