package com.herbmarshall.require;

import java.util.Optional;

/**
 * Module to provide data assertions for {@link Optional} values.
 * @param <T> The {@link Optional} element type
 */
@SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
public final class RequireOptional<T>
	extends Require<Optional<T>, RequireOptionalFaultBuilder<T>, RequireOptional<T>> {

	RequireOptional( Optional<T> actual ) {
		super( actual, new RequireOptionalFaultBuilder<>( actual ) );
	}

	/**
	 * Will check {@code actual} for a value.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is null or empty
	 * @see Optional#isPresent()
	 */
	public RequireOptional<T> isPresent() {
		return Optional.ofNullable( actual )
			.filter( Optional::isPresent )
			.map( actual -> self() )
			.orElseThrow( fault.isPresent()::build );
	}

	/**
	 * Will check to see if {@code actual} is empty.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is null or contains a value
	 * @see Optional#isEmpty()
	 */
	@SuppressWarnings( "OptionalAssignedToNull" )
	public RequireOptional<T> isEmpty() {
		if ( actual == null || actual.isPresent() )
			throw fault.isEmpty().build();
		return self();
	}

	/**
	 * Ensure {@code actual} is not {@code null} and return a {@link RequirePointer} for that value.
	 * <p>Note, while {@code actual} is checked for null, the value inside {@code actual} can still be null</p>
	 * @return A {@link RequirePointer} for the value contained in {@code actual}
	 * @throws AssertionError if {@code actual} is null
	 * @see RequirePointer
	 */
	public RequirePointer<T> value() {
		return Require.that(
			Require.notNull( actual ).orElse( null )
		);
	}

}
