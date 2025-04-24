package com.proyIntUdeA.proyectoIntegradorI.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "files")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileEntity {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String type;
    private String userId;
    @Lob
    private byte[] data;

}
