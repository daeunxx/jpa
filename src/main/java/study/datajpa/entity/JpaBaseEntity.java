package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;

@MappedSuperclass
@Getter
public class JpaBaseEntity {

  @Column(updatable = false)
  private LocalDateTime createdDate;

  private LocalDateTime updatedDate;

  // 저장하기 전에 해당 메서드 실행
  @PrePersist
  public void prePersist() {
    this.createdDate = LocalDateTime.now();
    this.updatedDate = LocalDateTime.now();
  }

  // 업데이트하기 전에 해당 메서드 실행
  @PreUpdate
  public void preUpdate() {
    this.updatedDate = LocalDateTime.now();
  }
}
