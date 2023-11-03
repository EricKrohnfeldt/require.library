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
	permits RequirePointer, RequireBoolean {

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
	 * Will check {@code value} for {@code null} condition.
	 * @throws AssertionError if {@code value} IS {@code null}
	 */
	public final void isNull() {
		if ( actual != null )
			throw new AssertionError( fault.isNull().getMessage() );
	}

	/**
	 * Will check {@code value} for {@code null} condition.
	 * @throws AssertionError if {@code value} is NOT {@code null}
	 */
	public final void isNotNull() {
		if ( actual == null )
			throw new AssertionError( fault.isNotNull().getMessage() );
	}

	/**
	 * Will check that {@code expected} is the same pointer as {@code actual}.
	 * @throws AssertionError if {@code expected} is NOT the same pointer as {@code actual}
	 */
	public final void isTheSame( T expected ) {
		if ( actual != expected )
			throw new AssertionError( fault.isTheSame( expected ).getMessage() );
	}

	/**
	 * Will check that {@code expected} is the same pointer as {@code actual}.
	 * @throws AssertionError if {@code expected} IS the same pointer as {@code actual}
	 */
	public final void isNotTheSame( T expected ) {
		if ( actual == expected )
			throw new AssertionError( fault.isNotTheSame().getMessage() );
	}

	/**
	 * Will check that {@code expected} is equal to {@code actual}.
	 * Based on the {@link Object#equals(Object)} method.
	 * On failure, {@link DiffGenerator} will generate a diff and print it to {@link Standard#err}
	 * @throws AssertionError if {@code expected} is NOT equal to {@code actual}
	 */
	public final void isEqualTo( T expected ) {
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
	public final void isNotEqualTo( T expected ) {
		if ( checkEqual( expected ) )
			throw new AssertionError( fault.isNotEqualTo( expected ).getMessage() );
	}

	private boolean checkEqual( T expected ) {
		return ( actual == null ) == ( expected == null ) &&
			( actual == null || actual.equals( expected ) );
	}

	/** Set the displayed error message to the default. */
	public final SELF withDefaultMessage() {
		fault.withDefaultMessage();
		return self();
	}

	/** Set the displayed error message. */
	public final SELF withMessage( String message ) {
		fault.withMessage( message );
		return self();
	}

	/**
	 * Create a {@link Require} for specific data.
	 * @param actual The data to evaluate
	 * @return A new {@link RequirePointer} instance
	 * @param <T> The type of value to operate on
	 */
	public static <T> RequirePointer<T> that( T actual ) {
		return new RequirePointer<>( actual );
	}

	/**
	 * Create a {@link Require} for specific {@link Boolean} data.
	 * @param actual The {@link Boolean} to evaluate
	 * @return A new {@link RequireBoolean} instance
	 */
	public static RequireBoolean that( Boolean actual ) {
		return new RequireBoolean( actual );
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
	 * Create a {@link RequireBooleanFaultBuilder} for specific {@link Boolean} data.
	 * @param actual The {@link Boolean} to evaluate
	 * @return A new {@link RequirePointerFaultBuilder} instance
	 */
	public static RequireBooleanFaultBuilder fault( Boolean actual ) {
		return new RequireBooleanFaultBuilder( actual );
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
