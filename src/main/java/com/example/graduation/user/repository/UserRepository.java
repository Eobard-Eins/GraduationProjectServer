package com.example.graduation.user.repository;

import com.example.graduation.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Modifying
    @Query(value = "update users set username = :un where phone = :ph", nativeQuery = true)
    int updateUsername(@Param("un") String username, @Param("ph") String phone);//返回更新条数

    @Modifying
    @Query(value = "update users set password = :pw where phone = :ph", nativeQuery = true)
    int updatePassword(@Param("pw") String password, @Param("ph") String phone);

    @Modifying
    @Query(value = "update users set avatar = :ava where phone = :ph", nativeQuery = true)
    int updateAvatar(@Param("ava") String avatar, @Param("ph") String phone);

    @Modifying
    @Query(value = "update users set longitude = :lng, latitude = :lat where phone = :ph", nativeQuery = true)
    int updateLongitudeAndLongitude(@Param("lng") Double longitude, @Param("lat") Double latitude, @Param("ph") String phone);

    @Modifying
    @Query(value = "update users set point = :pt where phone = :ph", nativeQuery = true)
    int updatePoint(@Param("pt") Double point, @Param("ph") String phone);

    @Modifying
    @Query(value = "update users set registered = :reg where phone = :ph", nativeQuery = true)
    int updateRegistered(@Param("reg") boolean registered, @Param("ph") String phone);


}
