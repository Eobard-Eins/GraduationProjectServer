package com.example.graduation.user.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    //性别
    @Column(name = "gender")
    private String gender;

    //头像
    @Column(name = "avatar")
    private String avatar;

    //经纬度
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "latitude")
    private Double latitude;

    //积分
    @Column(name = "point")
    private Double point;

    @Column(name = "registered")
    private boolean registered;

//    //最后登录时间
//    @Column(name = "last_login_time")
//    private String lastLoginTime;
}
