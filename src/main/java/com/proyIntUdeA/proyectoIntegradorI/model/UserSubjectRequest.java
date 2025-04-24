package com.proyIntUdeA.proyectoIntegradorI.model;

import lombok.Data;

import java.util.List;

@Data
public class UserSubjectRequest {
    private String user_id;
    private List<Long> subject_ids;
}
