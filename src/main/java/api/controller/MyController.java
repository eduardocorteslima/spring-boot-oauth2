package api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import api.model.MyEntity;
import api.repository.MyRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author eduardo
 *
 */
@RestController
@RequestMapping({ "/api" })
@Api(value = "Api faz tudo")
public class MyController {

	@Autowired
	private MyRepository appRepository;

	/**
	 * Create
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/entidade", method = RequestMethod.POST)
	public ResponseEntity<MyEntity> create(@RequestBody MyEntity entity) {
		return new ResponseEntity<MyEntity>(appRepository.save(entity), HttpStatus.CREATED);
	}

	/**
	 * Read by id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping({ "/entidade/{id}" })
	public ResponseEntity<MyEntity> getById(@PathVariable("id") String id) {
		MyEntity entity = appRepository.findOne(id);
		return new ResponseEntity<MyEntity>(entity, HttpStatus.OK);
	}

	/**
	 * Read All
	 * 
	 * @return
	 */
	@ApiOperation(value = "Retorna lista de todas entitades", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@RequestMapping({ "/entidades" })
	public ResponseEntity<List<MyEntity>> getAll() {
		return new ResponseEntity<List<MyEntity>>(appRepository.findAll(), HttpStatus.OK);
	}

	/**
	 * Update
	 * 
	 * @param entityParam
	 * @return
	 */
	@RequestMapping(value = "/entidade", method = RequestMethod.PUT)
	public ResponseEntity<MyEntity> update(@RequestBody MyEntity entityParam) {
		MyEntity entity = appRepository.findOne(entityParam.getId());
		entity.setData(entityParam.getData());
		entity.setNome(entityParam.getNome());
		entity.setValor(entityParam.getValor());

		return new ResponseEntity<MyEntity>(appRepository.save(entity), HttpStatus.CREATED);
	}

	/**
	 * Delete
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/entidade/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<MyEntity> delete(@PathVariable("id") String id) {
		appRepository.delete(id);
		return new ResponseEntity<MyEntity>(HttpStatus.OK);
	}

}
