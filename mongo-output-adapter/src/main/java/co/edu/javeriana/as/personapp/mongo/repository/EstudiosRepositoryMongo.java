package co.edu.javeriana.as.personapp.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;

@Repository
public interface EstudiosRepositoryMongo extends MongoRepository<EstudiosDocument, String> {
    
    List<EstudiosDocument> findByPersona_Id(String personaId);

    List<EstudiosDocument> findByProfesion_Id(String profesionId);

    EstudiosDocument findByPersona_IdAndProfesion_Id(String personaId, String profesionId);
}
