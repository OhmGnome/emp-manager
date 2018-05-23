package com.svm.hackathon.framework;

/**
 * 
 * 
 * 
 * A Generic Class to use in the Cook Connect Framework.
 * 
 * Any Generic Many To Many Table entities will extend this class for use with table access at the data layer.
 * @param <T>
 *
 */
public interface GenericManyToManyEntity<T1 extends GenericEntity, T2 extends GenericEntity> {

	public void sleftEntity(T1 t);
	public void srightEntity(T2 t);
	public T1 gleftEntity();
	public T2 grightEntity();
	
}
