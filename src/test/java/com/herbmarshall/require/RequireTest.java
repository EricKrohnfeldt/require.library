package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;
import com.herbmarshall.require.tester.RequireTestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

abstract non-sealed class RequireTest<T, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>>
	extends BaseRequireTest<T, F, R> {

	RequireTest( RequireTestBuilder<T, F, R> builder ) {
		super( builder );
	}

	@Test
	@Override
	final void isTheSameAs() {
		T actual = randomValue();
		builder.test( Require::isTheSameAs, RequireFaultBuilder::isTheSameAs )
			.pass( actual, actual )
			.pass( null, null )
			.fault( null, randomValue() )
			.fault( actual, null )
			.fault( actual, checkedCopyValue( actual ) );
	}

	@Test
	@Override
	final void isNotTheSameAs() {
		T actual = randomValue();
		builder.test(
			Require::isNotTheSameAs,
			( faultBuilder, expected ) -> faultBuilder.isNotTheSameAs()
		)
			.pass( actual, null )
			.pass( null, randomValue() )
			.fault( actual, actual )
			.fault( null, null )
			.pass( actual, checkedCopyValue( actual ) );
	}

	@Test
	@Override
	final void isEqualTo() {
		T actual = randomValue();
		T expected = randomValue( actual );
		builder.test( Require::isEqualTo, RequireFaultBuilder::isEqualTo )
			.pass( actual, actual )
			.pass( null, null )
			.fault( actual, expected )
			.fault( null, expected )
			.fault( actual, null )
			.pass( actual, checkedCopyValue( actual ) );
	}

	@Test
	@Override
	final void isNotEqualTo() {
		T actual = randomValue();
		T expected = randomValue( actual );
		builder.test( Require::isNotEqualTo, RequireFaultBuilder::isNotEqualTo )
			.pass( actual, expected )
			.pass( null, expected )
			.pass( actual, null )
			.fault( actual, actual )
			.fault( null, null )
			.fault( actual, checkedCopyValue( actual ) );
	}

	@Nested
	class done {

		@Test
		void happyPath() {
			// Arrange
			T value = randomValue();
			// Act
			T output = Require.that( value ).done();
			// Assert
			Assertions.assertSame( value, output );
		}

		@Test
		void nullActual() {
			// Arrange
			// Act
			T output = Require.that( ( T ) null ).done();
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
				Require.fault( null ).isNotNull().validate( e );
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
	void nonNullFault() {
		// Arrange
		// Act
		Fault<AssertionError> output = Require.notNullFault();
		// Assert
		Fault<AssertionError> expected = Require.fault( null ).isNotNull();
		Assertions.assertEquals( expected, output );
	}

	private T checkedCopyValue( T source ) {
		T copy = copyValue( source );
		Assertions.assertNotSame( source, copy, "Copy cannot be the same reference" );
		Assertions.assertEquals( source, copy, "Copy cannot be the equal" );
		return copy;
	}

	protected abstract T copyValue( T source );

}
