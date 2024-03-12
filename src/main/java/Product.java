import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Product {

  @Id @GeneratedValue
  private Long id;

  private String name;

  @OneToMany(mappedBy = "product")
  List<MemberProduct> memberProducts = new ArrayList<>();

}
