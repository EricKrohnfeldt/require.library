package com.herbmarshall.require;

import com.herbmarshall.require.tester.RequireTestBuilder;
import org.junit.jupiter.api.Test;

/**
 * {@link SingletonRequireTest} should be used with singleton objects and primitives.
 * @param <T> The type of value
 * @param <F> The {@link RequireFaultBuilder} type
 * @param <R> The {@link Require} type
 */
abstract non-sealed class SingletonRequireTest<T, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>>
	extends BaseRequireTest<T, F, R> {

	SingletonRequireTest( RequireTestBuilder<T, F, R> builder ) {
		super( builder );
	}

	@Test
	@Override
	final void isTheSameAs() {
		T actual = randomValue();
		builder.<T>test( Require::isTheSameAs, RequireFaultBuilder::isTheSameAs )
			.pass( actual, actual )
			.pass( null, null )
			.fault( null, randomValue() )
			.fault( actual, null );
	}

	@Test
	@Override
	final void isNotTheSameAs() {
		T actual = randomValue();
		builder.<T>test(
			Require::isNotTheSameAs,
			( faultBuilder, expected ) -> faultBuilder.isNotTheSameAs()
		)
			.pass( actual, null )
			.pass( null, randomValue() )
			.fault( actual, actual )
			.fault( null, null );
	}

	@Test
	@Override
	final void isEqualTo() {
		T actual = randomValue();
		T expected = randomValue( actual );
		builder.<T>test( Require::isEqualTo, RequireFaultBuilder::isEqualTo )
			.pass( actual, actual )
			.pass( null, null )
			.fault( actual, expected )
			.fault( null, expected )
			.fault( actual, null );
	}

	@Test
	@Override
	final void isNotEqualTo() {
		T actual = randomValue();
		T expected = randomValue( actual );
		builder.<T>test( Require::isNotEqualTo, RequireFaultBuilder::isNotEqualTo )
			.pass( actual, expected )
			.pass( null, expected )
			.pass( actual, null )
			.fault( actual, actual )
			.fault( null, null );
	}

}
