import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

  @Id
  private Long id;

  @Column(name = "name", nullable = false)
  private String username;

  private int age;

  @Enumerated(EnumType.STRING)
  private RolyType rolyType;

  private LocalDateTime createdDate;

  private LocalDateTime lastModifiedDate;

  @Lob
  private String decriptioin;

  private int temp;

  public Member(Long id, String username) {
    this.id = id;
    this.username = username;
  }
}
