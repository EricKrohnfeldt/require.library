package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;
import com.herbmarshall.standardPipe.Standard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.herbmarshall.require.Require.*;

class RequireTest {

	@Test
	void isNull() {
		test( Require::isNull, RequireFaultBuilder::isNull )
			.pass( null )
			.fault( randomString() );
	}

	@Test
	void isNotNull() {
		test( Require::isNotNull, RequireFaultBuilder::isNotNull )
			.pass( randomString() )
			.fault( null );
	}

	@Test
	void isTheSame() {
		String actual = randomString();
		test( Require::isTheSame, RequireFaultBuilder::isTheSame )
			.pass( actual, actual )
			.pass( null, null )
			.fault( actual, actual + "" )  // Equal, but not same
			.fault( null, randomString() )
			.fault( actual, null );
	}

	@Test
	void isNotTheSame() {
		String actual = randomString();
		test( Require::isNotTheSame, RequireFaultBuilder::isNotTheSame )
			.pass( actual, actual + "" )  // Equal, but not same
			.pass( actual, null )
			.pass( null, randomString() )
			.fault( actual, actual )
			.fault( null, null );
	}

	@Test
	void isEqualTo() {
		String actual = randomString();
		String expected = randomString();
		test( Require::isEqualTo, RequireFaultBuilder::isEqualTo )
			.pass( actual, actual )
			.pass( actual, actual + "" )  // Equal, but not same
			.pass( null, null )
			.fault( actual, expected )
			.fault( null, expected )
			.fault( actual, null );
	}

	@Test
	void isNotEqualTo() {
		String actual = randomString();
		String expected = randomString();
		test( Require::isNotEqualTo, RequireFaultBuilder::isNotEqualTo )
			.pass( actual, expected )
			.pass( null, expected )
			.pass( actual, null )
			.fault( actual, actual )
			.fault( actual, actual + "" )  // Equal, but not same
			.fault( null, null );
	}

	private <T> SimpleRequireAssertionTester<T> test(
		Consumer<Require<T>> thatMethod,
		Function<RequireFaultBuilder<T>, Fault<AssertionError>> faultMethod
	) {
		return new SimpleRequireAssertionTester<>( thatMethod, faultMethod );
	}

	private <T> RequireAssertionTester<T> test(
		BiConsumer<Require<T>, T> thatMethod,
		Function<RequireFaultBuilder<T>, Fault<AssertionError>> faultMethod
	) {
		return test(
			thatMethod,
			( builder, ignored ) -> faultMethod.apply( builder )
		);
	}

	private <T> RequireAssertionTester<T> test(
		BiConsumer<Require<T>, T> thatMethod,
		BiFunction<RequireFaultBuilder<T>, T, Fault<AssertionError>> faultMethod
	) {
		return new RequireAssertionTester<>( thatMethod, faultMethod );
	}

	@Test
	void fail() {
		// Arrange
		// Act
		try {
			Require.fail();
			Assertions.fail();
		}
		catch ( AssertionError e ) {
			Assertions.assertNull( e.getMessage() );
		}
	}

	@Test
	void fail_message() {
		// Arrange
		String message = randomString();
		// Act
		try {
			Require.fail( message );
			Assertions.fail();
		}
		catch ( AssertionError e ) {
			Assertions.assertEquals( message, e.getMessage() );
		}
	}

	@Nested
	class todo_noArgs {

		@Test
		void fullTest() {
			temporarilySetSystemValue( randomString(), () -> {
				// Arrange
				// Act
				try {
					Require.todo();
					Assertions.fail();
				}
				// Assert
				catch ( AssertionError e ) {
					Assertions.assertNull( e.getMessage() );
				}
			} );
		}

		@Test
		@SuppressWarnings( "Convert2MethodRef" )
		void preliminary() {
			temporarilySetSystemValue( TODO_ENVIRONMENT_VARIABLE_VALUE, () -> {
				// Arrange
				// Act
				Require.todo();
				// Assert
				// Does not throw
			} );
		}

	}

	@Nested
	class todo_message {

		@Test
		void fullTest() {
			temporarilySetSystemValue( randomString(), () -> {
				// Arrange
				String message = randomString();
				// Act
				try {
					Require.todo( message );
					Assertions.fail();
				}
				// Assert
				catch ( AssertionError e ) {
					Assertions.assertEquals( message, e.getMessage() );
				}
			} );
		}

		@Test
		void preliminary() {
			temporarilySetSystemValue( TODO_ENVIRONMENT_VARIABLE_VALUE, () -> {
				// Arrange
				String message = randomString();
				// Act
				Require.todo( message );
				// Assert
				// Does not throw
			} );
		}

	}

	@Nested
	class setDiffGenerator {

		@Test
		void noSet() {
			// Arrange
			String actual = randomString();
			String expected = randomString();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			Standard.err.override( buffer );
			Require<String> require = Require.that( actual );
			// Act
			try {
				require.isEqualTo( expected );
				Assertions.fail();
			}
			// Assert
			catch ( AssertionError e ) {
				Require.fault( actual ).isEqualTo( expected ).validate( e );
			}
			finally {
				Standard.err.reset();
			}
			Assertions.assertEquals( SETUP_DIFF_MESSAGE + "\n", buffer.toString() );
		}

		@Test
		void set() {
			// Arrange
			String actual = randomString();
			String expected = randomString();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			Standard.err.override( buffer );
			String diff = randomString();
			DiffGeneratorStub generator = new DiffGeneratorStub( diff );
			Require.setDiffGenerator( generator );
			Require<String> require = Require.that( actual );
			// Act
			try {
				require.isEqualTo( expected );
				Assertions.fail();
			}
			// Assert
			catch ( AssertionError e ) {
				Require.fault( actual ).isEqualTo( expected ).validate( e );
			}
			finally {
				Standard.err.reset();
				Require.setDiffGenerator( null );
			}
			Assertions.assertEquals( actual, generator.getActual() );
			Assertions.assertEquals( expected, generator.getExpected() );
			Assertions.assertEquals( diff + "\n", buffer.toString() );
		}

		@Test
		void unSet() {
			// Arrange
			String actual = randomString();
			String expected = randomString();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			Standard.err.override( buffer );
			Require.setDiffGenerator( null );
			Require<String> require = Require.that( actual );
			// Act
			try {
				require.isEqualTo( expected );
				Assertions.fail();
			}
			// Assert
			catch ( AssertionError e ) {
				Require.fault( actual ).isEqualTo( expected ).validate( e );
			}
			finally {
				Standard.err.reset();
			}
			Assertions.assertEquals( SETUP_DIFF_MESSAGE + "\n", buffer.toString() );
		}

	}

	private static void temporarilySetSystemValue( String value, Runnable runnable ) {
		final Optional<String> currentValue = Optional.ofNullable(
			System.getProperty( TODO_ENVIRONMENT_VARIABLE_NAME )
		);
		try {
			System.setProperty( TODO_ENVIRONMENT_VARIABLE_NAME, value );
			runnable.run();
		}
		finally {
			currentValue.ifPresentOrElse(
				v -> System.setProperty( TODO_ENVIRONMENT_VARIABLE_NAME, v ),
				() -> System.clearProperty( TODO_ENVIRONMENT_VARIABLE_NAME )
			);
		}
	}

	private static String randomString() {
		return UUID.randomUUID().toString();
	}

}
