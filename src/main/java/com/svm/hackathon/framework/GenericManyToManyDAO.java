package com.svm.hackathon.framework;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 
 *
 * A Generic Many To Many Data Access object for use in the Cook Connect Framework.
 * 
 * <p>
 * 		A Generic setup for a Data Access object used to access joined entities. One of these DAOs should be implemented for every junction table on the database.
 * 		
 * 		This DAO can be used for basic implementation of CRUD operations, and implementation has two requirements.
 * </p>
 * <ol>
 * 		<li>
 * 			When a DAO extends the Generic Many To Many DAO Class, the extension must take one class that extends GenericManyToManyEntity and two that extend GenericEntity.
 * 			
 * 			Example: public class UserDAO extends GenericManyToManyDAO<UserSkill, User, Skill>{}
 * 			
 * 			In this example, UserSkill, User, and Skill are the names of objects created from the Hibernate Reverse Engineering.
 * 		</li>
 * 		<li>
 *			The Generic Many To Many Entity Class and the Generic Entity Classes must be passed into the Generic DAO via a setClasses method, and the names of the query fields must be passed into a setIdFields method.
 *			This is used in the queries to call the tables by the name of the object in HQL.		
 * 
 * 			Example:
 * 			
 * 			public class UserSkillDAO extends GenericManyToManyDAO<UserSkill, User, Skill>
 * 
 * 			@PostConstruct
 * 			public void init(){
 * 				super.setClasses(UserSkill.class, User.class, Skill.class);
 * 				super.setIdFields("UserSkill", "user", "skill");
 * 			}
 * 		</li>
 * </ol>
 *
 * @param <T1> {T1 extends GenericManyToManyEntity} The join entity from the hibernate reverse engineering.
 * @param <T2> {T2 extends GenericEntity} The left field on the table.
 * @param <T3> {T3 extends GenericEntity} The right field on the table.
 */
@Transactional
@SuppressWarnings("unchecked")
public abstract class GenericManyToManyDAO<T1 extends GenericManyToManyEntity, T2 extends GenericEntity, T3 extends GenericEntity> {

	/**
	 * The class entities that will be referenced throughout the DAO.
	 */
	private Class<T1> joinEntity;
	private Class<T2> leftEntity;
	private Class<T3> rightEntity;

	/**
	 * The names of the class fields for use in queries.
	 */
	private String joinClassName;
	private String leftField;
	private String rightField;

	/**
	 * The connection object for accessing the database.
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Maps the left field for the join class to the right field of the join class.
	 * 
	 * @param leftId {Integer} The id of the object to map the list of objects to.
	 * @param rightIds {List<Integer>} A list of ids from the right Entity table.
	 * @return {List<T3 extends GenericEntity} A list of the right entities that have been mapped to the left entity.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected List<T3> mapPairLeftToRight(Integer leftId, List<Integer> rightIds)
			throws InstantiationException, IllegalAccessException {
		
		// Gets an initial count of the objects from the table.
		Long initialCount = countTableSize(getJoinClass());
		
		// Gets the object to which the right entities will be mapped.
		T2 t = (T2) entityManager.find(leftEntity, leftId);
		
		// Queries the database for a list of right entities.
		String queryString = getManyToManyQueryString(getRightClass(), rightIds.size());
		List<T3> listOfRightEntities = (List<T3>) executeQuery(queryString, rightIds);
		
		// Persists junction objects to the database.
		for (T3 entity : listOfRightEntities) {
			T1 newJoin = joinEntity.newInstance();
			newJoin.sleftEntity(t);
			newJoin.srightEntity(entity);
			entityManager.persist(newJoin);
		}
		
		// Makes sure that the objects have been saved by checking size of the table after the query.
		if (initialCount + rightIds.size() == countTableSize(getJoinClass())) {
			return listOfRightEntities;
			
		// If the objects have not been saved, will throw an error.
		} else {

			// TODO: throw and error here.
			return null;
		}

	}

	/**
	 * Maps the right field for the join class to the left field of the join class.
	 * 
	 * @param leftId {Integer} The id of the object to map the list of objects to.
	 * @param rightIds {List<Integer>} A list of ids from the left Entity table.
	 * @return {List<T2 extends GenericEntity} A list of the left entities that have been mapped to the left entity.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected List<T2> mapPairRightToLeft(Integer rightId, List<Integer> leftIds)
			throws InstantiationException, IllegalAccessException {
		
		// Gets an initial count from the table for comparison after the save.
		Long initialCount = countTableSize(getJoinClass());
		
		// Gets the necessary instance of the left entity.
		T2 t = (T2) entityManager.find(leftEntity, rightId);
		
		// Queries the database for a list of right entities.
		String queryString = getManyToManyQueryString(getRightClass(), leftIds.size());
		List<T2> listOfLeftEntities = (List<T2>) executeQuery(queryString, leftIds);
		
		// Persists the junction objects.
		for (T2 entity : listOfLeftEntities) {
			T1 newJoin = joinEntity.newInstance();
			newJoin.sleftEntity(t);
			newJoin.srightEntity(entity);
			entityManager.persist(newJoin);
		}
		
		// Checks the count after the save to make sure that right number of objects were persisted.
		if (initialCount + leftIds.size() == countTableSize(getJoinClass())) {
			return listOfLeftEntities;
			
		// If the right number of objects was not persisted, will throw an error.
		} else {

			// TODO: throw and error here.
			return null;
		}
	}

	/**
	 * Gets a list of left entities based on an id input from a right entity.
	 * 
	 * @param rightId {Integer} The id of the right entity to search by.
	 * @return {List<T2 extends GenericEntity>} A list of left entities to return.
	 */
	protected List<T2> getLeftByRight(Integer rightId) {
		List<T1> joinEntities = (List<T1>)entityManager.createQuery("from " + joinClassName + " where " + rightField + ".id = :id").setParameter("id", rightId).getResultList();
		List<T2> leftEntityList = new ArrayList<T2>();
		for(T1 joinEntity: joinEntities){
			leftEntityList.add((T2) joinEntity.gleftEntity());
		}
		return leftEntityList;
	}

