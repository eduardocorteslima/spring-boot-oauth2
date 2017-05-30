package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.AppEntity;
import app.repository.AppRepository;

/**
 * 
 * @author eduardo
 *
 */
@RestController
@RequestMapping({ "/application" })
public class AppController {

	@Autowired
	private AppRepository appRepository;

	/**
	 * Create
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/entidade", method = RequestMethod.POST)
	public ResponseEntity<AppEntity> create(@RequestBody AppEntity entity) {
		return new ResponseEntity<AppEntity>(appRepository.save(entity), HttpStatus.CREATED);
	}

	/**
	 * Read by id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping({ "/entidade/{id}" })
	public ResponseEntity<AppEntity> getById(@PathVariable("id") String id) {
		AppEntity entity = appRepository.findOne(id);
		return new ResponseEntity<AppEntity>(entity, HttpStatus.OK);
	}

	/**
	 * Read All
	 * 
	 * @return
	 */
	@RequestMapping({ "/entidades" })
	public ResponseEntity<List<AppEntity>> getAll() {
		return new ResponseEntity<List<AppEntity>>(appRepository.findAll(), HttpStatus.OK);
	}

	/**
	 * Update
	 * 
	 * @param entityParam
	 * @return
	 */
	@RequestMapping(value = "/entidade", method = RequestMethod.PUT)
	public ResponseEntity<AppEntity> update(@RequestBody AppEntity entityParam) {
		AppEntity entity = appRepository.findOne(entityParam.getId());
		entity.setData(entityParam.getData());
		entity.setNome(entityParam.getNome());
		entity.setValor(entityParam.getValor());

		return new ResponseEntity<AppEntity>(appRepository.save(entity), HttpStatus.CREATED);
	}

	/**
	 * Delete
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/entidade/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<AppEntity> delete(@PathVariable("id") String id) {
		appRepository.delete(id);
		return new ResponseEntity<AppEntity>(HttpStatus.OK);
	}

}
