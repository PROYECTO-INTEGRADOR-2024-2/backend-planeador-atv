package com.proyIntUdeA.proyectoIntegradorI.controller;
import com.proyIntUdeA.proyectoIntegradorI.Jwt.JwtService;
import com.proyIntUdeA.proyectoIntegradorI.model.Subject;
import com.proyIntUdeA.proyectoIntegradorI.service.SubjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/subject")
public class SubjectController {
    private SubjectService subjectService;
    private JwtService jwtService;

    public SubjectController(SubjectService subjectService, JwtService jwtService) {
        this.subjectService = subjectService;
        this.jwtService = jwtService;
    }

    @PostMapping("/")
    public ResponseEntity<?> saveSubject(@RequestBody Subject subject, HttpServletRequest request) {
        ResponseEntity<String> tokenVerification = jwtService.verifyToken(request);

        if (tokenVerification.getStatusCode() != HttpStatus.OK) {
            return tokenVerification;
        }

        String token = request.getHeader("Authorization").substring(7);
        String user_role = jwtService.getClaim(token, claims -> claims.get("user_role", String.class)).toLowerCase();

        if(!user_role.equalsIgnoreCase("ROLE_ADMIN")){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Debes ser usuario administrador");
        }

        Subject response = subjectService.saveSubject(subject);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubject();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable("id") long id) {
        Subject subject = null;
        subject = subjectService.getSubjectById(id);
        return ResponseEntity.ok(subject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteSubject(@PathVariable("id") long id) {
        boolean deleted = false;
        deleted = subjectService.deleteSubject(id);
        Map<String, Boolean> response= new HashMap<>();
        response.put("deleted", deleted);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable("id") long id, @RequestBody Subject subject) {
        subject = subjectService.updateSubject(id, subject);
        return ResponseEntity.ok(subject);
    }
}
