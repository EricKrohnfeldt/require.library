package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;
import org.junit.jupiter.api.Assertions;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

class SimpleRequireAssertionTester<T> {

	private final Consumer<Require<T>> thatMethod;
	private final Function<RequireFaultBuilder<T>, Fault<AssertionError>> faultMethod;

	SimpleRequireAssertionTester(
		Consumer<Require<T>> thatMethod,
		Function<RequireFaultBuilder<T>, Fault<AssertionError>> faultMethod
	) {
		this.thatMethod = Objects.requireNonNull( thatMethod );
		this.faultMethod = Objects.requireNonNull( faultMethod );
	}

	SimpleRequireAssertionTester<T> pass( T actual ) {
		// Arrange
		Require<T> require = Require.that( actual );
		// Act
		thatMethod.accept( require );
		// Assert
		// Does not throw
		return this;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	SimpleRequireAssertionTester<T> fault( T actual ) {
		fault(
			Require.that( actual ).withDefaultMessage(),
			Require.fault( actual ).withDefaultMessage()
		);
		String message = randomString();
		fault(
			Require.that( actual ).withMessage( message ),
			Require.fault( actual ).withMessage( message )
		);
		return this;
	}

	private void fault( Require<T> require, RequireFaultBuilder<T> faultBuilder ) {
		// Arrange
		// Act
		try {
			thatMethod.accept( require );
			Assertions.fail();
		}
		// Assert
		catch ( AssertionError e ) {
			faultMethod.apply( faultBuilder )
				.print()
				.validate( e );
		}
	}

	private static String randomString() {
		return UUID.randomUUID().toString();
	}

}
