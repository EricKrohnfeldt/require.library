/*
 * This file is part of herbmarshall.com: require.library  ( hereinafter "require.library" ).
 *
 * require.library is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either version 2 of the License,
 * or (at your option) any later version.
 *
 * require.library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with require.library.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.herbmarshall.require;

import com.herbmarshall.base.Equals;
import com.herbmarshall.base.SelfTyped;
import com.herbmarshall.base.diff.DiffVisualizer;
import com.herbmarshall.fault.Fault;
import com.herbmarshall.standardPipe.Standard;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Module to provide data assertions.
 * @param <T> The type of value to operate on
 * @param <F> The type of {@link RequireFaultBuilder} to operate with
 * @param <SELF> Self type reference
 */
public abstract sealed class Require<T, F extends RequireFaultBuilder<T, F>, SELF extends Require<T, F, SELF>>
	extends SelfTyped<SELF>
	permits
		RequireBoolean,
		RequirePointer,
		RequireOptional,
		RequireCollection,
		RequireStream {

	static final String TODO_ENVIRONMENT_VARIABLE_NAME = "preliminaryTest";
	static final String TODO_ENVIRONMENT_VARIABLE_VALUE = "true";

	protected final T actual;

	protected final F fault;

	Require( T actual, F fault ) {
		this.actual = actual;
		this.fault = Objects.requireNonNull( fault );
	}

	/**
	 * Will check {@code actual} for {@code null} condition.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} IS {@code null}
	 */
	public final SELF isNull() {
		if ( actual != null )
			throw fault.isNull().build();
		return self();
	}

	/**
	 * Will check {@code actual} for {@code null} condition.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is NOT {@code null}
	 */
	public final SELF isNotNull() {
		if ( actual == null )
			throw fault.isNotNull().build();
		return self();
	}

	/**
	 * Will check that {@code expected} is the same pointer as {@code actual}.
	 * @return A self reference
	 * @throws AssertionError if {@code expected} is NOT the same pointer as {@code actual}
	 */
	public final SELF isTheSameAs( T expected ) {
		if ( actual != expected )
			throw fault.isTheSameAs( expected ).build();
		return self();
	}

	/**
	 * Will check that {@code expected} is the same pointer as {@code actual}.
	 * @return A self reference
	 * @throws AssertionError if {@code expected} IS the same pointer as {@code actual}
	 */
	public final SELF isNotTheSameAs( T expected ) {
		if ( actual == expected )
			throw fault.isNotTheSameAs().build();
		return self();
	}

	/**
	 * Will check that {@code expected} is equal to {@code actual}.
	 * Based on the {@link Object#equals(Object)} method.
	 * On failure, {@link DiffVisualizer} will generate a diff and print it to {@link Standard#err}
	 * @return A self reference
	 * @throws AssertionError if {@code expected} is NOT equal to {@code actual}
	 */
	public final SELF isEqualTo( T expected ) {
		if ( ! Equals.evaluate( expected, actual ) ) {
			Standard.err.println( DiffVisualizer.generate( expected, actual ) );
			throw fault.isEqualTo( expected ).build();
		}
		return self();
	}

	/**
	 * Will check that {@code expected} is equal to {@code actual}.
	 * Based on the {@link Object#equals(Object)} method.
	 * @return A self reference
	 * @throws AssertionError if {@code expected} is equal to {@code actual}
	 */
	public final SELF isNotEqualTo( T expected ) {
		if ( Equals.evaluate( expected, actual ) )
			throw fault.isNotEqualTo( expected ).build();
		return self();
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

	/** @return The {@code actual} pointer */
	public T done() {
		return actual;
	}

	/**
	 * For fast in-line null checking.
	 * @param value The data to evaluate
	 * @return value reference
	 * @param <T> The type of value
	 */
	public static <T> T notNull( T value ) {
		return Require.that( value ).isNotNull().done();
	}

	/**
	 * For fast in-line null check {@link Fault}.
	 * @return An {@link AssertionError} {@link Fault}
	 */
	public static Fault<AssertionError> notNullFault() {
		return Require.fault( ( Object ) null ).isNotNull();
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
	 * Create a {@link Require} for specific data.
	 * @param actual The data to evaluate
	 * @return A new {@link RequirePointer} instance
	 * @param <T> The type of value to operate on
	 */
	public static <T> RequirePointer<T> that( T actual ) {
		return new RequirePointer<>( actual );
	}

	/**
	 * Create a {@link Require} for specific {@link Optional} data.
	 * @param actual The {@link Optional} to evaluate
	 * @return A new {@link RequireOptional} instance
	 * @param <E> The type of element stored in the {@link Optional}
	 */
	@SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
	public static <E> RequireOptional<E> that( Optional<E> actual ) {
		return new RequireOptional<>( actual );
	}

	/**
	 * Create a {@link Require} for specific {@link List} data.
	 * @param actual The {@link List} to evaluate
	 * @return A new {@link RequireList} instance
	 * @param <E> The type of element stored in the {@link List}
	 */
	public static <E> RequireList<E> that( List<E> actual ) {
		return new RequireList<>( actual );
	}

	/**
	 * Create a {@link Require} for specific {@link Set} data.
	 * @param actual The {@link Set} to evaluate
	 * @return A new {@link RequireSet} instance
	 * @param <E> The type of element stored in the {@link Set}
	 */
	public static <E> RequireSet<E> that( Set<E> actual ) {
		return new RequireSet<>( actual );
	}

	/**
	 * Create a {@link Require} for specific {@link Stream} of data.
	 * @param actual The {@link Stream} to evaluate
	 * @return A new {@link RequireStream} instance
	 * @param <E> The type of element stored in the {@link Stream}
	 */
	public static <E> RequireStream<E> that( Stream<E> actual ) {
		return new RequireStream<>( actual );
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
	 * Create a {@link RequirePointerFaultBuilder} for specific data.
	 * @param actual The data to evaluate
	 * @return A new {@link RequirePointerFaultBuilder} instance
	 * @param <T> The type of value to operate on
	 */
	public static <T> RequirePointerFaultBuilder<T> fault( T actual ) {
		return new RequirePointerFaultBuilder<>( actual );
	}

	/**
	 * Create a {@link RequireOptionalFaultBuilder} for specific {@link Optional} data.
	 * @param actual The {@link Optional} to evaluate
	 * @return A new {@link RequireOptionalFaultBuilder} instance
	 * @param <E> The type of element stored in the {@link Optional}
	 */
	@SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
	public static <E> RequireOptionalFaultBuilder<E> fault( Optional<E> actual ) {
		return new RequireOptionalFaultBuilder<>( actual );
	}

	/**
	 * Create a {@link RequireListFaultBuilder} for specific {@link List} data.
	 * @param actual The {@link List} to evaluate
	 * @return A new {@link RequireListFaultBuilder} instance
	 * @param <E> The type of element stored in the {@link List}
	 */
	public static <E> RequireListFaultBuilder<E> fault( List<E> actual ) {
		return new RequireListFaultBuilder<>( actual );
	}

	/**
	 * Create a {@link RequireSetFaultBuilder} for specific {@link Set} data.
	 * @param actual The {@link Set} to evaluate
	 * @return A new {@link RequireSetFaultBuilder} instance
	 * @param <E> The type of element stored in the {@link Set}
	 */
	public static <E> RequireSetFaultBuilder<E> fault( Set<E> actual ) {
		return new RequireSetFaultBuilder<>( actual );
	}

	/**
	 * Create a {@link RequireStreamFaultBuilder} for specific {@link Stream} of data.
	 * @param actual The {@link Stream} to evaluate
	 * @return A new {@link RequireStreamFaultBuilder} instance
	 * @param <E> The type of element stored in the {@link Stream}
	 */
	public static <E> RequireStreamFaultBuilder<E> fault( Stream<E> actual ) {
		return new RequireStreamFaultBuilder<>( actual );
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
	 * <p>
	 *     In IntelliJ, go to Run -> Edit Configurations -> Edit Configuration Templates -> Junit.
	 *     Then add '{@value Require#TODO_ENVIRONMENT_VARIABLE_NAME}={@value Require#TODO_ENVIRONMENT_VARIABLE_VALUE}'
	 *     to the VM Options, not environment variables.
	 * </p>
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
	 * @param diffGenerator The {@link DiffGenerator} to use.  {@code null} value will set to default
	 * @deprecated <a href="https://herbmarshall.atlassian.net/browse/UTIL-350">UTIL-350</a>
	 *  Please use {@link DiffVisualizer#setGenerator(com.herbmarshall.base.diff.DiffGenerator)}
	 * @see DiffVisualizer#setGenerator(com.herbmarshall.base.diff.DiffGenerator)
	 */
	@SuppressWarnings( { "removal", "DeprecatedIsStillUsed" } )
	@Deprecated( since = "1.14", forRemoval = true )
	public static void setDiffGenerator( DiffGenerator diffGenerator ) {
		DiffVisualizer.setGenerator(
			diffGenerator == null ? null : ( expected, actual ) -> diffGenerator.diff( actual, expected ) );
	}

	private static boolean checkForPreliminaryTestFlag() {
		return TODO_ENVIRONMENT_VARIABLE_VALUE.equals( System.getProperty( TODO_ENVIRONMENT_VARIABLE_NAME ) );
	}

}
