package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.entity.SessionEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.Session;
import com.proyIntUdeA.proyectoIntegradorI.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
@Service
public class SessionServiceImplementation implements SessionService{

    private SessionRepository sessionRepository;

    @Override
    public Session saveSession(Session session) {
        SessionEntity sessionEntity = new SessionEntity();
        BeanUtils.copyProperties(session, sessionEntity);
        sessionRepository.save(sessionEntity);
        return session;
    }

    @Override
    public List<Session> getAllSessions() {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();

        return sessionEntities.stream().map(sessionEntity -> new Session(
                sessionEntity.getClass_id(),
                sessionEntity.getClass_state(),
                sessionEntity.getStudent_id(),
                sessionEntity.getTutor_id(),
                sessionEntity.getSubject_id(),
                sessionEntity.getClass_topics(),
                sessionEntity.getClass_date(),
                sessionEntity.getClass_rate())).collect(Collectors.toList());
    }

    @Override
    public Session getSessionById(long id) {
        SessionEntity sessionEntity = sessionRepository.findById(id).get();
        Session session = new Session();
        BeanUtils.copyProperties(sessionEntity, session);
        return session;
    }

    @Override
    public boolean deleteSession(long id) {
        SessionEntity person = sessionRepository.findById(id).get();
        sessionRepository. delete(person);
        return true;
    }

    @Override
    public Session updateSession(long id, Session session) {
        SessionEntity sessionEntity = sessionRepository.findById(id).get();
        sessionEntity.setClass_state(session.getClass_state());
        sessionEntity.setStudent_id(session.getStudent_id());
        sessionEntity.setTutor_id(session.getTutor_id());
        sessionEntity.setSubject_id(session.getSubject_id());
        sessionEntity.setClass_topics(session.getClass_topics());
        sessionEntity.setClass_date(session.getClass_date());
        sessionEntity.setClass_rate(session.getClass_rate());

        sessionRepository.save(sessionEntity);
        return session;
    }
}