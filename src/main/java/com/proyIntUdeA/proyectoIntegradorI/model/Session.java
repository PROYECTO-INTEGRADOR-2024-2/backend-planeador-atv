package com.proyIntUdeA.proyectoIntegradorI.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.proyIntUdeA.proyectoIntegradorI.model.enums.canceledBy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Session {
    private long classId;
    private boolean registered;
    private canceledBy canceledBy;
    private boolean accepted;
    private String studentId;
    private String tutorId;
    private long subjectId;
    private String classTopics;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date classDate;
    private float classRate;
}
