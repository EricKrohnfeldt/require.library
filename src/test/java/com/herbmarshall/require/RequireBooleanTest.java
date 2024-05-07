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

import com.herbmarshall.require.tester.RequireTestBuilder;
import org.junit.jupiter.api.Test;

import java.time.Instant;

final class RequireBooleanTest extends IdentityEqualsRequireTest<Boolean, RequireBooleanFaultBuilder, RequireBoolean> {

	RequireBooleanTest() {
		super( RequireTestBuilder.with(
			Require::that,
			Require::fault
		) );
	}

	@Test
	void isTrue() {
		builder.test( RequireBoolean::isTrue, RequireBooleanFaultBuilder::isTrue )
			.pass( true )
			.fault( false )
			.fault( null );
	}

	@Test
	void isFalse() {
		builder.test( RequireBoolean::isFalse, RequireBooleanFaultBuilder::isFalse )
			.pass( false )
			.fault( true )
			.fault( null );
	}

	@Override
	protected Boolean randomValue() {
		return Instant.now().getNano() % 2 == 0;
	}

}
