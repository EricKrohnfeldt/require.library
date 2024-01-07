package com.herbmarshall.require.tester;

import com.herbmarshall.fault.Fault;
import com.herbmarshall.require.Require;
import com.herbmarshall.require.RequireFaultBuilder;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Factory to build tests for {@link Require} subclasses.
 * @param <T> The type of value to operate on
 * @param <F> The type of {@link RequireFaultBuilder} to operate with
 * @param <R> The type of {@link Require} to operate with
 */
public final class RequireTestBuilder<T, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>> {

	private final Function<T, R> thatMethod;
	private final Function<T, F> faultMethod;

	private RequireTestBuilder(
		Function<T, R> thatMethod,
		Function<T, F> faultMethod
	) {
		this.thatMethod = Objects.requireNonNull( thatMethod );
		this.faultMethod = Objects.requireNonNull( faultMethod );
	}

	/**
	 * Create tests for a {@link Require} method.
	 * @param assertMethod The {@link Require} method that will perform the assertion
	 * @param errorMethod The {@link RequireFaultBuilder} method that will produce the {@link Fault}
	 * @return a new {@link SimpleRequireTester}
	 */
	public SimpleRequireTester<T, F, R> test(
		UnaryOperator<R> assertMethod,
		Function<F, Fault<AssertionError>> errorMethod
	) {
		return new SimpleRequireTester<>( thatMethod, faultMethod, assertMethod, errorMethod );
	}

	/**
	 * Create tests for a {@link Require} method.
	 * @param assertMethod The {@link Require} method that will perform the assertion
	 * @param errorMethod The {@link RequireFaultBuilder} method that will produce the {@link Fault}
	 * @return a new {@link RequireTester}
	 */
	public RequireTester<T, F, R> test(
		BiFunction<R, T, R> assertMethod,
		BiFunction<F, T, Fault<AssertionError>> errorMethod
	) {
		return new RequireTester<>( thatMethod, faultMethod, assertMethod, errorMethod );
	}

	/**
	 * Create tests for a {@link Require} method.
	 * @param assertMethod The {@link Require} method that will perform the assertion
	 * @param errorMethod The {@link RequireFaultBuilder} method that will produce the {@link Fault}
	 * @return a new {@link ComplexRequireTester}
	 */
	public <C> ComplexRequireTester<T, C, F, R> testComplex(
		BiFunction<R, C, R> assertMethod,
		BiFunction<F, C, Fault<AssertionError>> errorMethod
	) {
		return new ComplexRequireTester<>( thatMethod, faultMethod, assertMethod, errorMethod );
	}

	/**
	 * Create a new {@link RequireFaultBuilder}.
	 * @param thatMethod The {@link Require} method that will create a {@link Require} subclass
	 * @param faultMethod The {@link Require} method that will create a {@link RequireFaultBuilder} subclass
	 * @param <T> The type of value to operate on
	 * @param <F> The type of {@link RequireFaultBuilder} to operate with
	 * @param <R> The type of {@link Require} to operate with
	 * @return A new {@link RequireTestBuilder}
	 */
	public static <T, F extends RequireFaultBuilder<T, F>, R extends Require<T, F, R>> RequireTestBuilder<T, F, R> with(
		Function<T, R> thatMethod,
		Function<T, F> faultMethod
	) {
		return new RequireTestBuilder<>( thatMethod, faultMethod );
	}

}
