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

/**
 * {@link IdentityEqualsRequireTest} should be used with objects that
 *  do <b>not</b> override {@link Object#equals(Object)} and boxed primitives.
 * @param <T> The type of value
 * @param <F> The {@link RequireFaultBuilder} type
 * @param <R> The {@link Require} type
 * @see RequireTest
 */
abstract non-sealed class IdentityEqualsRequireTest<T, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>>
	extends BaseRequireTest<T, F, R> {

	IdentityEqualsRequireTest( RequireTestBuilder<T, F, R> builder ) {
		super( builder );
	}

	@Test
	@Override
	final void isTheSameAs() {
		var tester = builder.<T>test( Require::isTheSameAs, RequireFaultBuilder::isTheSameAs )
			.pass( null, null )
			.fault( null, randomValue() )
			.fault( randomValue(), null );

		T actual = randomValue();
		tester.pass( actual, actual );
	}

	@Test
	@Override
	final void isNotTheSameAs() {
		var tester = builder.<T>test(
				Require::isNotTheSameAs,
				( faultBuilder, expected ) -> faultBuilder.isNotTheSameAs()
			)
			.pass( null, randomValue() )
			.pass( randomValue(), null )
			.fault( null, null );

		T actual = randomValue();
		tester.fault( actual, actual );
	}

	@Test
	@Override
	final void isEqualTo() {
		var tester = builder.<T>test( Require::isEqualTo, RequireFaultBuilder::isEqualTo )
			.pass( null, null )
			.fault( null, randomValue() )
			.fault( randomValue(), null );

		T actual = randomValue();
		tester.pass( actual, actual );

		actual = randomValue();
		tester.fault( actual, randomValue( actual ) );
	}

	@Test
	@Override
	final void isNotEqualTo() {
		var tester = builder.<T>test( Require::isNotEqualTo, RequireFaultBuilder::isNotEqualTo )
			.pass( null, randomValue() )
			.pass( randomValue(), null )
			.fault( null, null );

		T actual = randomValue();
		tester.pass( actual, randomValue( actual ) );

		actual = randomValue();
		tester.fault( actual, actual );
	}

}
