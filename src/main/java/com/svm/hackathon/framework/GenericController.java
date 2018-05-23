package com.svm.hackathon.framework;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svm.hackathon.config.Roles;
import com.svm.hackathon.model.User;
import com.svm.hackathon.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * 
 * 
 * 
 *         A Generic Rest Controller for the Cook Connect Framework.
 * 
 *         <p>
 *         A Generic setup for a Rest Controller. One of these rest controllers should be implemented for every table on
 *         the database.
 * 
 *         This Controller can be used for basic implementation of CRUD operations, and implementation has two
 *         requirements. Implementation of this Generic allows for access to several endpoints for access to the generic
 *         CRUD methods.
 *         </p>
 *         <p>
 *         <ol>
 *         <li>When a Controller extends the Generic Controller Class, the extension must take a class that extends
 *         GenericEntity.
 * 
 *         Example: public class UserController extends GenericController<User>{}
 * 
 *         In this example, User is the name of the object created from the Hibernate Reverse Engineering.</li>
 *         <li>Implementation of Database access requires that there is a DAO set in the implementation of the
 *         controller. The DAO should be autowired in the implementation and passed into the Generic through
 *         and @PostConstruct init method. This method makes the DAO available to the inherited methods from the super
 *         class.
 * 
 *         Example:
 * 
 * @Autowired UserDAO userDAO
 * 
 * @PostConstruct public void init(){ super.setDAO(userDAO); }</li>
 *                </ol>
 *
 * @param <T>
 *            A class that extends Generic Entity.
 */
public abstract class GenericController<T extends GenericEntity> {

	/**
	 * The DAO that will be passed into the Generic upon instantiation of the bean.
	 */
	public GenericDAO<T> dao;

	/**
	 * The setter for the DAO method that will be called in the inherited class.
	 * 
	 * <p>
	 * Example:
	 * 
	 * @Autowired UserDAO userDAO.
	 * 
	 * @PostConstruct public void init(){ super.setDAO(userDAO); }
	 *                </p>
	 * 
	 * @param dao
	 *            The Autowired data access object.
	 */
	public void setDAO(GenericDAO<T> dao) {
		this.dao = dao;
	}

	/**
	 * Gets all rows from the persisted table on the database.
	 * 
	 * @return A response entity containing all of the rows of the table in a list object and the response status code.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	ResponseEntity<List<T>> getAll() {
		return new ResponseEntity<List<T>>(dao.findAll(), HttpStatus.OK);
	}

	/**
	 * Gets a single object by the id field. The id must be passed in as a url parameter.
	 * 
	 * <p>
	 * Example URL: "/user/id/i".
	 * </p>
	 * 
	 * @param id
	 *            {Integer} The unique identifier for the row on the table.
	 * 
	 * @return A response entity containing the mapped object and the response status code.
	 */
	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	ResponseEntity<T> getById(@PathVariable Integer id) {
		return new ResponseEntity<T>(dao.findById(id), HttpStatus.OK);
	}

	/**
	 * Saves a new row to the database.
	 * 
	 * Will receive the request body in json form and map it to the table's object.
	 * 
	 * @param t
	 *            {T extends GenericEntity} The object to persist on the database.
	 * @return A response entity with true if the object was successfully saved and the response status code.
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json")
	ResponseEntity<T> save(@RequestBody T t, HttpServletRequest request) {
		if (t instanceof User) {
			return new ResponseEntity<T>(dao.save(t), HttpStatus.OK);
		} else {
			String token = request.getHeader("Authorization");
			if (JwtUtil.verifyRole(token, Roles.EDITOR)) {
				return new ResponseEntity<T>(dao.save(t), HttpStatus.OK);
			} else {
				return new ResponseEntity<T>(t, HttpStatus.FORBIDDEN);
			}
		}
	}

	/**
	 * Updates an existing row on the database.
	 * 
	 * Will receive the request body in json form and map it to the table's object.
	 * 
	 * @param t
	 *            {T extends GenericEntity} The object to update on the database.
	 * @return A Response entity containing the updated object and the response status code.
	 */
	@RequestMapping(value = "/", method = RequestMethod.PUT, consumes = "application/json")
	ResponseEntity<T> update(@RequestBody T t, HttpServletRequest request) {
		if (t.getId() != null) {
			if (t instanceof User) {
				return new ResponseEntity<T>(dao.update(t), HttpStatus.OK);
			} else {
				String token = request.getHeader("Authorization");
				if (JwtUtil.verifyRole(token, Roles.EDITOR)) {
					return new ResponseEntity<T>(dao.update(t), HttpStatus.OK);
				} else {
					return new ResponseEntity<T>(t, HttpStatus.FORBIDDEN);
				}
			}
		} else {
			return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Deletes an existing row on the database.
	 * 
	 * Will receive the request body in json form and map it to the table's object.
	 * 
	 * @param t
	 *            t {T extends GenericEntity} The object to delete from the database.
	 * @return A response entity with true if the object was successfully deleted and the response status code.
	 */
	@RequestMapping(value = "/id/{id}", method = RequestMethod.DELETE)
	ResponseEntity<Boolean> delete(@PathVariable Integer id) {
		return new ResponseEntity<Boolean>(dao.delete(id), HttpStatus.OK);
	}
}
