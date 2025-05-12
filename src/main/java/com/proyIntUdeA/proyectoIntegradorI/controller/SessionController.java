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
import com.proyIntUdeA.proyectoIntegradorI.Jwt.JwtService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/session")
public class SessionController {

    // IMPORTES DE LOS SERVICIOS NECESARIOS ---------------------------------------------------------
    private SessionService sessionService;
    private JwtService jwtService;

    // Guardar una sesión
    @PostMapping("/")
    public Session saveSession(@RequestBody Session session) {
        return sessionService.saveSession(session);
    }

    // Obtener una sesión
    @GetMapping("/")
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    //Obtener todas las sesiones
    @GetMapping("/tutos")
    public List<Session> getTutos(){
        return sessionService.getTutos();
    }

    // Obtener una tutoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable("id") long id) {
        Session session = null;
        session = sessionService.getSessionById(id);
        return ResponseEntity.ok(session);
    }

    // Borrar una tutoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteSession(@PathVariable("id") long id) {
        boolean deleted = false;
        deleted = sessionService.deleteSession(id);
        Map<String, Boolean> response= new HashMap<>();
        response.put("deleted", deleted);
        return ResponseEntity.ok(response);
    }

    // Actualizar una tutoría
    @PutMapping("/{id}")
    public ResponseEntity<SessionEntity> updateSession(@PathVariable("id") long id, @RequestBody SessionEntity session) {
        session = sessionService.updateSession(id, session);
        return ResponseEntity.ok(session);
    }

    // Traer las tutorías asignadas a un tutor por id
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

    // Endpoint para traer las tutorías asignadas a un estudiante, pasando el id del estudiante
    @GetMapping("/sessionsstudent/{id}")
    public List<Session> getTutosStudent(HttpServletRequest request, @PathVariable("id") String id) {
        return sessionService.getTutosStudent(id);
    }

    // Aceptar una tutoría. Con verificación de JWT
    @PutMapping("/accept/{id}")
    public ResponseEntity<?> acceptSession(@PathVariable("id") Long id, HttpServletRequest request) {
        ResponseEntity<String> tokenVerification = jwtService.verifyToken(request);

        if (tokenVerification.getStatusCode() != HttpStatus.OK) {
            return tokenVerification;
        }

        String token = request.getHeader("Authorization").substring(7);

        String tutorId = jwtService.getClaim(token, claims -> claims.get("user_id", String.class));
        String userRole = jwtService.getClaim(token, claims ->
                claims.get("user_role", String.class).toLowerCase()
        );

        if (!userRole.equals("tutor")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("No es un tutor el que quiere aceptar la tutoría");
        }

        sessionService.acceptSession(id, tutorId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Tutoría aceptada correctamente");
    }

    // Obtener las tutorías pasadas de un estudiante
    @GetMapping("/pastSessionsStudent/{id}")
    public List<Session> getPastTutosStudent(@PathVariable("id") String id) {
        return sessionService.getAllPastSessionsStudent(id);
    }

    // Obtener las tutorías pasadas de un tutor
    @GetMapping("/pastSessionsTutor/{id}")
    public List<Session> getPastTutosTutor(@PathVariable("id") String id) {
        return sessionService.getAllPastSessionsTutor(id);
    }

    // Obtener las tutorías con estado pendiente para un estudiante
    @GetMapping("/pendingSessionsStudent/{id}")
    public List<Session> getPendingTutosStudent(@PathVariable("id") String id) {
        return sessionService.getAllPendingSessionsStudent(id);
    }

    // Obtener las tutorías con estado pendiente para un tutor
    @GetMapping("/pendingSessionsTutor/{id}")
    public List<Session> getPendingTutosTutor(@PathVariable("id") String id) {
        return sessionService.getAllPendingSessionsTutor(id);
    }

    // Endpoint para agregar la calificación de una tutoría siendo un estudiante
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

    // Endpoint para rechazar una tutoría, siendo un tutor, se pasa el id de la tutoría
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

    // Endpoint para que un estudiante cancele una tutoría
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

    // Endpoint para que un tutor cancele una tutoría
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

    // Enpoint para traer todas las tutorías que me deben dar
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

    // Enpoint para traer todas las tutorías que están en el pool
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
