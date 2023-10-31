package com.herbmarshall.require;

/**
 * Module to provide data assertions.
 * @param <T> The type of value to operate on
 */
public final class RequirePointer<T>
	extends Require<T, RequirePointerFaultBuilder<T>, RequirePointer<T>> {

	RequirePointer( T actual ) {
		super( actual, new RequirePointerFaultBuilder<>( actual ) );
	}

	/**
	 * Will check {@code value} for {@code null} condition.
	 * @throws AssertionError if {@code value} IS {@code null}
	 */
	public void isNull() {
		if ( actual != null )
			throw new AssertionError( fault.isNull().getMessage() );
	}

	/**
	 * Will check {@code value} for {@code null} condition.
	 * @throws AssertionError if {@code value} is NOT {@code null}
	 */
	public void isNotNull() {
		if ( actual == null )
			throw new AssertionError( fault.isNotNull().getMessage() );
	}

	/**
	 * Will check that {@code expected} is the same pointer as {@code actual}.
	 * @throws AssertionError if {@code expected} is NOT the same pointer as {@code actual}
	 */
	public void isTheSame( T expected ) {
		if ( actual != expected )
			throw new AssertionError( fault.isTheSame( expected ).getMessage() );
	}

	/**
	 * Will check that {@code expected} is the same pointer as {@code actual}.
	 * @throws AssertionError if {@code expected} IS the same pointer as {@code actual}
	 */
	public void isNotTheSame( T expected ) {
		if ( actual == expected )
			throw new AssertionError( fault.isNotTheSame().getMessage() );
	}

}
