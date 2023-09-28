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

class RequireFaultBuilderTest {

	@Nested
	class getActual {

		@Test
		void happyPath() {
			// Arrange
			String expected = randomString();
			RequireFaultBuilder<String> builder = new RequireFaultBuilder<>( expected );
			// Act
			String output = builder.getActual();
			// Assert
			Assertions.assertSame( expected, output );
		}

		@Test
		void actual_null() {
			// Arrange
			RequireFaultBuilder<String> builder = new RequireFaultBuilder<>( null );
			// Act
			String output = builder.getActual();
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
			RequireFaultBuilder<String> builder = new RequireFaultBuilder<>( randomString() )
				.withMessage( expected );
			// Act
			String output = builder.getMessage().orElseThrow();
			// Assert
			Assertions.assertSame( expected, output );
		}

		@Test
		void message_null() {
			// Arrange
			RequireFaultBuilder<String> builder = new RequireFaultBuilder<>( randomString() )
				.withMessage( null );
			// Act
			Optional<String> output = builder.getMessage();
			// Assert
			Assertions.assertTrue( output.isEmpty() );
		}

		@Test
		void message_setDefault() {
			// Arrange
			RequireFaultBuilder<String> builder = new RequireFaultBuilder<>( randomString() )
				.withDefaultMessage();
			// Act
			Optional<String> output = builder.getMessage();
			// Assert
			Assertions.assertTrue( output.isEmpty() );
		}

		@Test
		void message_unset() {
			// Arrange
			RequireFaultBuilder<String> builder = new RequireFaultBuilder<>( randomString() );
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
			String actual = randomString();
			test_noParameterBuilder(
				RequireFaultBuilder::isNull,
				actual,
				NULL_MESSAGE_TEMPLATE.formatted( actual )
			);
		}

		@Test
		void actual_null() {
			test_noParameterBuilder(
				RequireFaultBuilder::isNull,
				null,
				NULL_MESSAGE_TEMPLATE.formatted( ( Object ) null )
			);
		}

		@Test
		void message_provided() {
			// Arrange
			String actual = randomString();
			String message = randomString();
			test_noParameterBuilder(
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
			test_noParameterBuilder(
				RequireFaultBuilder::isNotNull,
				randomString(),
				NOT_NULL_MESSAGE_TEMPLATE
			);
		}

		@Test
		void actual_null() {
			test_noParameterBuilder(
				RequireFaultBuilder::isNotNull,
				null,
				NOT_NULL_MESSAGE_TEMPLATE
			);
		}

		@Test
		void message_provided() {
			String message = randomString();
			test_noParameterBuilder(
				RequireFaultBuilder::isNotNull,
				randomString(),
				message,
				buildCustom( message, NOT_NULL_MESSAGE_TEMPLATE )
			);
		}

	}

	@Nested
	class isTheSame {

