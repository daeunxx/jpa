import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import java.util.ArrayList;
import java.util.List;
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
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "MEMBER_SEQ_GENERATOR"
  )
  private Long id;

  private String username;

  private int age;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @OneToOne
  @JoinColumn(name = "locker_id")
  private Locker locker;

  @OneToMany(mappedBy = "member")
  private List<MemberProduct> products = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private RoleType type;

  @Lob
  private String description;

  private int temp;

  public void changeTeam(Team team) {
    this.team = team;
    team.getMembers().add(this);
  }

}
