package com.proyIntUdeA.proyectoIntegradorI.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    String userId;
    String userIdType;
    String userFirstname;
    String userLastname;
    String userEmail;
    String userPassword;
    String userPhone;
    String userDepartment;
    String userCity;
    String userState;
    String userRole;
}
