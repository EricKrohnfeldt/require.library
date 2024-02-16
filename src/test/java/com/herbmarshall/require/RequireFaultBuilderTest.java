package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.herbmarshall.require.RequireFaultBuilder.*;

abstract class RequireFaultBuilderTest<T, B extends RequireFaultBuilder<T, B>> {

	@Nested
	class getActual {

		@Test
		void happyPath() {
			// Arrange
			T expected = randomValue();
			B builder = initializeFaultBuilder( expected );
			// Act
			T output = builder.getActual();
			// Assert
			Assertions.assertSame( expected, output );
		}

		@Test
		void actual_null() {
			// Arrange
			B builder = initializeFaultBuilder( null );
			// Act
			T output = builder.getActual();
			// Assert
			Assertions.assertNull( output );
		}

	}

	@Nested
	class getMessage {

		@Test
		void happyPath() {
			// Arrange
			String expected = randomString();
			B builder = initializeFaultBuilder( randomValue() )
				.withMessage( expected );
			// Act
			String output = builder.getMessage().orElseThrow();
			// Assert
			Assertions.assertSame( expected, output );
		}

		@Test
		void message_null() {
			// Arrange
			B builder = initializeFaultBuilder( randomValue() )
				.withMessage( null );
			// Act
			Optional<String> output = builder.getMessage();
			// Assert
			Assertions.assertTrue( output.isEmpty() );
		}

		@Test
		void message_setDefault() {
			// Arrange
			B builder = initializeFaultBuilder( randomValue() )
				.withDefaultMessage();
			// Act
			Optional<String> output = builder.getMessage();
			// Assert
			Assertions.assertTrue( output.isEmpty() );
		}

		@Test
		void message_unset() {
			// Arrange
			B builder = initializeFaultBuilder( randomValue() );
			// Act
			Optional<String> output = builder.getMessage();
			// Assert
			Assertions.assertTrue( output.isEmpty() );
		}

	}

	@Nested
	class isNull {

		@Test
		void happyPath() {
			T actual = randomValue();
			testBuilder(
				RequireFaultBuilder::isNull,
				actual,
				NULL_MESSAGE_TEMPLATE.formatted( actual )
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				RequireFaultBuilder::isNull,
				null,
				NULL_MESSAGE_TEMPLATE.formatted( ( Object ) null )
			);
		}

		@Test
		void message_provided() {
			// Arrange
			T actual = randomValue();
			String message = randomString();
			testBuilder(
				RequireFaultBuilder::isNull,
				actual,
				message,
				buildCustom( message, NULL_MESSAGE_TEMPLATE.formatted( actual ) )
			);
		}

	}

	@Nested
	class isNotNull {

		@Test
		void happyPath() {
			testBuilder(
				RequireFaultBuilder::isNotNull,
				randomValue(),
				NOT_NULL_MESSAGE_TEMPLATE
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				RequireFaultBuilder::isNotNull,
				null,
				NOT_NULL_MESSAGE_TEMPLATE
			);
		}

		@Test
		void message_provided() {
			String message = randomString();
			testBuilder(
				RequireFaultBuilder::isNotNull,
				randomValue(),
				message,
				buildCustom( message, NOT_NULL_MESSAGE_TEMPLATE )
			);
		}

	}

	@Nested
	class isTheSameAs {

		@Test
		void happyPath() {
			T actual = randomValue();
			T expectedValue = randomValue();
			testBuilder(
				RequireFaultBuilder::isTheSameAs,
				actual,
				expectedValue,
				SAME_MESSAGE_TEMPLATE.formatted(
					toIdentifier( actual ),
					toIdentifier( expectedValue )
				)
			);
		}

		@Test
		void actual_null() {
			T expectedValue = randomValue();
			testBuilder(
				RequireFaultBuilder::isTheSameAs,
				null,
				expectedValue,
				SAME_MESSAGE_TEMPLATE.formatted( 0, toIdentifier( expectedValue ) )
			);
		}

		@Test
		void expected_null() {
			T actual = randomValue();
			testBuilder(
				RequireFaultBuilder::isTheSameAs,
				actual,
				( T ) null,
				SAME_MESSAGE_TEMPLATE.formatted( toIdentifier( actual ), 0 )
			);
		}

		@Test
		void message_provided() {
			T actual = randomValue();
			T expectedValue = randomValue();
			String message = randomString();
			testBuilder(
				RequireFaultBuilder::isTheSameAs,
				actual,
				expectedValue,
				message,
				buildCustom(
					message,
					SAME_MESSAGE_TEMPLATE.formatted(
						toIdentifier( actual ),
						toIdentifier( expectedValue )
					)
				)
			);
		}

	}

	@Nested
	class isNotTheSameAs {

		@Test
		void happyPath() {
			T actual = randomValue();
			testBuilder(
				RequireFaultBuilder::isNotTheSameAs,
				actual,
				NOT_SAME_MESSAGE_TEMPLATE.formatted( toIdentifier( actual ) )
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				RequireFaultBuilder::isNotTheSameAs,
				null,
				NOT_SAME_MESSAGE_TEMPLATE.formatted( 0 )
			);
		}

