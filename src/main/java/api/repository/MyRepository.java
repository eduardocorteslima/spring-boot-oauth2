package api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import api.model.MyEntity;

public interface MyRepository extends MongoRepository<MyEntity, String>{

}
