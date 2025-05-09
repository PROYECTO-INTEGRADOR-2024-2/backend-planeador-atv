package com.proyIntUdeA.proyectoIntegradorI.controller;

import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoDTO;
import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoTutorDTO;
import com.proyIntUdeA.proyectoIntegradorI.entity.SessionEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.RateClassRequest;
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
import java.util.Objects;

import static java.awt.SystemColor.info;

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

    // Endpoint para traer las tutorías asignadas a un tutor por id
    @GetMapping("/sessionstutor")
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
    public List<Session> getTutosStudent(HttpServletRequest request, @PathVariable("id") String id) {
        return sessionService.getTutosStudent(id);
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<?> acceptSession(@PathVariable("id") Long id, HttpServletRequest request) {
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
        Session sesion = sessionService.getSessionById(id);


        if(sesion.getTutorId().equals(tutorId)){
            sessionService.acceptSession(id, tutorId);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Tutoría valorada correctamente");
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
    public ResponseEntity<?> rateClass(@RequestBody RateClassRequest rate, HttpServletRequest request) {
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
        Session sesion = sessionService.getSessionById(rate.getClassId());

        if(!studentId.equals(sesion.getStudentId())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se puede valorar una tutoría de otro estudiante");
        }else if(!sesion.getCanceledBy().equals(canceledBy.NONE)){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se puede valorar una tutoría que está cancelada");
        }
        sessionService.rateClass(rate.getClassId(), rate.getRate());
        return ResponseEntity.status(HttpStatus.OK).body("Tutoría valorada correctamente");
    }

    // Endpoint para editar tutorías
    @PutMapping("/rejectClass/{id}")
    public ResponseEntity<?> rejectClass(@PathVariable("id") Long id, HttpServletRequest request) {
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
        Session sesion = sessionService.getSessionById(id);
        System.out.print("id que se está enviando en el token: " + tutorId);
        System.out.print("id que se está enviando en la sesión: " + sesion.getTutorId());
        if(!tutorId.equals(sesion.getTutorId())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se puede rechazar la tutoría de otro tutor");
        }else if(!sesion.getCanceledBy().equals(canceledBy.NONE)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se puede rechazar una tutoría que está cancelada");
        } else if(sesion.isAccepted()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se puede rechazar una tutoría que está aceptada");
        }
        sessionService.rejectSession(id, tutorId);
        return ResponseEntity.status(HttpStatus.OK).body("Tutoría rechazada correctamente");
    }

    @PutMapping("/cancelTutoStudent/{id}")
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
        String studentId = claims.get("user_id", String.class);
        String studentIdlc = studentId.toLowerCase();

        if(studentIdlc.equals(session.getStudentId())){
            session.setCanceledBy(canceledBy.STUDENT);
            System.out.print("Canceled by" + session.getCanceledBy());
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No puede cancelar la sesión de otro estudiante");
        }

        SessionEntity sessionEntity = new SessionEntity();
        BeanUtils.copyProperties(session, sessionEntity);
        sessionService.updateSession(id, sessionEntity);
        return ResponseEntity.status(HttpStatus.OK).body("La tutoría ha sido cancelada correctamente");
    }

    @PutMapping("/cancelTutoTutor/{id}")
    public ResponseEntity<?> cancelSessionTutor(@PathVariable("id") long id, HttpServletRequest request){
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
        String tutorId = claims.get("user_id", String.class);
        String tutorIdlc = tutorId.toLowerCase();

        if(tutorIdlc.equals(session.getTutorId())){
            session.setCanceledBy(canceledBy.TUTOR);
            System.out.print("Canceled by" + session.getCanceledBy());
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No puede cancelar una sesión que no esté asignada a usted");
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
    @GetMapping("/pool")
    public ResponseEntity<?> getTutosPool(HttpServletRequest request) {

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

        String userRole = claims.get("user_role", String.class);
        userRole = userRole.toLowerCase();
        if (userRole.equals("student") ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Los estudiantes no acceden al pool");
        }
        List<BasicTutoringInfoTutorDTO> info = sessionService.getTutoringInfoTutor("0");
        return ResponseEntity.ok(info);
    }

}