		@Test
		void happyPath() {
			String actual = randomString();
			String expectedValue = randomString();
			test_singleParameterBuilder(
				RequireFaultBuilder::isTheSame,
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
			String expectedValue = randomString();
			test_singleParameterBuilder(
				RequireFaultBuilder::isTheSame,
				null,
				expectedValue,
				SAME_MESSAGE_TEMPLATE.formatted( 0, toIdentifier( expectedValue ) )
			);
		}

		@Test
		void expected_null() {
			String actual = randomString();
			test_singleParameterBuilder(
				RequireFaultBuilder::isTheSame,
				actual,
				null,
				SAME_MESSAGE_TEMPLATE.formatted( toIdentifier( actual ), 0 )
			);
		}

		@Test
		void message_provided() {
			String actual = randomString();
			String expectedValue = randomString();
			String message = randomString();
			test_singleParameterBuilder(
				RequireFaultBuilder::isTheSame,
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
	class isNotTheSame {

		@Test
		void happyPath() {
			String actual = randomString();
			test_noParameterBuilder(
				RequireFaultBuilder::isNotTheSame,
				actual,
				NOT_SAME_MESSAGE_TEMPLATE.formatted( toIdentifier( actual ) )
			);
		}

		@Test
		void actual_null() {
			test_noParameterBuilder(
				RequireFaultBuilder::isNotTheSame,
				null,
				NOT_SAME_MESSAGE_TEMPLATE.formatted( 0 )
			);
		}

		@Test
		void message_provided() {
			String actual = randomString();
			String message = randomString();
			test_noParameterBuilder(
				RequireFaultBuilder::isNotTheSame,
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
			String actual = randomString();
			String expectedValue = randomString();
			test_singleParameterBuilder(
				RequireFaultBuilder::isEqualTo,
				actual,
				expectedValue,
				EQUAL_MESSAGE_TEMPLATE.formatted( actual, expectedValue )
			);
		}

		@Test
		void actual_null() {
			String expectedValue = randomString();
			test_singleParameterBuilder(
				RequireFaultBuilder::isEqualTo,
				null,
				expectedValue,
				EQUAL_MESSAGE_TEMPLATE.formatted( null, expectedValue )
			);
		}

		@Test
		void expected_null() {
			String actual = randomString();
			test_singleParameterBuilder(
				RequireFaultBuilder::isEqualTo,
				actual,
				null,
				EQUAL_MESSAGE_TEMPLATE.formatted( actual, null )
			);
		}

		@Test
		void message_provided() {
			String actual = randomString();
			String expectedValue = randomString();
			String message = randomString();
			test_singleParameterBuilder(
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
			String actual = randomString();
			String expectedValue = randomString();
			test_singleParameterBuilder(
				RequireFaultBuilder::isNotEqualTo,
				actual,
				expectedValue,
				NOT_EQUAL_MESSAGE_TEMPLATE.formatted( actual, expectedValue )
			);
		}

		@Test
		void actual_null() {
			String expectedValue = randomString();
			test_singleParameterBuilder(
				RequireFaultBuilder::isNotEqualTo,
				null,
				expectedValue,
				NOT_EQUAL_MESSAGE_TEMPLATE.formatted( null, expectedValue )
			);
		}

		@Test
		void expected_null() {
			String actual = randomString();
			test_singleParameterBuilder(
				RequireFaultBuilder::isNotEqualTo,
				actual,
				null,
				NOT_EQUAL_MESSAGE_TEMPLATE.formatted( actual, null )
			);
		}

		@Test
		void message_provided() {
			String actual = randomString();
			String expectedValue = randomString();
			String message = randomString();
			test_singleParameterBuilder(
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

	private void test_noParameterBuilder(
		Function<RequireFaultBuilder<?>, Fault<AssertionError>> method,
		String actual,
		String errorMessage
	) {
		test_noParameterBuilder( method, actual, null, errorMessage );
	}

	private void test_noParameterBuilder(
		Function<RequireFaultBuilder<?>, Fault<AssertionError>> method,
		String actual,
		String customMessage,
		String errorMessage
	) {
		// Arrange
		RequireFaultBuilder<String> builder = new RequireFaultBuilder<>( actual )
			.withMessage( customMessage );
		// Act
		Fault<AssertionError> output = method.apply( builder );
		// Assert
		Assertions.assertEquals(
			new Fault<>( AssertionError.class, errorMessage ),
			output
		);
	}

	private void test_singleParameterBuilder(
		BiFunction<RequireFaultBuilder<String>, String, Fault<AssertionError>> function,
		String actual,
		String expected,
		String errorMessage
	) {
		test_singleParameterBuilder( function, actual, expected, null, errorMessage );
	}

	private void test_singleParameterBuilder(
		BiFunction<RequireFaultBuilder<String>, String, Fault<AssertionError>> function,
		String actual,
		String expected,
		String customMessage,
		String errorMessage
	) {
		// Arrange
		RequireFaultBuilder<String> builder = new RequireFaultBuilder<>( actual )
			.withMessage( customMessage );
		// Act
		Fault<AssertionError> output = function.apply( builder, expected );
		// Assert
		Assertions.assertEquals(
			new Fault<>( AssertionError.class, errorMessage ),
			output
		);
	}

	private String randomString() {
		return UUID.randomUUID().toString();
	}

	private String buildCustom( String message, String defaultMessage ) {
		return CUSTOM_MESSAGE_TEMPLATE.formatted( message, defaultMessage );
	}

	private String toIdentifier( Object value ) {
		return Integer.toHexString( System.identityHashCode( value ) );
	}

}
