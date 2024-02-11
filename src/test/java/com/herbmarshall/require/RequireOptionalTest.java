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
				.pass( Optional.of( random() ) )
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
				.fault( Optional.of( random() ) )
				.fault( null );
		}

	}

	@Nested
	class value {

		@Test
		void actual_isPresent() {
			// Arrange
			Object expected = random();
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
		void actual_null() {
			// Arrange
			RequireOptional<Object> require = Require.that( Optional.empty() );
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
		return Optional.of( random() );
	}

	@Override
	@SuppressWarnings( "SimplifyOptionalCallChains" )
	protected Optional<Object> copyValue( Optional<Object> source ) {
		return Optional.ofNullable( source.orElse( null ) );
	}

	private static Object random() {
		return UUID.randomUUID();
	}

}
