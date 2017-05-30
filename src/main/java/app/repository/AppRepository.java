package app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import app.model.AppEntity;

public interface AppRepository extends MongoRepository<AppEntity, String>{

}
