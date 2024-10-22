package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.entity.SessionEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.AcceptSessionRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.Session;
import java.util.List;

public interface SessionService {
    Session saveSession(Session session);
    List<Session> getAllSessions();
    List<Session> getTutos();
    Session getSessionById(long id);
    boolean deleteSession(long id);
    SessionEntity updateSession(long id, SessionEntity session);
    List<Session> getAllPendingSessions();
    List<Session> getTutosTutor(String id);
    List<Session> getTutosStudent(String id);
    boolean acceptSession(AcceptSessionRequest acceptSessionRequest);

}
