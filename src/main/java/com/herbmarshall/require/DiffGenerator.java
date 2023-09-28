package com.herbmarshall.require;

/** Module to generate a diff visualization of two objects. */
public interface DiffGenerator {

	/**
	 * Create a text representation of an object diff.
	 * @implNote Must handle null values
	 */
	String diff( Object actual, Object expected );

}
