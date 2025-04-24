package com.proyIntUdeA.proyectoIntegradorI.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person {
    private String userId;
    private String userIdType;
    private String userFirstname;
    private String userLastname;
    private String userEmail;
    private String userPassword;
    private String userPhone;
    private String userDepartment;
    private String userCity;
    private String userState;
    private String userRole;
}
