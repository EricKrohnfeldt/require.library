package com.herbmarshall.require;

/**
 * Module to provide data assertions.
 * @param <T> The type of value to operate on
 */
public final class Require<T> {

	static final String TODO_ENVIRONMENT_VARIABLE_NAME = "preliminaryTest";
	static final String TODO_ENVIRONMENT_VARIABLE_VALUE = "true";

	private final T actual;

	private final RequireFaultBuilder<T> fault;

	Require( T actual ) {
		this.actual = actual;
		this.fault = Require.fault( actual );
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

	/**
	 * Will check that {@code expected} is equal to {@code actual}.
	 * Based on the {@link Object#equals(Object)} method.
	 * @throws AssertionError if {@code expected} is NOT equal to {@code actual}
	 */
	public void isEqualTo( T expected ) {
		if ( checkEqual( expected ) )
			throw new AssertionError( fault.isEqualTo( expected ).getMessage() );
//		Objects.requireNonNull( message );
//		if ( expected != null && expected.getClass().isArray() )
//			isArrayEqual( message, ( Object[] ) expected, ( Object[] ) actual );
//		else
//			isObjectEqual( message, expected, actual );
	}

//	private void isObjectEqual( String message, T expected, T actual ) {
//		try {
//			Assertions.assertEquals( expected, actual, message );
//		}
//		catch ( AssertionFailedError e ) {
//			printer.println( diffVisualizer.diff( expected, actual ) );
//			throw e;
//		}
//	}
//
//	private void isArrayEqual( String message, Object[] expected, Object[] actual ) {
//		try {
//			Assertions.assertArrayEquals( expected, actual, message );
//		}
//		catch ( AssertionFailedError e ) {
//			printer.println( diffVisualizer.diff( expected, actual ) );
//			throw e;
//		}
//	}

	/**
	 * Will check that {@code expected} is equal to {@code actual}.
	 * Based on the {@link Object#equals(Object)} method.
	 * @throws AssertionError if {@code expected} is equal to {@code actual}
	 */
	public void isNotEqualTo( T expected ) {
		if ( ! checkEqual( expected ) )
			throw new AssertionError( fault.isNotEqualTo( expected ).getMessage() );
	}

	private boolean checkEqual( T expected ) {
		return
			( actual == null ^ expected == null ) ||
			( actual != null && ! actual.equals( expected ) );
	}

	/** Set the displayed error message to the default. */
	public Require<T> withDefaultMessage() {
		fault.withDefaultMessage();
		return this;
	}

	/** Set the displayed error message. */
	public Require<T> withMessage( String message ) {
		fault.withMessage( message );
		return this;
	}

	/**
	 * Create a {@link Require} for specific data.
	 * @param actual The data to evaluate
	 * @return A new {@link Require} instance
	 * @param <T> The type of value to operate on
	 */
	public static <T> Require<T> that( T actual ) {
		return new Require<>( actual );
	}

	/**
	 * Create a {@link RequireFaultBuilder} for specific data.
	 * @param actual The data to evaluate
	 * @return A new {@link RequireFaultBuilder} instance
	 * @param <T> The type of value to operate on
	 */
	public static <T> RequireFaultBuilder<T> fault( T actual ) {
		return new RequireFaultBuilder<>( actual );
	}

	/**
	 * Simply fail.
	 * @throws AssertionError every time
	 */
	public static void fail() {
		throw new AssertionError();
	}

	/**
	 * Fail with message.
	 * @param message The message to use while creating the {@link AssertionError}
	 * @throws AssertionError every time
	 */
	public static void fail( String message ) {
		throw new AssertionError( message );
	}

	/**
	 * Will fail unless environment variable {@value Require#TODO_ENVIRONMENT_VARIABLE_NAME}
	 *  is set to {@value Require#TODO_ENVIRONMENT_VARIABLE_VALUE}.
	 * @throws AssertionError conditionally
	 */
	public static void todo() {
		if ( checkForPreliminaryTestFlag() ) return;
		throw new AssertionError();
	}

	/**
	 * Will fail with message unless environment variable {@value Require#TODO_ENVIRONMENT_VARIABLE_NAME}
	 *  is set to {@value Require#TODO_ENVIRONMENT_VARIABLE_VALUE}.
	 * @param message The message to use while creating the {@link AssertionError}
	 * @throws AssertionError conditionally
	 */
	public static void todo( String message ) {
		if ( checkForPreliminaryTestFlag() ) return;
		throw new AssertionError( message );
	}

	private static boolean checkForPreliminaryTestFlag() {
		return TODO_ENVIRONMENT_VARIABLE_VALUE.equals( System.getProperty( TODO_ENVIRONMENT_VARIABLE_NAME ) );
	}

}
