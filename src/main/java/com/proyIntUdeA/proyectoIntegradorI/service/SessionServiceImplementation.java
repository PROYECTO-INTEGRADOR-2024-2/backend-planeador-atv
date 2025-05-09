package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoDTO;
import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoTutorDTO;
import com.proyIntUdeA.proyectoIntegradorI.entity.PersonEntity;
import com.proyIntUdeA.proyectoIntegradorI.entity.SessionEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.AcceptSessionRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.RejectSessionRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.Session;
import com.proyIntUdeA.proyectoIntegradorI.model.enums.canceledBy;
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
                    sessionEntity.isRegistered(),
                    sessionEntity.getCanceledBy(),
                    sessionEntity.isAccepted(),
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
            sessionEntity.setCanceledBy(session.getCanceledBy());
            sessionEntity.setRegistered(session.isRegistered());
            sessionEntity.setAccepted(session.isAccepted());
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
                .filter(sessionEntity -> sessionEntity.isRegistered() == false)
                .map(sessionEntity -> {
                    String studentName = getPersonFullName(sessionEntity.getStudentId());
                    return new Session(
                            sessionEntity.getClassId(),
                            sessionEntity.isRegistered(),
                            sessionEntity.getCanceledBy(),
                            sessionEntity.isAccepted(),
                            studentName,
                            sessionEntity.getTutorId(),
                            sessionEntity.getSubjectId(),
                            sessionEntity.getClassTopics(),
                            sessionEntity.getClassDate(),
                            sessionEntity.getClassRate());
                }).collect(Collectors.toList());
    }

    @Override
    public List<Session> getTutosTutor(String id) {
        System.out.println("El id es: " + id);
        List<SessionEntity> sessionEntities = sessionRepository.findAll();

        return sessionEntities.stream()
                .filter(sessionEntity -> id.equals(sessionEntity.getTutorId()))
                .map(sessionEntity -> {
                    String studentName = getPersonFullName(sessionEntity.getStudentId());
                    return new Session(
                            sessionEntity.getClassId(),
                            sessionEntity.isRegistered(),
                            sessionEntity.getCanceledBy(),
                            sessionEntity.isAccepted(),
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
                            sessionEntity.isRegistered(),
                            sessionEntity.getCanceledBy(),
                            sessionEntity.isAccepted(),
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
    public boolean acceptSession(Long sessionId, String tutorId) {

        Optional<SessionEntity> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isPresent()) {
            SessionEntity sessionEntity = sessionOpt.get();
            sessionEntity.setAccepted(true);
            sessionEntity.setTutorId(tutorId);
            updateSession(sessionId, sessionEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean rejectSession(Long id, String userId) {
        long sessionId = id;
        String tutorId = userId;

        Optional<PersonEntity> personOpt = personRepository.findById(tutorId);
        if (personOpt.isEmpty()) {
            throw new RuntimeException("No eres tutor para hacer esta operación");
        }

        Optional<SessionEntity> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isPresent()) {
            SessionEntity sessionEntity = sessionOpt.get();
            sessionEntity.setRegistered(false);
            sessionEntity.setTutorId("0");
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
                            sessionEntity.isRegistered(),
                            sessionEntity.getCanceledBy(),
                            sessionEntity.isAccepted(),
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
                            sessionEntity.isRegistered(),
                            sessionEntity.getCanceledBy(),
                            sessionEntity.isAccepted(),
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
                            sessionEntity.isRegistered(),
                            sessionEntity.getCanceledBy(),
                            sessionEntity.isAccepted(),
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
                            sessionEntity.isRegistered(),
                            sessionEntity.getCanceledBy(),
                            sessionEntity.isAccepted(),
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
            session.setRegistered(true);
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
            session.setRegistered(false);
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
        return rawData.stream().map(row -> {
            canceledBy canceledByEnum = null;
            if (row[4] != null) {
                try {
                    Short canceledByValue = (Short) row[4];
                    canceledByEnum = canceledBy.fromValue(canceledByValue);
                } catch (Exception e) {
                }
            }

            return new BasicTutoringInfoDTO(
                    (Long) row[0],
                    (Date) row[1],
                    (String) row[2],
                    (boolean) row[3],
                    canceledByEnum,
                    (boolean) row[5],
                    (String) row[6],
                    (String) row[7],
                    (String) row[8],
                    (String) row[9]
            );
        }).collect(Collectors.toList());
    }

    @Override
    public List<BasicTutoringInfoTutorDTO> getTutoringInfoTutor(String tutorId) {
        List<Object[]> rawData = sessionRepository.findBasicTutoInfoTutorRaw(tutorId);
        return rawData.stream().map(row -> {
            canceledBy canceledByEnum = null;
            if (row[4] != null) {
                try {
                    Short canceledByValue = (Short) row[4];
                    canceledByEnum = canceledBy.fromValue(canceledByValue);
                } catch (Exception e) {
                }
            }

            return new BasicTutoringInfoTutorDTO(
                    (Long) row[0],
                    (Date) row[1],
                    (String) row[2],
                    (boolean) row[3],
                    canceledByEnum,
                    (boolean) row[5],
                    (String) row[6],
                    (String) row[7],
                    (String) row[8],
                    (String) row[9]
            );
        }).collect(Collectors.toList());
    }
}