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

/** Module to provide data assertions for {@link Boolean} values. */
public final class RequireBoolean
	extends Require<Boolean, RequireBooleanFaultBuilder, RequireBoolean> {

	RequireBoolean( Boolean actual ) {
		super( actual, new RequireBooleanFaultBuilder( actual ) );
	}

	/**
	 * Will check {@code actual} for {@code true} condition.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is {@code false} or {@code null}
	 */
	public RequireBoolean isTrue() {
		if ( actual == null || ! actual )
			throw fault.isTrue().build();
		return self();
	}

	/**
	 * Will check {@code actual} for {@code false} condition.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is {@code true} or {@code null}
	 */
	public RequireBoolean isFalse() {
		if ( actual == null || actual )
			throw fault.isFalse().build();
		return self();
	}

}
