package com.herbmarshall.require.tester;

import com.herbmarshall.fault.Fault;
import com.herbmarshall.require.Require;
import com.herbmarshall.require.RequireFaultBuilder;
import org.junit.jupiter.api.Assertions;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * A tester for 'actual only' ( no expected ) {@link Require} methods.
 * @param <A> The type of {@code actual} to operate on
 * @param <F> The type of {@link RequireFaultBuilder} to operate with
 * @param <R> The type of {@link Require} to operate with
 */
public final class SimpleRequireTester<A, F extends RequireFaultBuilder<A, F>, R extends Require<A, F, R>> {

	private final RequireTestBuilder<A, F, R> builder;

	private final UnaryOperator<R> assertMethod;
	private final Function<F, Fault<AssertionError>> errorMethod;

	SimpleRequireTester(
		RequireTestBuilder<A, F, R> builder,
		UnaryOperator<R> assertMethod,
		Function<F, Fault<AssertionError>> errorMethod
	) {
		this.builder = Objects.requireNonNull( builder );
		this.assertMethod = Objects.requireNonNull( assertMethod );
		this.errorMethod = Objects.requireNonNull( errorMethod );
	}

	/**
	 * Run the test with passing inputs.
	 * @param actual The {@code actual} value for the assertion
	 * @return A self reference
	 */
	public SimpleRequireTester<A, F, R> pass( A actual ) {
		// Arrange
		R require = builder.that( actual );
		// Act
		R self = assertMethod.apply( require );
		// Assert
		Assertions.assertSame( require, self );
		return this;
	}

	/**
	 * Run the test with failing inputs.
	 * @param actual The {@code actual} value for the assertion
	 * @return A self reference
	 */
	public SimpleRequireTester<A, F, R> fault( A actual ) {
		fault(
			builder.that( actual ).withDefaultMessage(),
			builder.fault( actual ).withDefaultMessage()
		);
		String message = randomString();
		fault(
			builder.that( actual ).withMessage( message ),
			builder.fault( actual ).withMessage( message )
		);
		return this;
	}

	private void fault( R require, F faultBuilder ) {
		// Arrange
		// Act
		try {
			assertMethod.apply( require );
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
