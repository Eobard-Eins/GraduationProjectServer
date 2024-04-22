package com.example.graduation.project.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "user_info")
public class User {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;

    @Column(name = "email")
    public String email;

    @Column(name = "username")
    public String username;

    @Column(name = "password")
    public String password;

    //性别
    @Column(name = "gender")
    public String gender;

    //头像
    @Column(name = "avatar")
    public String avatar;

    //积分
    @Column(name = "point")
    public Double point;

    public User(String email){
        this.email=email;
        this.username=email.substring(0, email.indexOf('@'));
        this.avatar="eins-graduatin-oss.oss-cn-shenzhen.aliyuncs.com/default.jpg";
        this.point=5.0;
    }
}
