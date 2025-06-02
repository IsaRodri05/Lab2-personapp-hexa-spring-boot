package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.as.personapp.adapter.StudyInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.EditStudyRequest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;

/**
 * Controlador REST para CRUD de Study (Estudio).
 */
@RestController
@RequestMapping("/api/studies")
public class StudyController {

    @Autowired
    private StudyInputAdapterRest studyAdapter;

    @PostMapping
    public ResponseEntity<StudyResponse> create(@RequestBody StudyRequest request) {
        StudyResponse resp = studyAdapter.createStudy(request);
        if (resp == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<StudyResponse>> getAll() {
        return ResponseEntity.ok(studyAdapter.getAllStudies());
    }

    @GetMapping("/{personId}/{professionId}")
    public ResponseEntity<StudyResponse> getById(@PathVariable Integer personId,
                                                 @PathVariable Integer professionId) {
        StudyResponse resp = studyAdapter.getStudyById(personId, professionId);
        if (resp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{personId}/{professionId}")
    public ResponseEntity<StudyResponse> update(@PathVariable Integer personId,
                                                @PathVariable Integer professionId,
                                                @RequestBody EditStudyRequest request) {
        StudyResponse resp = studyAdapter.updateStudy(personId, professionId, request);
        if (resp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{personId}/{professionId}")
    public ResponseEntity<Void> delete(@PathVariable Integer personId,
                                       @PathVariable Integer professionId) {
        boolean deleted = studyAdapter.deleteStudy(personId, professionId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
