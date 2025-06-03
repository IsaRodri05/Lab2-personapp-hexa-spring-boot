package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.as.personapp.adapter.StudyInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.EditStudyRequest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/studies")
public class StudyControllerV1 {

    @Autowired
    private StudyInputAdapterRest studyAdapter;

    @Operation(summary = "Listar todos los estudios de la base de datos indicada")
    @GetMapping(path = "/{database}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudyResponse>> getAll(
            @PathVariable("database") String database) {
        log.info("Entrando en getAll para DB={}", database);
        List<StudyResponse> lista = studyAdapter.getAllStudies(database.toUpperCase());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Crear un nuevo estudio (la base de datos se indica en el body)")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudyResponse> create(@RequestBody StudyRequest request) {
        log.info("Entrando en create con request.database={}", request.getDatabase());
        StudyResponse resp = studyAdapter.createStudy(request);
        if (resp == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Encontrar un estudio por personId y professionId en la base de datos indicada")
    @GetMapping(path = "/{database}/{personId}/{professionId}", 
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudyResponse> getById(
            @PathVariable("database") String database,
            @PathVariable Integer personId,
            @PathVariable Integer professionId) {

        log.info("Entrando en getById para DB={}, personId={}, professionId={}", 
                 database, personId, professionId);

        StudyResponse resp = studyAdapter.getStudyById(
                personId, professionId, database.toUpperCase());

        if (resp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Actualizar un estudio existente en la base de datos indicada")
    @PutMapping(path = "/{database}/{personId}/{professionId}", 
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudyResponse> update(
            @PathVariable("database") String database,
            @PathVariable Integer personId,
            @PathVariable Integer professionId,
            @RequestBody EditStudyRequest request) {

        log.info("Entrando en update para DB={}, personId={}, professionId={}",
                 database, personId, professionId);

        StudyResponse resp = studyAdapter.updateStudy(
                personId, professionId, request, database.toUpperCase());

        if (resp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Eliminar un estudio por personId y professionId en la base de datos indicada")
    @DeleteMapping(path = "/{database}/{personId}/{professionId}")
    public ResponseEntity<Void> delete(
            @PathVariable("database") String database,
            @PathVariable Integer personId,
            @PathVariable Integer professionId) {

        log.info("Entrando en delete para DB={}, personId={}, professionId={}", 
                 database, personId, professionId);

        boolean deleted = studyAdapter.deleteStudy(
                personId, professionId, database.toUpperCase());

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
