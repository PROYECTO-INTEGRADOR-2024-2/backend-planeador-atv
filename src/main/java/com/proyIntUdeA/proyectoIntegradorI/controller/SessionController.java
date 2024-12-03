package com.proyIntUdeA.proyectoIntegradorI.controller;

import com.proyIntUdeA.proyectoIntegradorI.entity.SessionEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.AcceptSessionRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.RejectSessionRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.Session;
import com.proyIntUdeA.proyectoIntegradorI.service.SessionService;
import lombok.AllArgsConstructor;
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
    @GetMapping("/sessionstutor/{id}")
    public List<Session> getTutosTutor(@PathVariable("id") String id) {
        return sessionService.getTutosTutor(id);
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
}
