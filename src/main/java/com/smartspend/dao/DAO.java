package com.smartspend.dao;


import java.util.List;

/**
 * DAO - data access object
 * this is the interface which all child DAO objects will inherit from
 * @param <T> A generic to be replaced by other data types
 */
public interface DAO<T> {
    /**
     * this is to insert the data into the table
     */
    public void insert(T value);

    /**
     * updates an Existing record in the database
     * @param value the instance of the object
     */
    public void update(T value);
    /**
     * Deletes an Existing record in the database
     * @param value the instance of the object
     */
    public void delete(T value);

    /**
     *  gets the table which represents a data object from the database
     * @return A list representing the entire database
     */
    public List<T> getAll();
    /**
     * gets a single Record from a table
     * @return An instance of the object
     * @param id the id of a record in the table
     */
    public T get(int id);
}
