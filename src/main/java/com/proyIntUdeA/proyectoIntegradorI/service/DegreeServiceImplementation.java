package com.proyIntUdeA.proyectoIntegradorI.service;
import com.proyIntUdeA.proyectoIntegradorI.entity.DegreeEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.Degree;
import com.proyIntUdeA.proyectoIntegradorI.repository.DegreeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class DegreeServiceImplementation implements DegreeService{

    private final DegreeRepository degreeRepository;
    public DegreeServiceImplementation(DegreeRepository degreeRepository) {
        this.degreeRepository = degreeRepository;
    }
    @Override
    public Degree saveDegree(Degree degree) {
        DegreeEntity degreeEntity = new DegreeEntity();
        BeanUtils.copyProperties(degree, degreeEntity);
        degreeRepository.save(degreeEntity);
        return degree;
    }

    @Override
    public List<Degree> getAllDegree() {
        List<DegreeEntity> degreeEntities = degreeRepository.findAll();

        return degreeEntities.stream().map(degreeEntity -> new Degree(
                (long) degreeEntity.getDegree_id(),
                degreeEntity.getDegree_name(),
                degreeEntity.getDegree_modality(),
                degreeEntity.getDegree_department())).toList();
    }

    @Override
    public Degree getDegreeById(long id) {

        DegreeEntity DegreeEntity = degreeRepository.findById(id).get();
        Degree degree = new Degree();
        BeanUtils.copyProperties(DegreeEntity, degree);
        return degree;
    }

    @Override
    public boolean deleteDegree(long id) {

        DegreeEntity degree = degreeRepository.findById(id).get();
        degreeRepository. delete(degree);
        return true;
    }

    @Override

    public Degree updateDegree(long id, Degree degree) {

        DegreeEntity degreeEntity = degreeRepository.findById(id).get();
        degreeEntity.setDegree_name(degree.getDegree_name());
        degreeEntity.setDegree_modality(degree.getDegree_modality());
        degreeEntity.setDegree_department(degree.getDegree_department());

        degreeRepository.save(degreeEntity);
        return degree;
    }
}
