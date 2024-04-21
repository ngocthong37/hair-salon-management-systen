package com.hairsalon.respository;

import com.hairsalon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

//    @Query(value = "SELECT S FROM User S WHERE S.serviceName LIKE CONCAT('%', :serviceName, '%')")
//    List<User> findAllStylist();

}
