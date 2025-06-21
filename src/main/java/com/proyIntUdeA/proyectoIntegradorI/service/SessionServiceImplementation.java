package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoAdminDTO;
import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoDTO;
import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoTutorDTO;
import com.proyIntUdeA.proyectoIntegradorI.entity.PersonEntity;
import com.proyIntUdeA.proyectoIntegradorI.entity.SessionEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.Session;
import com.proyIntUdeA.proyectoIntegradorI.model.enums.canceledBy;
import com.proyIntUdeA.proyectoIntegradorI.repository.PersonRepository;
import com.proyIntUdeA.proyectoIntegradorI.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.proyIntUdeA.proyectoIntegradorI.utils.DateUtils;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SessionServiceImplementation implements SessionService {

    private final SessionRepository sessionRepository;
    private final PersonRepository personRepository;
    private final DateUtils dateUtils;

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
                .filter(sessionEntity -> sessionEntity.getClassDate().toInstant().isBefore(now) &&
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
                .filter(sessionEntity -> sessionEntity.getClassDate().toInstant().isAfter(now) &&
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
                .filter(sessionEntity -> sessionEntity.getClassDate().toInstant().isBefore(now) &&
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
                .filter(sessionEntity -> sessionEntity.getClassDate().toInstant().isAfter(now) &&
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
                    (float) row[7],
                    (String) row[8],
                    (String) row[9],
                    (String) row[10]);
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
                    (float) row[7],
                    (String) row[8],
                    (String) row[9],
                    (String) row[10]);
        }).collect(Collectors.toList());
    }

    @Override
    public List<BasicTutoringInfoAdminDTO> getTutoringInfoAdmin() {
        List<Object[]> rawData = sessionRepository.findBasicTutoInfoAdminRaw();
        return rawData.stream().map(row -> {
            canceledBy canceledByEnum = null;
            if (row[4] != null) {
                try {
                    Short canceledByValue = ((Number) row[4]).shortValue();
                    canceledByEnum = canceledBy.fromValue(canceledByValue);
                } catch (Exception e) {
                }
            }

            return new BasicTutoringInfoAdminDTO(
                    (Long) row[0], // class_id
                    (Date) row[1], // class_date
                    (String) row[2], // subject_name
                    (Boolean) row[3], // registered
                    canceledByEnum, // canceled_by
                    (Boolean) row[5], // accepted
                    (String) row[6], // class_topics
                    (float) row[7], // class_rate
                    (String) row[8], // student_id
                    (String) row[9], // student_firstname
                    (String) row[10], // student_lastname
                    (String) row[11], // tutor_id
                    (String) row[12], // tutor_firstname
                    (String) row[13] // tutor_lastname
            );
        }).collect(Collectors.toList());
    }

    // Devuelve true si hay algún error
    @Override
    public boolean verificarDispoTutor(String tutorId, String fechaParam, String hourParam, String period) {
        List<Object[]> fechas = sessionRepository.findDates(tutorId);
        List<String> fechasForm = new ArrayList<>();
        List<String> fechasFinal = new ArrayList<>();
        boolean isBusy = false;

        for (Object[] fecha : fechas) {
            fechasForm.add(dateUtils.formatearfecha((Date) fecha[0]));
        }

        fechasForm.forEach(fecha -> {
            if (fecha.substring(0, 10).equals(fechaParam)) {
                fechasFinal.add(fecha);
            }
        });
        System.out.println("-----------------------------------------------------");
        String hourTuto = "";
        for (String fecha : fechasFinal) {
            System.out.println("Hora stu: " + hourParam + " " + period);
            System.out.println("Hora tuto " + fecha.substring(11, 16) + " " + fecha.substring(17, 19));

            if (dateUtils.isBusyOneHourBefore(hourParam, period, fecha.substring(11, 16), fecha.substring(17, 19))
                    || dateUtils.isBusyOneHourLater(hourParam, period, fecha.substring(11, 16), fecha.substring(17, 19))) {
                isBusy = true;
            } else if (hourParam.equals(fecha.substring(11, 16)) && period.equals(fecha.substring(17, 19))) {
                isBusy = true;
            }
        }
        return isBusy;
    }
}