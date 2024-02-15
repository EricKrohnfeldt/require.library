package com.herbmarshall.require.tester;

import com.herbmarshall.fault.Fault;
import com.herbmarshall.require.Require;
import com.herbmarshall.require.RequireFaultBuilder;
import org.junit.jupiter.api.Assertions;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * A tester for 'actual / condition' {@link Require} methods.
 * @param <T> The type of value to operate on
 * @param <C> The type of condition to operate on
 * @param <F> The type of {@link RequireFaultBuilder} to operate with
 * @param <R> The type of {@link Require} to operate with
 */
public final class SingleParameterRequireTester<T, C, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>> {

	private final RequireTestBuilder<T, F, R> builder;

	private final BiFunction<R, C, R> assertMethod;
	private final BiFunction<F, C, Fault<AssertionError>> errorMethod;

	SingleParameterRequireTester(
		RequireTestBuilder<T, F, R> builder,
		BiFunction<R, C, R> assertMethod,
		BiFunction<F, C, Fault<AssertionError>> errorMethod
	) {
		this.builder = Objects.requireNonNull( builder );
		this.assertMethod = Objects.requireNonNull( assertMethod );
		this.errorMethod = Objects.requireNonNull( errorMethod );
	}

	/**
	 * Run the test with passing inputs.
	 * @param actual The actual value for the assertion
	 * @param condition The condition use for evaluation
	 * @return A self reference
	 */
	public SingleParameterRequireTester<T, C, F, R> pass( T actual, C condition ) {
		// Arrange
		R require = builder.that( actual );
		// Act
		R self = assertMethod.apply( require, condition );
		// Assert
		Assertions.assertSame( require, self );
		return this;
	}

	/**
	 * Run the test with failing inputs.
	 * @param actual The actual value for the assertion
	 * @param condition The condition use for evaluation
	 * @return A self reference
	 */
	public SingleParameterRequireTester<T, C, F, R> fault( T actual, C condition ) {
		fault(
			condition,
			builder.that( actual ).withDefaultMessage(),
			errorMethod.apply(
				builder.fault( actual ).withDefaultMessage(),
				condition
			)
		);
		String message = randomString();
		fault(
			condition,
			builder.that( actual ).withMessage( message ),
			errorMethod.apply(
				builder.fault( actual ).withMessage( message ),
				condition
			)
		);
		return this;
	}

	/**
	 * Run the test with failing inputs but with 'non-standard' {@link Fault}.
	 * @param actual The actual value for the assertion
	 * @param condition The condition use for evaluation
	 * @param fault An alternative {@link Fault} to expect
	 * @return A self reference
	 */
	public SingleParameterRequireTester<T, C, F, R> fault( T actual, C condition, Fault<AssertionError> fault ) {
		fault(
			condition,
			builder.that( actual ),
			fault
		);
		return this;
	}

	private void fault( C condition, R require, Fault<AssertionError> fault ) {
		// Arrange
		// Act
		try {
			assertMethod.apply( require, condition );
			Assertions.fail( "Expected method to fail, it did not" );
		}
		// Assert
		catch ( AssertionError e ) {
			fault.print().validate( e );
		}
	}

	private static String randomString() {
		return UUID.randomUUID().toString();
	}

}
