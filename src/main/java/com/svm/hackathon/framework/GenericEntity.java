package com.svm.hackathon.framework;

/**
 * 
 * 
 *
 * A Generic Class to use in the Cook Connect Framework.
 * 
 * Any Generic Table entities will extend this class for use with table access at the data layer.
 *
 */
public interface GenericEntity {

	/**
	 * Will return the ID field of the object that extends this class.
	 * 
	 * <p>
	 * 		This method is used in the GenericDAO class for access to the field in Hibernate Queries.
	 * </p>
	 * 
	 * @return The unique identifier of the object.
	 */
	public Integer getId();
	
}
