package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoDTO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SessionServiceImplementation implements SessionService {

    private final SessionRepository sessionRepository;
    private final PersonRepository personRepository;

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

        return sessionEntities.stream().map(this::mapEntityToSession).collect(Collectors.toList());
    }

    public List<Session> getTutos() {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();

        return sessionEntities.stream().map(sessionEntity -> {
            String studentName = getPersonFullName(sessionEntity.getStudentId());
            return new Session(
                    sessionEntity.getClassId(),
                    sessionEntity.getClassState(),
                    studentName,
                    sessionEntity.getTutorId(),
                    sessionEntity.getSubjectId(),
                    sessionEntity.getClassTopics(),
                    sessionEntity.getClassDate(),
                    sessionEntity.getClassRate());
        }).collect(Collectors.toList());
    }

    @Override
    public Session getSessionById(long id) {
        Optional<SessionEntity> sessionEntityOpt = sessionRepository.findById(id);
        if (sessionEntityOpt.isPresent()) {
            SessionEntity sessionEntity = sessionEntityOpt.get();
            Session session = new Session();
            BeanUtils.copyProperties(sessionEntity, session);
            return session;
        }
        throw new RuntimeException("No se encontró la sesión con ID: " + id);
    }

    @Override
    public boolean deleteSession(long id) {
        Optional<SessionEntity> sessionOpt = sessionRepository.findById(id);
        if (sessionOpt.isPresent()) {
            sessionRepository.delete(sessionOpt.get());
            return true;
        }
        return false;
    }

    @Override
    public SessionEntity updateSession(long id, SessionEntity session) {
        Optional<SessionEntity> sessionEntityOpt = sessionRepository.findById(id);
        if (sessionEntityOpt.isPresent()) {
            SessionEntity sessionEntity = sessionEntityOpt.get();
            sessionEntity.setClassState(session.getClassState());
            sessionEntity.setStudentId(session.getStudentId());
            sessionEntity.setTutorId(session.getTutorId());
            sessionEntity.setStudentId(session.getStudentId());
            sessionEntity.setClassTopics(session.getClassTopics());
            sessionEntity.setClassDate(session.getClassDate());
            sessionEntity.setClassRate(session.getClassRate());

            return sessionRepository.save(sessionEntity);
        }
        throw new RuntimeException("No se encontró la sesión con ID: " + id);
    }

    @Override
    public List<Session> getAllPendingSessions() {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();

        return sessionEntities.stream()
                .filter(sessionEntity -> "pendiente".equals(sessionEntity.getClassState()))
                .map(sessionEntity -> {
                    String studentName = getPersonFullName(sessionEntity.getStudentId());
                    return new Session(
                            sessionEntity.getClassId(),
                            sessionEntity.getClassState(),
                            studentName,
                            sessionEntity.getTutorId(),
                            sessionEntity.getSubjectId(),
                            sessionEntity.getClassTopics(),
                            sessionEntity.getClassDate(),
                            sessionEntity.getClassRate());
                }).collect(Collectors.toList());
    }

    // Método para traer todas las tutorías asignadas a un tutor por su ID
    @Override
    public List<Session> getTutosTutor(String id) {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();

        return sessionEntities.stream()
                .filter(sessionEntity -> id.equals(sessionEntity.getTutorId()))
                .map(sessionEntity -> {
                    String studentName = getPersonFullName(sessionEntity.getStudentId());
                    return new Session(
                            sessionEntity.getClassId(),
                            sessionEntity.getClassState(),
                            studentName,
                            sessionEntity.getTutorId(),
                            sessionEntity.getSubjectId(),
                            sessionEntity.getClassTopics(),
                            sessionEntity.getClassDate(),
                            sessionEntity.getClassRate());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Session> getTutosStudent(String id) {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();

        return sessionEntities.stream()
                .filter(sessionEntity -> id.equals(sessionEntity.getStudentId()))
                .map(sessionEntity -> {
                    String studentName = getPersonFullName(sessionEntity.getStudentId());
                    String tutorName = getPersonFullName(sessionEntity.getTutorId());

                    return new Session(
                            sessionEntity.getClassId(),
                            sessionEntity.getClassState(),
                            studentName,
                            tutorName,
                            sessionEntity.getSubjectId(),
                            sessionEntity.getClassTopics(),
                            sessionEntity.getClassDate(),
                            sessionEntity.getClassRate());
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean acceptSession(AcceptSessionRequest acceptSessionRequest) {
        long sessionId = acceptSessionRequest.getSessionId();
        String tutorId = acceptSessionRequest.getTutorId();

        Optional<SessionEntity> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isPresent()) {
            SessionEntity sessionEntity = sessionOpt.get();
            sessionEntity.setClassState("aceptada");
            sessionEntity.setTutorId(tutorId);
            updateSession(sessionId, sessionEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean rejectSession(RejectSessionRequest rejectSessionRequest) {
        long sessionId = rejectSessionRequest.getSessionId();
        String tutorId = rejectSessionRequest.getTutorId();

        Optional<PersonEntity> personOpt = personRepository.findById(tutorId);
        if (personOpt.isEmpty()) {
            throw new RuntimeException("No eres tutor para hacer esta operación");
        }

        Optional<SessionEntity> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isPresent()) {
            SessionEntity sessionEntity = sessionOpt.get();
            sessionEntity.setClassState("pendiente");
            sessionEntity.setTutorId("0000");
            updateSession(sessionId, sessionEntity);
            return true;
        }
        return false;
    }

    @Override
    public List<Session> getAllPastSessionsStudent(String id) {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        Instant now = Instant.now();

        return sessionEntities.stream()
                .filter(sessionEntity ->
                        sessionEntity.getClassDate().toInstant().isBefore(now) &&
                                id.equals(sessionEntity.getStudentId()))
                .map(sessionEntity -> {
                    String studentName = getPersonFullName(sessionEntity.getStudentId());
                    String tutorName = getPersonFullName(sessionEntity.getTutorId());

                    return new Session(
                            sessionEntity.getClassId(),
                            sessionEntity.getClassState(),
                            studentName,
                            tutorName,
                            sessionEntity.getSubjectId(),
                            sessionEntity.getClassTopics(),
                            sessionEntity.getClassDate(),
                            sessionEntity.getClassRate());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Session> getAllPendingSessionsStudent(String id) {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        Instant now = Instant.now();

        return sessionEntities.stream()
                .filter(sessionEntity ->
                        sessionEntity.getClassDate().toInstant().isAfter(now) &&
                                id.equals(sessionEntity.getStudentId()))
                .map(sessionEntity -> {
                    String studentName = getPersonFullName(sessionEntity.getStudentId());
                    String tutorName = getPersonFullName(sessionEntity.getTutorId());

                    return new Session(
                            sessionEntity.getClassId(),
                            sessionEntity.getClassState(),
                            studentName,
                            tutorName,
                            sessionEntity.getSubjectId(),
                            sessionEntity.getClassTopics(),
                            sessionEntity.getClassDate(),
                            sessionEntity.getClassRate());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Session> getAllPastSessionsTutor(String id) {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        Instant now = Instant.now();

        return sessionEntities.stream()
                .filter(sessionEntity ->
                        sessionEntity.getClassDate().toInstant().isBefore(now) &&
                                id.equals(sessionEntity.getTutorId()))
                .map(sessionEntity -> {
                    String studentName = getPersonFullName(sessionEntity.getStudentId());

                    return new Session(
                            sessionEntity.getClassId(),
                            sessionEntity.getClassState(),
                            studentName,
                            sessionEntity.getTutorId(),
                            sessionEntity.getSubjectId(),
                            sessionEntity.getClassTopics(),
                            sessionEntity.getClassDate(),
                            sessionEntity.getClassRate());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Session> getAllPendingSessionsTutor(String id) {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        Instant now = Instant.now();

        return sessionEntities.stream()
                .filter(sessionEntity ->
                        sessionEntity.getClassDate().toInstant().isAfter(now) &&
                                id.equals(sessionEntity.getTutorId()))
                .map(sessionEntity -> {
                    String studentName = getPersonFullName(sessionEntity.getStudentId());

                    return new Session(
                            sessionEntity.getClassId(),
                            sessionEntity.getClassState(),
                            studentName,
                            sessionEntity.getTutorId(),
                            sessionEntity.getSubjectId(),
                            sessionEntity.getClassTopics(),
                            sessionEntity.getClassDate(),
                            sessionEntity.getClassRate());
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean rateClass(Long classId, float rate) {
        Optional<SessionEntity> sessionOpt = sessionRepository.findById(classId);
        if (sessionOpt.isPresent()) {
            SessionEntity session = sessionOpt.get();
            session.setClassRate(rate);
            SessionEntity saved = sessionRepository.save(session);
            return saved != null;
        }
        return false;
    }

    @Override
    public boolean registerClass(Long classId) {
        Optional<SessionEntity> sessionOpt = sessionRepository.findById(classId);
        if (sessionOpt.isPresent()) {
            SessionEntity session = sessionOpt.get();
            session.setClassState("realizada");
            SessionEntity saved = sessionRepository.save(session);
            return saved != null;
        }
        return false;
    }

    @Override
    public boolean noClass(Long classId) {
        Optional<SessionEntity> sessionOpt = sessionRepository.findById(classId);
        if (sessionOpt.isPresent()) {
            SessionEntity session = sessionOpt.get();
            session.setClassState("no realizada");
            SessionEntity saved = sessionRepository.save(session);
            return saved != null;
        }
        return false;
    }

    // Métodos auxiliares

    private String getPersonFullName(String personId) {
        Optional<PersonEntity> personOpt = personRepository.findById(personId);
        return personOpt.map(person -> person.getUsername() + " " + person.getUserLastname())
                .orElse("Usuario no encontrado");
    }

    private Session mapEntityToSession(SessionEntity entity) {
        Session session = new Session();
        BeanUtils.copyProperties(entity, session);
        return session;
    }
    @Override
    public List<BasicTutoringInfoDTO> getTutoringInfo(String studentId) {
        List<Object[]> rawData = sessionRepository.findBasicTutoInfoRaw(studentId);
        return rawData.stream().map(row -> new BasicTutoringInfoDTO(
                (Date) row[0],
                (String) row[1],
                (String) row[2],
                (String) row[3],
                (String) row[4],
                (String) row[5]
        )).collect(Collectors.toList());
    }

}