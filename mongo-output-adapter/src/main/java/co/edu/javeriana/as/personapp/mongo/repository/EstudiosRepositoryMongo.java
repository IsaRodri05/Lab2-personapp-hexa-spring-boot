package co.edu.javeriana.as.personapp.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;

@Repository
public interface EstudiosRepositoryMongo extends MongoRepository<EstudiosDocument, String> {
    
    List<EstudiosDocument> findByPrimaryPersona_Id(String personaId);

    List<EstudiosDocument> findByPrimaryProfesion_Id(String profesionId);

    EstudiosDocument findByPrimaryPersona_IdAndPrimaryProfesion_Id(String personaId, String profesionId);
}
