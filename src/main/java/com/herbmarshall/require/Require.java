package com.herbmarshall.require;

import com.herbmarshall.javaExtension.SelfTyped;
import com.herbmarshall.standardPipe.Standard;

import java.util.Objects;

/**
 * Module to provide data assertions.
 * @param <T> The type of value to operate on
 * @param <F> The type of {@link RequireFaultBuilder} to operate with
 * @param <SELF> Self type reference
 */
public abstract sealed class Require<T, F extends RequireFaultBuilder<T, F>, SELF extends Require<T, F, SELF>>
	extends SelfTyped<SELF>
	permits RequirePointer {

	static final String SETUP_DIFF_MESSAGE = "No diff generated, please set DiffGenerator";
	private static final DiffGenerator defaultDiffGen = new NoopDiffGenerator( SETUP_DIFF_MESSAGE );
	private static DiffGenerator diffGen = defaultDiffGen;

	static final String TODO_ENVIRONMENT_VARIABLE_NAME = "preliminaryTest";
	static final String TODO_ENVIRONMENT_VARIABLE_VALUE = "true";

	protected final T actual;

	protected final F fault;

	Require( T actual, F fault ) {
		this.actual = actual;
		this.fault = Objects.requireNonNull( fault );
	}

	/**
	 * Will check that {@code expected} is equal to {@code actual}.
	 * Based on the {@link Object#equals(Object)} method.
	 * On failure, {@link DiffGenerator} will generate a diff and print it to {@link Standard#err}
	 * @throws AssertionError if {@code expected} is NOT equal to {@code actual}
	 */
	public void isEqualTo( T expected ) {
		if ( ! checkEqual( expected ) ) {
			Standard.err.println( diffGen.diff( actual, expected ) );
			throw new AssertionError( fault.isEqualTo( expected ).getMessage() );
		}
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
		if ( checkEqual( expected ) )
			throw new AssertionError( fault.isNotEqualTo( expected ).getMessage() );
	}

	private boolean checkEqual( T expected ) {
		return ( actual == null ) == ( expected == null ) &&
			( actual == null || actual.equals( expected ) );
	}

	/** Set the displayed error message to the default. */
	public SELF withDefaultMessage() {
		fault.withDefaultMessage();
		return self();
	}

	/** Set the displayed error message. */
	public SELF withMessage( String message ) {
		fault.withMessage( message );
		return self();
	}

	/**
	 * Create a {@link Require} for specific data.
	 * @param actual The data to evaluate
	 * @return A new {@link Require} instance
	 * @param <T> The type of value to operate on
	 */
	public static <T> RequirePointer<T> that( T actual ) {
		return new RequirePointer<>( actual );
	}

	/**
	 * Create a {@link RequirePointerFaultBuilder} for specific data.
	 * @param actual The data to evaluate
	 * @return A new {@link RequirePointerFaultBuilder} instance
	 * @param <T> The type of value to operate on
	 */
	public static <T> RequirePointerFaultBuilder<T> fault( T actual ) {
		return new RequirePointerFaultBuilder<>( actual );
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

	/**
	 * Set the {@link DiffGenerator} to use when {@link Require#isEqualTo(Object)} fails.
	 * @param diffGenerator The {@link DiffGenerator} to use.  {@link null} value will set to default
	 */
	public static void setDiffGenerator( DiffGenerator diffGenerator ) {
		Require.diffGen = Objects.requireNonNullElse( diffGenerator, defaultDiffGen );
	}

	private static boolean checkForPreliminaryTestFlag() {
		return TODO_ENVIRONMENT_VARIABLE_VALUE.equals( System.getProperty( TODO_ENVIRONMENT_VARIABLE_NAME ) );
	}

}
