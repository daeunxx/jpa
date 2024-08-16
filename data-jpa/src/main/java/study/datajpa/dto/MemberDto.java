package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.datajpa.entity.Member;

@Data
@AllArgsConstructor
public class MemberDto {

  private Long id;
  private String username;
  private int age;
  private String teamName;

  public MemberDto(Long id, String username, int age) {
    this.id = id;
    this.username = username;
    this.age = age;
  }

  public MemberDto(Member member) {
    this.id = member.getId();
    this.username = member.getUsername();
    this.age = member.getAge();
  }
}
