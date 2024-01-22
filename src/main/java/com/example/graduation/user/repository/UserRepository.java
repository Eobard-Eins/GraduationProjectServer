package com.example.graduation.user.repository;

import com.example.graduation.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor {
    public User getUserByPhone(@Param("phone") String phone);
    public boolean existsUserByPhone(@Param("phone") String phone);
}
