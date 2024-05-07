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

/** Module to provide data assertion {@link Fault Faults} for {@link Boolean} assertions. */
public final class RequireBooleanFaultBuilder extends RequireFaultBuilder<Boolean, RequireBooleanFaultBuilder> {

	static final String TRUE_MESSAGE_TEMPLATE = "Required True, but found %s";
	static final String FALSE_MESSAGE_TEMPLATE = "Required False, but found %s";

	RequireBooleanFaultBuilder( Boolean actual ) {
		super( actual );
	}

	/** Create a {@link Fault} for {@link RequireBoolean#isTrue()}. */
	public Fault<AssertionError> isTrue() {
		return build( TRUE_MESSAGE_TEMPLATE.formatted( actual ) );
	}

	/** Create a {@link Fault} for {@link RequireBoolean#isFalse()}. */
	public Fault<AssertionError> isFalse() {
		return build( FALSE_MESSAGE_TEMPLATE.formatted( actual ) );
	}

}
