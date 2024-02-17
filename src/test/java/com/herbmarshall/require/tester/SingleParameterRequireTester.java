package com.herbmarshall.require.tester;

import com.herbmarshall.fault.Fault;
import com.herbmarshall.require.Require;
import com.herbmarshall.require.RequireFaultBuilder;
import org.junit.jupiter.api.Assertions;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * A tester for 'actual / expected' {@link Require} methods.
 * @param <A> The type of {@code actual} to operate on
 * @param <E> The type of {@code expected} to operate on
 * @param <F> The type of {@link RequireFaultBuilder} to operate with
 * @param <R> The type of {@link Require} to operate with
 */
public final class SingleParameterRequireTester<A, E, F extends RequireFaultBuilder<A, F>, R extends Require<A, F, R>> {

	private final RequireTestBuilder<A, F, R> builder;

	private final BiFunction<R, E, R> assertMethod;
	private final BiFunction<F, E, Fault<AssertionError>> errorMethod;

	SingleParameterRequireTester(
		RequireTestBuilder<A, F, R> builder,
		BiFunction<R, E, R> assertMethod,
		BiFunction<F, E, Fault<AssertionError>> errorMethod
	) {
		this.builder = Objects.requireNonNull( builder );
		this.assertMethod = Objects.requireNonNull( assertMethod );
		this.errorMethod = Objects.requireNonNull( errorMethod );
	}

	/**
	 * Run the test with passing inputs.
	 * @param actual The {@code actual} value for the assertion
	 * @param expected The {@code expected} use for evaluation
	 * @return A self reference
	 */
	public SingleParameterRequireTester<A, E, F, R> pass( A actual, E expected ) {
		// Arrange
		R require = builder.that( actual );
		// Act
		R self = assertMethod.apply( require, expected );
		// Assert
		Assertions.assertSame( require, self );
		return this;
	}

	/**
	 * Run the test with failing inputs.
	 * @param actual The {@code actual} value for the assertion
	 * @param expected The {@code expected} use for evaluation
	 * @return A self reference
	 */
	public SingleParameterRequireTester<A, E, F, R> fault( A actual, E expected ) {
		final R require = builder.that( actual );
		fault(
			expected,
			require.withDefaultMessage(),
			errorMethod.apply(
				builder.fault( actual ).withDefaultMessage(),
				expected
			)
		);
		String message = randomString();
		fault(
			expected,
			require.withMessage( message ),
			errorMethod.apply(
				builder.fault( actual ).withMessage( message ),
				expected
			)
		);
		return this;
	}

	/**
	 * Run the test with failing inputs but with 'non-standard' {@link Fault}.
	 * @param actual The {@code actual} value for the assertion
	 * @param expected The {@code expected} use for evaluation
	 * @param fault An alternative {@link Fault} to expect
	 * @return A self reference
	 */
	public SingleParameterRequireTester<A, E, F, R> fault( A actual, E expected, Fault<AssertionError> fault ) {
		fault(
			expected,
			builder.that( actual ),
			fault
		);
		return this;
	}

	private void fault( E expected, R require, Fault<AssertionError> fault ) {
		// Arrange
		// Act
		try {
			assertMethod.apply( require, expected );
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
