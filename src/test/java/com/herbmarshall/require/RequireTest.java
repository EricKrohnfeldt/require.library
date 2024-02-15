package com.herbmarshall.require;

import com.herbmarshall.require.tester.RequireTestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link RequireTest} should be used with objects that override {@link Object#equals(Object)}.
 * @param <T> The type of value
 * @param <F> The {@link RequireFaultBuilder} type
 * @param <R> The {@link Require} type
 * @see IdentityEqualsRequireTest
 */
abstract non-sealed class RequireTest<T, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>>
	extends BaseRequireTest<T, F, R> {

	RequireTest( RequireTestBuilder<T, F, R> builder ) {
		super( builder );
	}

	@Test
	@Override
	final void isTheSameAs() {
		var tester = builder.<T>test( Require::isTheSameAs, RequireFaultBuilder::isTheSameAs )
			.pass( null, null )
			.fault( null, randomValue() )
			.fault( randomValue(), null );

		T actual = randomValue();
		tester.pass( actual, actual );

		actual = randomValue();
		tester.fault( actual, checkedCopyValue( actual ) );
	}

	@Test
	@Override
	final void isNotTheSameAs() {
		var tester = builder.<T>test(
			Require::isNotTheSameAs,
			( faultBuilder, expected ) -> faultBuilder.isNotTheSameAs()
		)
			.pass( null, randomValue() )
			.pass( randomValue(), null )
			.fault( null, null );

		T actual = randomValue();
		tester.fault( actual, actual );

		actual = randomValue();
		tester.pass( actual, checkedCopyValue( actual ) );
	}

	@Test
	@Override
	final void isEqualTo() {
		var tester = builder.<T>test( Require::isEqualTo, RequireFaultBuilder::isEqualTo )
			.pass( null, null )
			.fault( null, randomValue() )
			.fault( randomValue(), null );

		T actual = randomValue();
		tester.pass( actual, actual );

		actual = randomValue();
		tester.pass( actual, checkedCopyValue( actual ) );

		actual = randomValue();
		tester.fault( actual, randomValue( actual ) );
	}

	@Test
	@Override
	final void isNotEqualTo() {
		var tester = builder.<T>test( Require::isNotEqualTo, RequireFaultBuilder::isNotEqualTo )
			.pass( null, randomValue() )
			.pass( randomValue(), null )
			.fault( null, null );

		T actual = randomValue();
		tester.pass( actual, randomValue( actual ) );

		actual = randomValue();
		tester.fault( actual, actual );

		actual = randomValue();
		tester.fault( actual, checkedCopyValue( actual ) );
	}

	private T checkedCopyValue( T source ) {
		T copy = copyValue( source );
		Assertions.assertNotSame( source, copy, "Copy cannot be the same reference" );
		Assertions.assertEquals( source, copy, "Copy must be the equal" );
		return copy;
	}

	protected abstract T copyValue( T source );

}
