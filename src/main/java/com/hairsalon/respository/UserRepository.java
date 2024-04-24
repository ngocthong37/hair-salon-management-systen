package com.hairsalon.respository;

import com.hairsalon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT U FROM User U where U.role = 'CUSTOMER'")
    List<User> findAllCustomer();

    @Query(value = "SELECT U FROM User U where U.role = 'EMPLOYEE'")
    List<User> findAllEmployee();




//    List<User> findAllStylist();

}
