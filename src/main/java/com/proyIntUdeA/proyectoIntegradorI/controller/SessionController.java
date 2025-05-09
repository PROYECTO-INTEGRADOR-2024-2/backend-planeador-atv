package com.proyIntUdeA.proyectoIntegradorI.controller;

import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoDTO;
import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoTutorDTO;
import com.proyIntUdeA.proyectoIntegradorI.entity.PersonEntity;
import com.proyIntUdeA.proyectoIntegradorI.entity.SessionEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.AcceptSessionRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.RateClassRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.RejectSessionRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.Session;
import com.proyIntUdeA.proyectoIntegradorI.model.enums.canceledBy;
import com.proyIntUdeA.proyectoIntegradorI.service.SessionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/session")
public class SessionController {
    private SessionService sessionService;
    @PostMapping("/")
    public Session saveSession(@RequestBody Session session) {
        return sessionService.saveSession(session);
    }

    @GetMapping("/")
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/tutos")
    public List<Session> getTutos(){
        return sessionService.getTutos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable("id") long id) {
        Session session = null;
        session = sessionService.getSessionById(id);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteSession(@PathVariable("id") long id) {
        boolean deleted = false;
        deleted = sessionService.deleteSession(id);
        Map<String, Boolean> response= new HashMap<>();
        response.put("deleted", deleted);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessionEntity> updateSession(@PathVariable("id") long id, @RequestBody SessionEntity session) {
        session = sessionService.updateSession(id, session);
        return ResponseEntity.ok(session);
    }

    @PutMapping("/rejectSession")
    public ResponseEntity<SessionEntity> rejectSession(@RequestBody RejectSessionRequest request) {
        boolean status = sessionService.rejectSession(request);
        if (status) {
            return ResponseEntity.ok(new SessionEntity());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pool")
    public List<Session> getPendingSessions() {
        return sessionService.getAllPendingSessions();
    }

    // Endpoint para traer las tutorías asignadas a un tutor por id
    @GetMapping("/sessionstutor/tutosTutor")
    public ResponseEntity<?> getTutosTutor(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Token missing or invalid");
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey("586E3272357538782F413F4428472B4B6250655368566B597033733676397924")
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");
        }

        String tutorId = claims.get("user_id", String.class);

        List<BasicTutoringInfoTutorDTO> info = sessionService.getTutoringInfoTutor(tutorId);
        System.out.println("Buscando tutorías que estén asignadas al id " + tutorId);
        return ResponseEntity.ok(info);
    }

    // Endpoint para traer las tutorías asignadas a un estudiante
    @GetMapping("/sessionsstudent/{id}")
    public List<Session> getTutosStudent(@PathVariable("id") String id) {
        return sessionService.getTutosStudent(id);
    }

    @PutMapping("/sessionsPoolAccept")
    public boolean acceptSession(@RequestBody AcceptSessionRequest acceptSessionRequest) {
        return sessionService.acceptSession(acceptSessionRequest);
    }

    @GetMapping("/pastSessionsStudent/{id}")
    public List<Session> getPastTutosStudent(@PathVariable("id") String id) {
        return sessionService.getAllPastSessionsStudent(id);
    }

    @GetMapping("/pastSessionsTutor/{id}")
    public List<Session> getPastTutosTutor(@PathVariable("id") String id) {
        return sessionService.getAllPastSessionsTutor(id);
    }

    @GetMapping("/pendingSessionsStudent/{id}")
    public List<Session> getPendingTutosStudent(@PathVariable("id") String id) {
        return sessionService.getAllPendingSessionsStudent(id);
    }

    @GetMapping("/pendingSessionsTutor/{id}")
    public List<Session> getPendingTutosTutor(@PathVariable("id") String id) {
        return sessionService.getAllPendingSessionsTutor(id);
    }

    // Endpoint para editar tutorías
    @PutMapping("/rateClass")
    public boolean rateClass(@RequestBody RateClassRequest rate) {
        return sessionService.rateClass(rate.getClassId(), rate.getRate());
    }

    @PutMapping("/cancelTuto/{id}")
    public ResponseEntity<?> cancelSession(@PathVariable("id") long id, HttpServletRequest request){



        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Token missing or invalid");
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey("586E3272357538782F413F4428472B4B6250655368566B597033733676397924")
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");
        }

        Session session = sessionService.getSessionById(id);
        String personRole = claims.get("user_role", String.class);
        String role = personRole.toLowerCase();

        if(role.equals("student")){
            session.setCanceledBy(canceledBy.STUDENT);
        } else if (role.equals("tutor")) {
            session.setCanceledBy(canceledBy.TUTOR);
        } else if (role.equals("admin")) {
            session.setCanceledBy(canceledBy.ADMIN);
        }

        SessionEntity sessionEntity = new SessionEntity();
        BeanUtils.copyProperties(session, sessionEntity);
        sessionService.updateSession(id, sessionEntity);
        return ResponseEntity.status(HttpStatus.OK).body("La tutoría ha sido cancelada correctamente");
    }

    @GetMapping("/personalTutos")
    public ResponseEntity<?> getTutoringInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Token missing or invalid");
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey("586E3272357538782F413F4428472B4B6250655368566B597033733676397924")
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");
        }

        String studentId = claims.get("user_id", String.class);

        List<BasicTutoringInfoDTO> info = sessionService.getTutoringInfo(studentId);
        return ResponseEntity.ok(info);
    }

}
