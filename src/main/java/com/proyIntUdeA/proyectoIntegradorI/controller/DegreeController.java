package com.proyIntUdeA.proyectoIntegradorI.controller;

import com.proyIntUdeA.proyectoIntegradorI.model.Degree;
import com.proyIntUdeA.proyectoIntegradorI.service.DegreeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proyIntUdeA.proyectoIntegradorI.Jwt.JwtService;

@RestController
@RequestMapping("/api/v1/")
public class DegreeController {
    private DegreeService degreeService;
    private JwtService jwtService;

    public DegreeController(JwtService jwtService, DegreeService degreeService) {
        this.jwtService = jwtService;
        this.degreeService = degreeService;
    }

    @PostMapping("/degree")
    public ResponseEntity<?> saveDegree(@RequestBody Degree degree, HttpServletRequest request) {
        ResponseEntity<String> tokenVerification = jwtService.verifyToken(request);

        if (tokenVerification.getStatusCode() != HttpStatus.OK) {
            return tokenVerification;
        }

        String token = request.getHeader("Authorization").substring(7);
        String user_role = jwtService.getClaim(token, claims -> claims.get("user_role", String.class)).toLowerCase();

        if(!user_role.equalsIgnoreCase("ROLE_ADMIN")){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Debes ser usuario administrador");
        }

        Degree response = degreeService.saveDegree(degree);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/degree")
    public List<Degree> getAllDegrees() {
        return degreeService.getAllDegree();
    }

    @GetMapping("/degree/{id}")
    public ResponseEntity<Degree> getDegreeById(@PathVariable("id") long id) {

        Degree degree = null;
        degree = degreeService.getDegreeById(id);
        return ResponseEntity.ok(degree);
    }

    @DeleteMapping("degree/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteDegree(@PathVariable("id") long id) {

        boolean deleted = false;
        deleted = degreeService.deleteDegree(id);
        Map<String, Boolean> response= new HashMap<>();
        response.put("deleted", deleted);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/degree/{id}")
    public ResponseEntity<Degree> updateDegree(@PathVariable("id") long id, @RequestBody Degree degree) {
        degree = degreeService.updateDegree(id, degree);
        return ResponseEntity.ok(degree);
    }
}
