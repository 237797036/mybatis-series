package com.zj.chat05.demo9.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements Serializable {
    private Integer id;
    private String name;
    private Integer age;
}
