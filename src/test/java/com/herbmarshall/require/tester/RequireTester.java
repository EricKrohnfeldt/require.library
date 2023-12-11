package com.herbmarshall.require.tester;

import com.herbmarshall.fault.Fault;
import com.herbmarshall.require.Require;
import com.herbmarshall.require.RequireFaultBuilder;
import org.junit.jupiter.api.Assertions;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A tester for 'actual / expected' {@link Require} methods.
 * @param <T> The type of value to operate on
 * @param <F> The type of {@link RequireFaultBuilder} to operate with
 * @param <R> The type of {@link Require} to operate with
 */
public final class RequireTester<T, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>> {

	private final Function<T, R> thatMethod;
	private final Function<T, F> faultMethod;

	private final BiFunction<R, T, R> assertMethod;
	private final BiFunction<F, T, Fault<AssertionError>> errorMethod;

	RequireTester(
		Function<T, R> thatMethod,
		Function<T, F> faultMethod,
		BiFunction<R, T, R> assertMethod,
		BiFunction<F, T, Fault<AssertionError>> errorMethod
	) {
		this.thatMethod = Objects.requireNonNull( thatMethod );
		this.faultMethod = Objects.requireNonNull( faultMethod );
		this.assertMethod = Objects.requireNonNull( assertMethod );
		this.errorMethod = Objects.requireNonNull( errorMethod );
	}

	/**
	 * Run the test with passing inputs.
	 * @param actual The actual value for the assertion
	 * @param expected The expected value for the assertion
	 * @return A self reference
	 */
	public RequireTester<T, F, R> pass( T actual, T expected ) {
		// Arrange
		R require = thatMethod.apply( actual );
		// Act
		R self = assertMethod.apply( require, expected );
		// Assert
		Assertions.assertSame( require, self );
		return this;
	}

	/**
	 * Run the test with failing inputs.
	 * @param actual The actual value for the assertion
	 * @param expected The expected value for the assertion
	 * @return A self reference
	 */
	public RequireTester<T, F, R> fault( T actual, T expected ) {
		fault(
			expected,
			thatMethod.apply( actual ).withDefaultMessage(),
			faultMethod.apply( actual ).withDefaultMessage()
		);
		String message = randomString();
		fault(
			expected,
			thatMethod.apply( actual ).withMessage( message ),
			faultMethod.apply( actual ).withMessage( message )
		);
		return this;
	}

	private void fault( T expected, R require, F faultBuilder ) {
		// Arrange
		// Act
		try {
			assertMethod.apply( require, expected );
			Assertions.fail( "Expected method to fail, it did not" );
		}
		// Assert
		catch ( AssertionError e ) {
			errorMethod.apply( faultBuilder, expected )
				.print()
				.validate( e );
		}
	}

	private static String randomString() {
		return UUID.randomUUID().toString();
	}

}
