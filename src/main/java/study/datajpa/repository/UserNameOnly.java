package study.datajpa.repository;

public interface UserNameOnly {

//  @Value("#{target.username + ' ' + target.age}")
  String getUsername();
}
