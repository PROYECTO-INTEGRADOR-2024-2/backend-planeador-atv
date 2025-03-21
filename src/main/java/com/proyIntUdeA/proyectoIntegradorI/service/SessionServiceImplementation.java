package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.entity.PersonEntity;
import com.proyIntUdeA.proyectoIntegradorI.entity.SessionEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.AcceptSessionRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.RejectSessionRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.Session;
import com.proyIntUdeA.proyectoIntegradorI.repository.PersonRepository;
import com.proyIntUdeA.proyectoIntegradorI.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SessionServiceImplementation implements SessionService {

    private SessionRepository sessionRepository;
    private PersonRepository personRepository;

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

    public List<Session> getTutos() {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();

        return sessionEntities.stream().map(sessionEntity -> new Session(
                sessionEntity.getClass_id(),
                sessionEntity.getClass_state(),
                personRepository.findById(sessionEntity.getStudent_id()).get().getUsername() + " " +
                        personRepository.findById(sessionEntity.getStudent_id()).get().getUser_lastname(),
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
        sessionRepository.delete(person);
        return true;
    }

    @Override
    public SessionEntity updateSession(long id, SessionEntity session) {
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

    @Override
    public List<Session> getAllPendingSessions() {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();

        return sessionEntities.stream().filter(sessionEntity -> "pendiente".equals(sessionEntity.getClass_state()))
                .map(sessionEntity -> new Session(
                sessionEntity.getClass_id(),
                sessionEntity.getClass_state(),
                personRepository.findById(sessionEntity.getStudent_id()).get().getUsername() + " " +
                        personRepository.findById(sessionEntity.getStudent_id()).get().getUser_lastname(),
                sessionEntity.getTutor_id(),
                sessionEntity.getSubject_id(),
                sessionEntity.getClass_topics(),
                sessionEntity.getClass_date(),
                sessionEntity.getClass_rate())).collect(Collectors.toList());
    }

    // Sé que es confuso el nombre xd, así que es un metodo para traer todas las tutorías
    // que están asignadas a un tutor pasándole su id
    @Override
    public List<Session> getTutosTutor(String id) {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();

        return sessionEntities.stream()
                .filter(sessionEntity -> id.equals(sessionEntity.getTutor_id()))
                .map(sessionEntity -> new Session(
                        sessionEntity.getClass_id(),
                        sessionEntity.getClass_state(),
                        personRepository.findById(sessionEntity.getStudent_id()).get().getUsername() + " " +
                                personRepository.findById(sessionEntity.getStudent_id()).get().getUser_lastname(),
                        sessionEntity.getTutor_id(),
                        sessionEntity.getSubject_id(),
                        sessionEntity.getClass_topics(),
                        sessionEntity.getClass_date(),
                        sessionEntity.getClass_rate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Session> getTutosStudent(String id) {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();

        return sessionEntities.stream()
                .filter(sessionEntity -> id.equals(sessionEntity.getStudent_id()))
                .map(sessionEntity -> new Session(
                        sessionEntity.getClass_id(),
                        sessionEntity.getClass_state(),
                        personRepository.findById(sessionEntity.getStudent_id()).get().getUsername() + " " +
                                personRepository.findById(sessionEntity.getStudent_id()).get().getUser_lastname(),
                        personRepository.findById(sessionEntity.getTutor_id()).get().getUsername() + " " +
                                personRepository.findById(sessionEntity.getTutor_id()).get().getUser_lastname(),
                        sessionEntity.getSubject_id(),
                        sessionEntity.getClass_topics(),
                        sessionEntity.getClass_date(),
                        sessionEntity.getClass_rate()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean acceptSession(AcceptSessionRequest acceptSessionRequest) {
        long sessionId = acceptSessionRequest.getSessionId();
        String tutorId = acceptSessionRequest.getTutorId();
        Optional<SessionEntity> session = sessionRepository.findById(sessionId);
        SessionEntity sessionEntity = session.get();
        sessionEntity.setClass_state("aceptada");
        sessionEntity.setTutor_id(tutorId);
        updateSession(sessionId,sessionEntity);
        return true;
    }

    @Override
    public boolean rejectSession(RejectSessionRequest rejectSessionRequest) {
        long sessionId = rejectSessionRequest.getSessionId();
        String tutorId = rejectSessionRequest.getTutorId();
        Optional<PersonEntity> person = personRepository.findById(tutorId);
            if (!person.isPresent()) {
                throw new RuntimeException("No eres tutor para hacer esta operación");
            }

        Optional<SessionEntity> session = sessionRepository.findById(sessionId);
        SessionEntity sessionEntity = session.get();
        sessionEntity.setClass_state("pendiente");
        sessionEntity.setTutor_id("0000");
        updateSession(sessionId,sessionEntity);
        return true;
    }

    @Override
    public List<Session> getAllPastSessionsStudent(String id) {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        Instant now = Instant.now();
        Instant twoHoursAgo = now.minus(7, ChronoUnit.HOURS);
        List<Session> allSessions = sessionEntities.stream()
                .filter(sessionEntity ->
                        sessionEntity.getClass_date().toInstant().isBefore(twoHoursAgo) &&
                                id.equals(sessionEntity.getStudent_id()))
                .map(sessionEntity -> new Session(
                        sessionEntity.getClass_id(),
                        sessionEntity.getClass_state(),
                        personRepository.findById(sessionEntity.getStudent_id()).get().getUsername() + " " +
                                personRepository.findById(sessionEntity.getStudent_id()).get().getUser_lastname(),
                        sessionEntity.getTutor_id(),
                        sessionEntity.getSubject_id(),
                        sessionEntity.getClass_topics(),
                        sessionEntity.getClass_date(),
                        sessionEntity.getClass_rate())).collect(Collectors.toList());

        return allSessions;
    }

    @Override
    public List<Session> getAllPendingSessionsStudent(String id) {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        Instant now = Instant.now();
        Instant twoHoursAgo = now.minus(7, ChronoUnit.HOURS);
        List<Session> allSessions = sessionEntities.stream()
                .filter(sessionEntity ->
                        sessionEntity.getClass_date().toInstant().isAfter(twoHoursAgo) &&
                                id.equals(sessionEntity.getStudent_id()))
                .map(sessionEntity -> new Session(
                        sessionEntity.getClass_id(),
                        sessionEntity.getClass_state(),
                        personRepository.findById(sessionEntity.getStudent_id()).get().getUsername() + " " +
                                personRepository.findById(sessionEntity.getStudent_id()).get().getUser_lastname(),
                        sessionEntity.getTutor_id(),
                        sessionEntity.getSubject_id(),
                        sessionEntity.getClass_topics(),
                        sessionEntity.getClass_date(),
                        sessionEntity.getClass_rate())).collect(Collectors.toList());

        return allSessions;
    }

    @Override
    public List<Session> getAllPastSessionsTutor(String id) {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        Instant now = Instant.now();
        Instant twoHoursAgo = now.minus(7, ChronoUnit.HOURS);
        List<Session> allSessions = sessionEntities.stream()
                .filter(sessionEntity ->
                        sessionEntity.getClass_date().toInstant().isBefore(twoHoursAgo) &&
                                id.equals(sessionEntity.getTutor_id()))
                .map(sessionEntity -> new Session(
                        sessionEntity.getClass_id(),
                        sessionEntity.getClass_state(),
                        personRepository.findById(sessionEntity.getStudent_id()).get().getUsername() + " " +
                                personRepository.findById(sessionEntity.getStudent_id()).get().getUser_lastname(),
                        sessionEntity.getTutor_id(),
                        sessionEntity.getSubject_id(),
                        sessionEntity.getClass_topics(),
                        sessionEntity.getClass_date(),
                        sessionEntity.getClass_rate())).collect(Collectors.toList());

        return allSessions;
    }

    @Override
    public List<Session> getAllPendingSessionsTutor(String id) {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        Instant now = Instant.now();
        Instant twoHoursAgo = now.minus(7, ChronoUnit.HOURS);
        List<Session> allSessions = sessionEntities.stream()
                .filter(sessionEntity ->
                        sessionEntity.getClass_date().toInstant().isAfter(twoHoursAgo) &&
                                id.equals(sessionEntity.getTutor_id()))
                .map(sessionEntity -> new Session(
                        sessionEntity.getClass_id(),
                        sessionEntity.getClass_state(),
                        personRepository.findById(sessionEntity.getStudent_id()).get().getUsername() + " " +
                                personRepository.findById(sessionEntity.getStudent_id()).get().getUser_lastname(),
                        sessionEntity.getTutor_id(),
                        sessionEntity.getSubject_id(),
                        sessionEntity.getClass_topics(),
                        sessionEntity.getClass_date(),
                        sessionEntity.getClass_rate())).collect(Collectors.toList());

        return allSessions;
    }

    // Métodos para realizar acciones sobre tutorías pasadas de tiempo

    @Override
    public boolean rateClass(Long classId, float rate) {
        SessionEntity session = sessionRepository.findById(classId).get();
        session.setClass_rate(rate);
        SessionEntity saved = sessionRepository.save(session);
        if(saved != null) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean registerClass(Long classId) {
        return false;
    }

    @Override
    public boolean noClass(Long classId) {
        return false;
    }




}
