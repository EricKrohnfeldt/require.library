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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

final class RequireOptionalTest
	extends RequireTest<Optional<Object>, RequireOptionalFaultBuilder<Object>, RequireOptional<Object>> {

	RequireOptionalTest() {
		super( RequireTestBuilder.with(
			Require::that,
			Require::fault
		) );
	}

	@Nested
	class isPresent {

		@Test
		void standardTests() {
			builder.test(
				RequireOptional::isPresent,
				RequireOptionalFaultBuilder::isPresent
			)
				.pass( Optional.of( randomElement() ) )
				.fault( Optional.empty() )
				.fault( null );
		}

	}

	@Nested
	class isEmpty {

		@Test
		void standardTests() {
			builder.test(
				RequireOptional::isEmpty,
				RequireOptionalFaultBuilder::isEmpty
			)
				.pass( Optional.empty() )
				.fault( Optional.of( randomElement() ) )
				.fault( null );
		}

	}

	@Nested
	class contains {

		@Test
		void standardTests() {
			var tester = builder.test(
				RequireOptional::contains,
				RequireOptionalFaultBuilder::contains
			)
				.pass( Optional.empty(), null )
				.fault( null, null )
				.fault( null, randomElement() )
				.fault( randomValue(), null )
				.fault( randomValue(), randomElement() );

			Object expected = randomElement();
			Optional<Object> actual = Optional.of( expected );
			tester.pass( actual, expected );
		}

	}

	@Nested
	class value {

		@Test
		void actual_isPresent() {
			// Arrange
			Object expected = randomElement();
			RequireOptional<Object> require = Require.that( Optional.of( expected ) );
			// Act
			RequirePointer<Object> output = require.value();
			// Assert
			Require.that( output.actual ).isEqualTo( expected );
		}

		@Test
		void actual_empty() {
			// Arrange
			RequireOptional<Object> require = Require.that( Optional.empty() );
			// Act
			RequirePointer<Object> output = require.value();
			// Assert
			Require.that( output.actual ).isNull();
		}

		@Test
		@SuppressWarnings( "OptionalAssignedToNull" )
		void actual_null() {
			// Arrange
			RequireOptional<Object> require = Require.that( ( Optional<Object> ) null );
			// Act
			try {
				require.value();
			}
			// Assert
			catch ( AssertionError e ) {
				Require.notNullFault().validate( e );
			}
		}

	}

	@Override
	protected Optional<Object> randomValue() {
		return Optional.of( randomElement() );
	}

	@Override
	@SuppressWarnings( "SimplifyOptionalCallChains" )
	protected Optional<Object> copyValue( Optional<Object> source ) {
		return Optional.ofNullable( source.orElse( null ) );
	}

	private static Object randomElement() {
		return UUID.randomUUID();
	}

}
