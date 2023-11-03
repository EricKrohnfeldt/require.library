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

	private static final int RANDOM_EXCEPT_ATTEMPTS = 1000;

	private final boolean singletonMode;
	protected final RequireTestBuilder<T, F, R> builder;

	RequireTest( RequireTestBuilder<T, F, R> builder ) {
		this( false, builder );
	}

	RequireTest( boolean singletonMode, RequireTestBuilder<T, F, R> builder ) {
		this.singletonMode = singletonMode;
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

	@Test
	final void isTheSame() {
		T actual = randomValue();
		var tester = builder.test( Require::isTheSame, RequireFaultBuilder::isTheSame )
			.pass( actual, actual )
			.pass( null, null )
			.fault( null, randomValue() )
			.fault( actual, null );
		if ( ! singletonMode )
			tester.fault( actual, checkedCopyValue( actual ) );
	}

	@Test
	final void isNotTheSame() {
		T actual = randomValue();
		var tester = builder.test(
			Require::isNotTheSame,
			( faultBuilder, expected ) -> faultBuilder.isNotTheSame()
		)
			.pass( actual, null )
			.pass( null, randomValue() )
			.fault( actual, actual )
			.fault( null, null );
		if ( ! singletonMode )
			tester.pass( actual, checkedCopyValue( actual ) );
	}

	@Test
	final void isEqualTo() {
		T actual = randomValue();
		T expected = randomValue( actual );
		var tester = builder.test( Require::isEqualTo, RequireFaultBuilder::isEqualTo )
			.pass( actual, actual )
			.pass( null, null )
			.fault( actual, expected )
			.fault( null, expected )
			.fault( actual, null );
		if ( ! singletonMode )
			tester.pass( actual, checkedCopyValue( actual ) );
	}

	@Test
	final void isNotEqualTo() {
		T actual = randomValue();
		T expected = randomValue( actual );
		var tester = builder.test( Require::isNotEqualTo, RequireFaultBuilder::isNotEqualTo )
			.pass( actual, expected )
			.pass( null, expected )
			.pass( actual, null )
			.fault( actual, actual )
			.fault( null, null );
		if ( ! singletonMode )
			tester.fault( actual, checkedCopyValue( actual ) );
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

	private T checkedCopyValue( T source ) {
		T copy = copyValue( source );
		Assertions.assertNotSame( source, copy, "Copy cannot be the same reference" );
		Assertions.assertEquals( source, copy, "Copy cannot be the equal" );
		return copy;
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

	protected abstract T copyValue( T source );

	protected final String randomString() {
		return UUID.randomUUID().toString();
	}

}
