package com.herbmarshall.require;

import com.herbmarshall.require.tester.RequireTestBuilder;
import org.junit.jupiter.api.Test;

abstract non-sealed class SingletonRequireTest<T, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>>
	extends BaseRequireTest<T, F, R> {

	SingletonRequireTest( RequireTestBuilder<T, F, R> builder ) {
		super( builder );
	}

	@Test
	@Override
	final void isTheSame() {
		T actual = randomValue();
		builder.test( Require::isTheSame, RequireFaultBuilder::isTheSame )
			.pass( actual, actual )
			.pass( null, null )
			.fault( null, randomValue() )
			.fault( actual, null );
	}

	@Test
	@Override
	final void isNotTheSame() {
		T actual = randomValue();
		builder.test(
			Require::isNotTheSame,
			( faultBuilder, expected ) -> faultBuilder.isNotTheSame()
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
		builder.test( Require::isEqualTo, RequireFaultBuilder::isEqualTo )
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
		builder.test( Require::isNotEqualTo, RequireFaultBuilder::isNotEqualTo )
			.pass( actual, expected )
			.pass( null, expected )
			.pass( actual, null )
			.fault( actual, actual )
			.fault( null, null );
	}

}
