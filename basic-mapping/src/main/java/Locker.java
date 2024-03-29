import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Locker {

  @Id @GeneratedValue
  private Long id;

  private String name;

  @OneToOne(mappedBy = "locker")
  private Member member;

}
