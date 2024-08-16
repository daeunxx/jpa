package study.datajpa.repository;

public interface NestedClosedProjections {

  // 필드명은 쿼리에서 정확하게 최적화가 됨
  String getUsername();

  // 엔티티는 쿼리를 통채로 불러옴
  TeamInfo getTeam();

  interface TeamInfo {

    String getName();
  }
}
