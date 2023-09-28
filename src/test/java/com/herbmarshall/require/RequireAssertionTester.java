package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;
import org.junit.jupiter.api.Assertions;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

class RequireAssertionTester<T> {

	private final BiConsumer<Require<T>, T> thatMethod;
	private final BiFunction<RequireFaultBuilder<T>, T, Fault<AssertionError>> faultMethod;

	RequireAssertionTester(
		BiConsumer<Require<T>, T> thatMethod,
		BiFunction<RequireFaultBuilder<T>, T, Fault<AssertionError>> faultMethod
	) {
		this.thatMethod = Objects.requireNonNull( thatMethod );
		this.faultMethod = Objects.requireNonNull( faultMethod );
	}

	RequireAssertionTester<T> pass( T actual, T expected ) {
		// Arrange
		Require<T> require = Require.that( actual );
		// Act
		thatMethod.accept( require, expected );
		// Assert
		// Does not throw
		return this;
	}

	RequireAssertionTester<T> fault( T actual, T expected ) {
		fault(
			expected,
			Require.that( actual ).withDefaultMessage(),
			Require.fault( actual ).withDefaultMessage()
		);
		String message = randomString();
		fault(
			expected,
			Require.that( actual ).withMessage( message ),
			Require.fault( actual ).withMessage( message )
		);
		return this;
	}

	private void fault( T expected, Require<T> require, RequireFaultBuilder<T> faultBuilder ) {
		// Arrange
		// Act
		try {
			thatMethod.accept( require, expected );
			Assertions.fail();
		}
		// Assert
		catch ( AssertionError e ) {
			faultMethod.apply( faultBuilder, expected )
				.print()
				.validate( e );
		}
	}

	private static String randomString() {
		return UUID.randomUUID().toString();
	}

}
