package com.svm.hackathon.framework;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 
 *
 * A generic Data Access Object for the Cook Connect Framework.
 * 
 * <p>
 * 		A Generic setup for a Data Access object. One of these DAOs should be implemented for every table on the database.
 * 		
 * 		This DAO can be used for basic implementation of CRUD operations, and implementation has two requirements.
 * </p>
 * <p>
 * <ol>
 * 		<li>
 * 			When a DAO extends the Generic DAO Class, the extension must take a class that extends GenericEntity.
 * 			
 * 			Example: public class UserDAO extends GenericDAO<User>{}
 * 			
 * 			In this example, User is the name of the object created from the Hibernate Reverse Engineering.
 * 		</li>
 * 		<li>
 *			The Generic Entity Class must be passed into the Generic DAO via a setClass method.
 *			This is used in the queries to call the tables by the name of the object in HQL.		
 * 
 * 			Example:
 * 			
 * 			public class UserDAO extends GenericDAO<User>
 * 
 * 			@PostConstruct
 * 			public void init(){
 * 				super.setClass(User.class);
 * 			}
 * 		</li>
 * </ol>
 *
 * @param <T> {T extends GenericEntity} An object that extends Generic Entity.
 */
@Transactional
@SuppressWarnings("unchecked")
public abstract class GenericDAO<T extends GenericEntity> {

	/**
	 * The class will be stored in the property and the name will be returned from the class getter.
	 */
	private Class<T> persistenceClass;
	
	/**
	 * The entity manager that will handle the HQL queries.
	 */
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * The setter for the persistence class.
	 * 
	 * @param persistenceClass {Class<T extends GenericEntity}
	 */
	public void setClass(Class<T> persistenceClass){
		this.persistenceClass = persistenceClass;
	}
	
	/**
	 * Returns all rows from the table on the database.
	 * 
	 * @return {List<T>} A list of all rows in the table.
	 */
	public List<T> findAll(){
		String queryString = "from " + getPersistenceClass();
		return entityManager.createQuery(queryString).getResultList();
	}
	
	/**
	 * Finds a single row in the table by the id field.
	 * 
	 * @param id {Integer} The id parameter for the table query.
	 * @return {T extends GenericEntity} A Single row from the table.
	 */
	public T findById(Integer id){
		return byId(id);
	}
	
	/**
	 * Persists a new instance of the class {T} to the database.
	 * 
	 * @param t {T extends GenericEntity} A new instance of the class to save.
	 * @return {T extends GenericEntity} The returned instance.
	 */
	public T save(T t){
		entityManager.persist(t);
		T entity = entityManager.merge(t);
		return entity;
	}
	
	/**
	 * Updates an existing instance on the database.
	 * 
	 * @param t {T extends GenericEntity} The instance to update on the database.
	 * @return {T extends GenericEntity} The saved instance.
	 */
	public T update(T t){
		entityManager.merge(t);
		return byId(t.getId());
	}
	
	/**
	 * Deletes an instance from the database.
	 * 
	 * @param t {T extends GenericEntity} The instance to delete.
	 * @return {Boolean} a boolean of whether the instance was deleted or not.
	 */
	public Boolean delete(Integer id){
		T t = findById(id);
		entityManager.remove(entityManager.contains(t) ? t : entityManager.merge(t));
		if(countById(t.getId()) == 0){
			return true;
		} else {
			// TODO: throw error.
			return false;
		}
	}
	
	/**
	 * A private function to find a single row on the database by id.
	 * 
	 * @param id {Integer} The id of the object to use in the query.
	 * @return {T extends GenericEntity} The resulting object.
	 */
	private T byId(Integer id){
		return (T) getIdQuery(id).getSingleResult();
	}
	
	/**
	 * Gets a count of the number of instances on the database with the given id.
	 * 
	 * @param id {Integer} the id to use in the database query.
	 * @return {int} the number of instances with the given id.
	 */
	private int countById(Integer id){
		return getIdQuery(id).getResultList().size();
	}
	
	/**
	 * Returns a query to search the database for an object with the given id.
	 * 
	 * @param id {Integer} The id to use in the database query.
	 * @return {Query} A query object with the input parameters.
	 */
	private Query getIdQuery(Integer id){
		String queryString = "from " + getPersistenceClass() + " where id = :id";
		return entityManager.createQuery(queryString).setParameter("id", id);
	}
	
	/**
	 * Return the name of the persistence class for use in queries.
	 * 
	 * @return {String} the name of the persistence class.
	 */
	private String getPersistenceClass(){
		return persistenceClass.getName();
	}
	
	/**
	 * Returns the entityManager for use in sub-classes.
	 * 
	 * @return {EntityManager} The entityManager for the DAO.
	 */
	public EntityManager getEM(){
		return this.entityManager;
	}
}