	/**
	 * Gets a list of right entities based on an id input from a left entity.
	 * 
	 * @param leftId {Integer} The id of the left entity to search by.
	 * @return {List<T3 extends GenericEntity>} A list of right entities to return.
	 */
	protected List<T3> getRightByLeft(Integer leftId) {
		List<T1> joinEntities = (List<T1>)entityManager.createQuery("from " + joinClassName + " where " + leftField + ".id = :id").setParameter("id", leftId).getResultList();
		List<T3> rightEntityList = new ArrayList<T3>();
		for(T1 joinEntity: joinEntities){
			rightEntityList.add((T3) joinEntity.grightEntity());
		}
		return rightEntityList;
	}
	
	/**
	 * Removes an individual mapping from a many to many table.
	 * 
	 * @param leftId {Integer} the id of the left column to unmap.
	 * @param rightId {Integer} the id of the right column to unmap.
	 * 
	 * @return {Boolean} true if the unmap was successful and false if the deletion failed.
	 */
	protected Boolean deleteManyToManyMapping(Integer leftId, Integer rightId){
		Long initialCount = countTableSize(joinClassName);
		Query query = entityManager.createQuery("from " + joinClassName + " where " + leftField + ".id = :leftId and " + rightField + ".id = :rightId");
		query.setParameter("leftId", leftId).setParameter("rightId", rightId);
		if(query.getResultList().size() == 1){
			T1 t = (T1)query.getSingleResult();
			entityManager.remove(t);
			return initialCount - 1 == countTableSize(joinClassName);
		} else{
			throw new Error("Unable to delete mapping because the mapping did not exist.");
		}
		
	}
	
	/**
	 * Counts the size of a table based on the input table size.
	 * 
	 * @param tableName {String} The name of the table to count.
	 * @return {Long} The number of rows on the input table.
	 */
	private Long countTableSize(String tableName) {
		return (Long) entityManager.createQuery("select count(*) from " + tableName).getSingleResult();
	}

	/**
	 * Gets a query string for pulling a list of entities based on multiple input ids.
	 * 
	 * @param tableName {String} The name of the table to query.
	 * @param listSize {int} The number of items in the list that will be queried.
	 * @return {String} An HQL query string for pulling a number of items on the table based on their ids.
	 */
	protected String getManyToManyQueryString(String tableName, int listSize) {
		String queryString = "from " + tableName + " where ";
		/**
		 * Adds a series of id options onto the query string separated by 'or'.
		 * 
		 * Will not add an 'or' if there is only one item in the query.
		 */
		for (int i = 0; i < listSize; i++) {
			if (i != 0) {
				queryString += " or ";
			}
			queryString += "id = :id" + i;
		}
		return queryString;
	}

	/**
	 * Executes a query based on an input query string and a list of mapped field ids.
	 * 
	 * @param queryString {String} The query string to use to instantiate the query object.
	 * @param mappedFieldIds {List<Integer>} A list of all the ids to search for on the database.
	 * @return {List<?>} A generic list that will be case for the return.
	 */
	protected List<?> executeQuery(String queryString, List<Integer> mappedFieldIds) {
		Query query = entityManager.createQuery(queryString);
		for (int i = 0; i < mappedFieldIds.size(); i++) {
			query.setParameter("id" + i, mappedFieldIds.get(i));
		}
		return query.getResultList();
	}

	/**
	 * A set function to initialize the necessary entities for use in the class.
	 * 
	 * @param t1
	 * @param t2
	 * @param t3
	 */
	protected void setClasses(Class t1, Class t2, Class t3) {
		this.joinEntity = t1;
		this.leftEntity = t2;
		this.rightEntity = t3;
	}

	/**
	 * Sets field names for use in queries.
	 * 
	 * @param joinId
	 * @param leftId
	 * @param rightId
	 */
	protected void setIdFields(String joinId, String leftId, String rightId) {
		this.joinClassName = joinId;
		this.leftField = leftId;
		this.rightField = rightId;
	}

	private String getJoinClass() {
		return joinEntity.getName();
	}

	private String getLeftClass() {
		return leftEntity.getName();
	}

	private String getRightClass() {
		return rightEntity.getName();
	}

	/**
	 * Returns the entity manager for use in sub-classes.
	 * 
	 * @return {EntityManager}
	 */
	protected EntityManager getEM() {
		return this.entityManager;
	}
}
