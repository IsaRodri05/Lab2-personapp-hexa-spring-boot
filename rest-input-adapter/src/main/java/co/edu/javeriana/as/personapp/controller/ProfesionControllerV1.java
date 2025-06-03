package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profesiones")
public class ProfesionControllerV1 {

    @Autowired
    private ProfesionInputAdapterRest profesionInputAdapterRest;
    
    @Operation(summary = "List all professions")
	@GetMapping(path = "/{database}/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProfesionResponse> listarProfesiones(@PathVariable String database) {
		log.info("Into personas REST API");
			return profesionInputAdapterRest.historial(database.toUpperCase());
	}

    @Operation(summary = "Create a new profession")
    @PostMapping
    public ProfesionResponse crearProfesion(@RequestBody ProfesionRequest request) {
        log.info("esta en el metodo crearProfesion en el controller del api");
        return profesionInputAdapterRest.crearProfesion(request);
    }

    @Operation(summary = "Update an existing profession")
    @PutMapping(path="/{database}/edit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProfesionResponse actualizarProfesion(@PathVariable String database, @RequestBody ProfesionRequest request) {
        log.info("esta en el metodo actualizarProfesion en el controller del api");
        return profesionInputAdapterRest.editarProfesion(request);
    }

    @Operation(summary = "Delete a profession")
    @DeleteMapping(path="/{database}/{identificacion}", produces = MediaType.APPLICATION_JSON_VALUE)    
    public Boolean eliminarProfesion(@PathVariable String identificacion, @PathVariable String database) {
        log.info("esta en el metodo eliminarProfesion en el controller del api");
        return profesionInputAdapterRest.eliminarProfesion(Integer.valueOf(identificacion), database);
    }

    @Operation(summary = "Find a profession by identification")
    @GetMapping(path="/{database}/{identificacion}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfesionResponse buscarProfesion(@PathVariable String identificacion, @PathVariable String database) {
        log.info("esta en el metodo buscarProfesion en el controller del api");
        return profesionInputAdapterRest.buscarUnaProfesion(Integer.valueOf(identificacion), database);
    }
}
