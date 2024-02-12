package com.herbmarshall.require.tester;

import com.herbmarshall.fault.Fault;
import com.herbmarshall.require.Require;
import com.herbmarshall.require.RequireFaultBuilder;

import java.util.function.BiFunction;

/**
 * A tester for 'actual / expected' {@link Require} methods.
 * @param <T> The type of value to operate on
 * @param <F> The type of {@link RequireFaultBuilder} to operate with
 * @param <R> The type of {@link Require} to operate with
 */
public final class RequireTester<T, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>>
	extends ComplexRequireTester<T, T, F, R> {

	RequireTester(
		RequireTestBuilder<T, F, R> builder,
		BiFunction<R, T, R> assertMethod,
		BiFunction<F, T, Fault<AssertionError>> errorMethod
	) {
		super( builder, assertMethod, errorMethod );
	}

	/**
	 * Run the test with passing inputs.
	 * @param actual The actual value for the assertion
	 * @param expected The expected value for the assertion
	 * @return A self reference
	 */
	public RequireTester<T, F, R> pass( T actual, T expected ) {
		super.pass( actual, expected );
		return this;
	}

	/**
	 * Run the test with failing inputs.
	 * @param actual The actual value for the assertion
	 * @param expected The expected value for the assertion
	 * @return A self reference
	 */
	public RequireTester<T, F, R> fault( T actual, T expected ) {
		super.fault( actual, expected );
		return this;
	}

}