		@Test
		void message_provided() {
			T actual = randomValue();
			String message = randomString();
			testBuilder(
				RequireFaultBuilder::isNotTheSameAs,
				actual,
				message,
				buildCustom( message, NOT_SAME_MESSAGE_TEMPLATE.formatted( toIdentifier( actual ) ) )
			);
		}

	}

	@Nested
	class isEqualTo {

		@Test
		void happyPath() {
			T actual = randomValue();
			T expectedValue = randomValue();
			testBuilder(
				RequireFaultBuilder::isEqualTo,
				actual,
				expectedValue,
				EQUAL_MESSAGE_TEMPLATE.formatted( actual, expectedValue )
			);
		}

		@Test
		void actual_null() {
			T expectedValue = randomValue();
			testBuilder(
				RequireFaultBuilder::isEqualTo,
				null,
				expectedValue,
				EQUAL_MESSAGE_TEMPLATE.formatted( null, expectedValue )
			);
		}

		@Test
		void expected_null() {
			T actual = randomValue();
			testBuilder(
				RequireFaultBuilder::isEqualTo,
				actual,
				( T ) null,
				EQUAL_MESSAGE_TEMPLATE.formatted( actual, null )
			);
		}

		@Test
		void message_provided() {
			T actual = randomValue();
			T expectedValue = randomValue();
			String message = randomString();
			testBuilder(
				RequireFaultBuilder::isEqualTo,
				actual,
				expectedValue,
				message,
				buildCustom(
					message,
					EQUAL_MESSAGE_TEMPLATE.formatted( actual, expectedValue )
				)
			);
		}

	}

	@Nested
	class isNotEqualTo {

		@Test
		void happyPath() {
			T actual = randomValue();
			T expectedValue = randomValue();
			testBuilder(
				RequireFaultBuilder::isNotEqualTo,
				actual,
				expectedValue,
				NOT_EQUAL_MESSAGE_TEMPLATE.formatted( actual, expectedValue )
			);
		}

		@Test
		void actual_null() {
			T expectedValue = randomValue();
			testBuilder(
				RequireFaultBuilder::isNotEqualTo,
				null,
				expectedValue,
				NOT_EQUAL_MESSAGE_TEMPLATE.formatted( null, expectedValue )
			);
		}

		@Test
		void expected_null() {
			T actual = randomValue();
			testBuilder(
				RequireFaultBuilder::isNotEqualTo,
				actual,
				( T ) null,
				NOT_EQUAL_MESSAGE_TEMPLATE.formatted( actual, null )
			);
		}

		@Test
		void message_provided() {
			T actual = randomValue();
			T expectedValue = randomValue();
			String message = randomString();
			testBuilder(
				RequireFaultBuilder::isNotEqualTo,
				actual,
				expectedValue,
				message,
				buildCustom(
					message,
					NOT_EQUAL_MESSAGE_TEMPLATE.formatted( actual, expectedValue )
				)
			);
		}

	}

	protected final void testBuilder(
		Function<B, Fault<AssertionError>> method,
		T actual,
		String errorMessage
	) {
		testBuilder( method, actual, null, errorMessage );
	}

	protected final void testBuilder(
		Function<B, Fault<AssertionError>> method,
		T actual,
		String customMessage,
		String errorMessage
	) {
		// Arrange
		B builder = initializeFaultBuilder( actual )
			.withMessage( customMessage );
		// Act
		Fault<AssertionError> output = method.apply( builder );
		// Assert
		Assertions.assertEquals(
			new Fault<>( AssertionError.class, errorMessage ),
			output
		);
	}

	protected final <U> void testBuilder(
		BiFunction<B, U, Fault<AssertionError>> function,
		T actual,
		U parameter,
		String errorMessage
	) {
		testBuilder( function, actual, parameter, null, errorMessage );
	}

	protected final <U> void testBuilder(
		BiFunction<B, U, Fault<AssertionError>> function,
		T actual,
		U parameter,
		String customMessage,
		String errorMessage
	) {
		// Arrange
		B builder = initializeFaultBuilder( actual )
			.withMessage( customMessage );
		// Act
		Fault<AssertionError> output = function.apply( builder, parameter );
		// Assert
		Assertions.assertEquals(
			new Fault<>( AssertionError.class, errorMessage ),
			output
		);
	}

	protected final String buildCustom( String message, String defaultMessage ) {
		return CUSTOM_MESSAGE_TEMPLATE.formatted( message, defaultMessage );
	}

	protected abstract B initializeFaultBuilder( T actual );

	protected abstract T randomValue();

	protected final String randomString() {
		return UUID.randomUUID().toString();
	}

	private String toIdentifier( T value ) {
		return Integer.toHexString( System.identityHashCode( value ) );
	}

}
