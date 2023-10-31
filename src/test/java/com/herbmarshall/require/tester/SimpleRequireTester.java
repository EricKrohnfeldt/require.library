package com.herbmarshall.require.tester;

import com.herbmarshall.fault.Fault;
import com.herbmarshall.require.Require;
import com.herbmarshall.require.RequireFaultBuilder;
import org.junit.jupiter.api.Assertions;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A tester for 'actual only' ( no expected ) {@link Require} methods.
 * @param <T> The type of value to operate on
 * @param <F> The type of {@link RequireFaultBuilder} to operate with
 * @param <R> The type of {@link Require} to operate with
 */
public final class SimpleRequireTester<T, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>> {

	private final Function<T, R> thatMethod;
	private final Function<T, F> faultMethod;

	private final Consumer<R> assertMethod;
	private final Function<F, Fault<AssertionError>> errorMethod;

	SimpleRequireTester(
		Function<T, R> thatMethod,
		Function<T, F> faultMethod,
		Consumer<R> assertMethod,
		Function<F, Fault<AssertionError>> errorMethod
	) {
		this.thatMethod = Objects.requireNonNull( thatMethod );
		this.faultMethod = Objects.requireNonNull( faultMethod );
		this.assertMethod = Objects.requireNonNull( assertMethod );
		this.errorMethod = Objects.requireNonNull( errorMethod );
	}

	/**
	 * Run the test with passing inputs.
	 * @param actual The actual value for the assertion
	 * @return A self reference
	 */
	public SimpleRequireTester<T, F, R> pass( T actual ) {
		// Arrange
		R require = thatMethod.apply( actual );
		// Act / Assert
		Assertions.assertDoesNotThrow(
			() -> assertMethod.accept( require )
		);
		return this;
	}

	/**
	 * Run the test with failing inputs.
	 * @param actual The actual value for the assertion
	 * @return A self reference
	 */
	public SimpleRequireTester<T, F, R> fault( T actual ) {
		fault(
			thatMethod.apply( actual ).withDefaultMessage(),
			faultMethod.apply( actual ).withDefaultMessage()
		);
		String message = randomString();
		fault(
			thatMethod.apply( actual ).withMessage( message ),
			faultMethod.apply( actual ).withMessage( message )
		);
		return this;
	}

	private void fault( R require, F faultBuilder ) {
		// Arrange
		// Act
		try {
			assertMethod.accept( require );
			Assertions.fail();
		}
		// Assert
		catch ( AssertionError e ) {
			errorMethod.apply( faultBuilder )
				.print()
				.validate( e );
		}
	}

	private static String randomString() {
		return UUID.randomUUID().toString();
	}

}
