import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public abstract class BaseEntity {

  private LocalDateTime createdDate;

  private LocalDateTime lastModifiedDate;

  private String createdBy;

  private String lastModifiedBy;
}
