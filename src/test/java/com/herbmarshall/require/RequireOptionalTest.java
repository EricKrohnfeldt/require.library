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
