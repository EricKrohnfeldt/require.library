package com.herbmarshall.require;

import com.herbmarshall.require.tester.RequireTestBuilder;
import com.herbmarshall.standardPipe.Standard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.UUID;

import static com.herbmarshall.require.RequirePointer.*;

abstract class RequireTest<T, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>> {

	protected final RequireTestBuilder<T, F, R> builder;

	RequireTest( RequireTestBuilder<T, F, R> builder ) {
		this.builder = builder;
	}

	@Test
	final void isEqualTo() {
		T actual = randomValue();
		T expected = randomValue();
		builder.test( Require::isEqualTo, RequireFaultBuilder::isEqualTo )
			.pass( actual, actual )
			.pass( actual, checkedCopyValue( actual ) )
			.pass( null, null )
			.fault( actual, expected )
			.fault( null, expected )
			.fault( actual, null );
	}

	@Test
	final void isNotEqualTo() {
		T actual = randomValue();
		T expected = randomValue();
		builder.test( Require::isNotEqualTo, RequireFaultBuilder::isNotEqualTo )
			.pass( actual, expected )
			.pass( null, expected )
			.pass( actual, null )
			.fault( actual, actual )
			.fault( actual, checkedCopyValue( actual ) )
			.fault( null, null );
	}

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

	protected final T checkedCopyValue( T source ) {
		T copy = uncheckedCopyValue( source );
		Assertions.assertNotSame( source, copy );
		Assertions.assertEquals( source, copy );
		return copy;
	}

	protected abstract T randomValue();

	protected abstract T uncheckedCopyValue( T source );

	protected final String randomString() {
		return UUID.randomUUID().toString();
	}

}
