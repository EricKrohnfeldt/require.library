package com.herbmarshall.require;

/** Module to generate a diff visualization of two objects. */
public interface DiffGenerator {

	/**
	 * Create a text representation of an object diff.
	 * <b>Must handle null values for both {@code actual} and {@code expected}</b>
	 * @param actual The value to evaluate
	 * @param expected The value to compare to
	 */
	String diff( Object actual, Object expected );

}
