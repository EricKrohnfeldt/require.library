package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;
import com.herbmarshall.require.tester.RequireTestBuilder;
import com.herbmarshall.standardPipe.Standard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.UUID;

import static com.herbmarshall.require.RequirePointer.*;

abstract sealed class BaseRequireTest<T, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>>
	permits RequireTest, IdentityEqualsRequireTest {

	private static final int RANDOM_EXCEPT_ATTEMPTS = 1000;

	protected final RequireTestBuilder<T, F, R> builder;

	BaseRequireTest( RequireTestBuilder<T, F, R> builder ) {
		this.builder = builder;
	}

	@Test
	final void isNull() {
		builder.test( Require::isNull, RequireFaultBuilder::isNull )
			.pass( null )
			.fault( randomValue() );
	}

	@Test
	final void isNotNull() {
		builder.test( Require::isNotNull, RequireFaultBuilder::isNotNull )
			.fault( null )
			.pass( randomValue() );
	}

	abstract void isTheSameAs();
	abstract void isNotTheSameAs();

	abstract void isEqualTo();
	abstract void isNotEqualTo();

	@Test
	final void fail() {
		// Arrange
		// Act
		try {
			RequirePointer.fail();
			Assertions.fail();
		}
		catch ( AssertionError e ) {
			Assertions.assertNull( e.getMessage() );
		}
	}

	@Test
	final void fail_message() {
		// Arrange
		String message = randomString();
		// Act
		try {
			RequirePointer.fail( message );
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
					RequirePointer.todo();
					Assertions.fail();
				}
				// Assert
				catch ( AssertionError e ) {
					Assertions.assertNull( e.getMessage() );
				}
			} );
		}

		@Test
		void preliminary() {
			temporarilySetSystemValue( TODO_ENVIRONMENT_VARIABLE_VALUE, () ->
				Assertions.assertDoesNotThrow( () -> RequirePointer.todo() )
			);
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
					RequirePointer.todo( message );
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
				// Act / Assert
				Assertions.assertDoesNotThrow(
					() -> RequirePointer.todo( message )
				);
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
			RequirePointer<String> require = RequirePointer.that( actual );
			// Act
			try {
				require.isEqualTo( expected );
				Assertions.fail();
			}
			// Assert
			catch ( AssertionError e ) {
				RequirePointer.fault( actual ).isEqualTo( expected ).validate( e );
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
			RequirePointer.setDiffGenerator( generator );
			RequirePointer<String> require = RequirePointer.that( actual );
			// Act
			try {
				require.isEqualTo( expected );
				Assertions.fail();
			}
			// Assert
			catch ( AssertionError e ) {
				RequirePointer.fault( actual ).isEqualTo( expected ).validate( e );
			}
			finally {
				Standard.err.reset();
				RequirePointer.setDiffGenerator( null );
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
			RequirePointer.setDiffGenerator( null );
			RequirePointer<String> require = RequirePointer.that( actual );
			// Act
			try {
				require.isEqualTo( expected );
				Assertions.fail();
			}
			// Assert
			catch ( AssertionError e ) {
				RequirePointer.fault( actual ).isEqualTo( expected ).validate( e );
			}
			finally {
				Standard.err.reset();
			}
			Assertions.assertEquals( SETUP_DIFF_MESSAGE + "\n", buffer.toString() );
		}

	}

	@Nested
	class done {

		@Test
		void happyPath() {
			// Arrange
			T value = randomValue();
			// Act
			T output = builder.that( value ).done();
			// Assert
			Assertions.assertSame( value, output );
		}

		@Test
		void nullActual() {
			// Arrange
			// Act
			T output = builder.that( null ).done();
			// Assert
			Assertions.assertNull( output );
		}

	}

	@Nested
	class notNull {

		@Test
		void nullValue() {
			// Arrange
			// Act
			try {
				Require.notNull( null );
				Assertions.fail();
			}
			// Assert
			catch ( AssertionError e ) {
				Require.fault( ( Object ) null ).isNotNull().validate( e );
			}
		}

		@Test
		void notNullValue() {
			// Arrange
			T original = randomValue();
			// Act
			T output = Require.notNull( original );
			// Assert
			Assertions.assertSame( original, output );
		}

	}

	@Test
	final void nonNullFault() {
		// Arrange
		// Act
		Fault<AssertionError> output = Require.notNullFault();
		// Assert
		Fault<AssertionError> expected = Require.fault( ( Object ) null ).isNotNull();
		Assertions.assertEquals( expected, output );
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

	protected abstract T randomValue();

	protected final T randomValue( T except ) {
		for ( int i = 0; i < RANDOM_EXCEPT_ATTEMPTS; ++i ) {
			T value = randomValue();
			if ( !  value.equals( except ) )
				return value;
		}
		throw new AssertionError( "Failed to find random value not equal to " + except );
	}

	protected final String randomString() {
		return UUID.randomUUID().toString();
	}

}
