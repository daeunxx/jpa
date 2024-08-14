package org.example.querydsl.repository;

import java.util.List;
import org.example.querydsl.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  List<Member> findByUsername(String username);
}
