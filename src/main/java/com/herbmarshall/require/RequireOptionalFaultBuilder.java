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

import com.herbmarshall.fault.Fault;

import java.util.Optional;

/**
 * Module to provide data assertion {@link Fault Faults} for {@link Optional} assertions.
 * @param <T> The {@link Optional} element type
 */
@SuppressWarnings( { "OptionalUsedAsFieldOrParameterType", "OptionalAssignedToNull" } )
public final class RequireOptionalFaultBuilder<T>
	extends RequireFaultBuilder<Optional<T>, RequireOptionalFaultBuilder<T>> {

	static final String IS_PRESENT = "Required Optional to contain value, it is empty";
	static final String IS_PRESENT_NULL = "Required Optional to contain value, however is is null" +
		" ( as opposed to empty )";

	static final String IS_EMPTY_BASIC = "Required Optional to be empty";
	static final String IS_EMPTY = IS_EMPTY_BASIC + ", it contains '%s'";
	static final String IS_EMPTY_NULL = IS_EMPTY_BASIC + ", however it is null";

	static final String CONTAINS = "Require Optional to contain %s, however it is empty";
	static final String CONTAINS_NULL = "Require Optional to contain %s, however it is null";

	RequireOptionalFaultBuilder( Optional<T> actual ) {
		super( actual );
	}

	/** Create a {@link Fault} for {@link RequireOptional#isPresent()}. */
	public Fault<AssertionError> isPresent() {
		return build( actual == null ? IS_PRESENT_NULL : IS_PRESENT );
	}

	/** Create a {@link Fault} for {@link RequireOptional#isEmpty()}. */
	public Fault<AssertionError> isEmpty() {
		return build( actual == null ?
			IS_EMPTY_NULL :
			actual
				.map( IS_EMPTY::formatted )
				.orElse( IS_EMPTY_BASIC )
		);
	}

	/** Create a {@link Fault} for {@link RequireOptional#contains(Object)}. */
	public Fault<AssertionError> contains( T expected ) {
		return build( ( actual == null ? CONTAINS_NULL : CONTAINS ).formatted( expected ) );
	}

}
