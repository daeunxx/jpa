import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@SequenceGenerator(
    name = "MEMBER_SEQ_GENERATOR",
    sequenceName = "MEMBER_SEQ"
)
public class Member {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "MEMBER_SEQ_GENERATOR"
  )
  private Long id;

  private String username;

  private int age;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;

  @OneToOne
  @JoinColumn(name = "locker_id")
  private Locker locker;

  @Enumerated(EnumType.STRING)
  private RolyType rolyType;

  private LocalDateTime createdDate;

  private LocalDateTime lastModifiedDate;

  @Lob
  private String description;

  private int temp;

  public void changeTeam(Team team) {
    this.team = team;
    team.getMembers().add(this);
  }

}
