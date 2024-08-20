package jpql;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Team {

  @Id
  @GeneratedValue
  private Long id;
  private String name;

  //@BatchSize(size = 100)
  @OneToMany(mappedBy = "team")
  private List<Member> members = new ArrayList<>();
}
