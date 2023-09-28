package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;

import java.util.Optional;

/**
 * Module to provide data assertion {@link Fault Faults}.
 * @param <T> The type of value to operate on
 */
public final class RequireFaultBuilder<T> {

	static final String CUSTOM_MESSAGE_TEMPLATE = "%s ( %s )";

	static final String NULL_MESSAGE_TEMPLATE = "Required null, but found %s";
	static final String NOT_NULL_MESSAGE_TEMPLATE = "Required pointer, but found null";

	static final String SAME_MESSAGE_TEMPLATE = "Expected %s to be the same pointer as %s";
	static final String NOT_SAME_MESSAGE_TEMPLATE = "Expected %s to be a different pointer";

	static final String EQUAL_MESSAGE_TEMPLATE = "Expected %s to be equal to %s";
	static final String NOT_EQUAL_MESSAGE_TEMPLATE = "Expected %s to not equal %s";

	private final T actual;

	private String message;

	RequireFaultBuilder( T actual ) {
		this.actual = actual;
	}

	/** @return The {@code actual} value */
	public T getActual() {
		return actual;
	}

	/** @return The {@code message} value */
	public Optional<String> getMessage() {
		return Optional.ofNullable( message );
	}

	/** Create a {@link Fault} for {@link Require#isNull()}. */
	public Fault<AssertionError> isNull() {
		return build( NULL_MESSAGE_TEMPLATE.formatted( actual ) );
	}

	/** Create a {@link Fault} for {@link Require#isNotNull()}. */
	public Fault<AssertionError> isNotNull() {
		return build( NOT_NULL_MESSAGE_TEMPLATE );
	}

	/**
	 * Create a {@link Fault} for {@link Require#isTheSame(Object)}.
	 * @param expected The expected value
	 */
	public Fault<AssertionError> isTheSame( T expected ) {
		// Integer.toHexString(hashCode())
		return build( SAME_MESSAGE_TEMPLATE.formatted(
			toIdentifier( actual ),
			toIdentifier( expected )
		) );
	}

	/** Create a {@link Fault} for {@link Require#isNotTheSame(Object)}. */
	public Fault<AssertionError> isNotTheSame() {
		return build( NOT_SAME_MESSAGE_TEMPLATE.formatted(
			toIdentifier( actual )
		) );
	}

	/** Create a {@link Fault} for {@link Require#isEqualTo(Object)}. */
	public Fault<AssertionError> isEqualTo( T expected ) {
		return build( EQUAL_MESSAGE_TEMPLATE.formatted( actual, expected ) );
	}

	/** Create a {@link Fault} for {@link Require#isNotEqualTo(Object)}. */
	public Fault<AssertionError> isNotEqualTo( T expected ) {
		return build( NOT_EQUAL_MESSAGE_TEMPLATE.formatted( actual, expected ) );
	}

	/** Set the displayed error message to the default. */
	public RequireFaultBuilder<T> withDefaultMessage() {
		return withMessage( null );
	}

	/** Set the displayed error message. */
	public RequireFaultBuilder<T> withMessage( String message ) {
		this.message = message;
		return this;
	}

	private Fault<AssertionError> build( String defaultMessage ) {
		return new Fault<>( AssertionError.class, choose( defaultMessage ) );
	}

	private String choose( String defaultMessage ) {
		return message == null ?
			defaultMessage :
			CUSTOM_MESSAGE_TEMPLATE.formatted( message, defaultMessage );
	}

	private String toIdentifier( T value ) {
		return Integer.toHexString( System.identityHashCode( value ) );
	}

}
