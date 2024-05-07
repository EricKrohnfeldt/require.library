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

import java.util.Objects;
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
	 * Checks if {@code actual} contains a value equal to {@code expected}.
	 * @param expected The value to check for equality.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is null or {@code expected} is not equal to contained value
	 */
	@SuppressWarnings( "OptionalAssignedToNull" )
	public RequireOptional<T> contains( T expected ) {
		if ( actual != null && Objects.equals(
			actual.orElse( null ),
			expected
		) )
			return self();
		throw fault.contains( expected ).build();
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
