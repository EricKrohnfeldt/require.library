package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;
import com.herbmarshall.javaExtension.SelfTyped;

import java.util.Optional;

/**
 * Module to provide data assertion {@link Fault Faults}.
 * @param <T> The type of value to operate on
 * @param <SELF> Self type reference
 */
public abstract sealed class RequireFaultBuilder<T, SELF extends RequireFaultBuilder<T, SELF>>
	extends SelfTyped<SELF>
	permits
		RequireBooleanFaultBuilder,
		RequirePointerFaultBuilder,
		RequireOptionalFaultBuilder,
		RequireCollectionFaultBuilder,
		RequireStreamFaultBuilder {

	static final String CUSTOM_MESSAGE_TEMPLATE = "%s ( %s )";

	static final String NULL_MESSAGE_TEMPLATE = "Required null, but found '%s'";
	static final String NOT_NULL_MESSAGE_TEMPLATE = "Required pointer, but found null";

	static final String SAME_MESSAGE_TEMPLATE = "Expected '%s' to be the same pointer as '%s'";
	static final String NOT_SAME_MESSAGE_TEMPLATE = "Expected '%s' to be a different pointer";

	static final String EQUAL_MESSAGE_TEMPLATE = "Expected '%s' to be equal to '%s'";
	static final String NOT_EQUAL_MESSAGE_TEMPLATE = "Expected '%s' to not equal '%s'";

	static final String NULL_AS_STRING = "null";

	final T actual;

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

	/** Create a {@link Fault} for {@link RequirePointer#isNull()}. */
	public final Fault<AssertionError> isNull() {
		return build( NULL_MESSAGE_TEMPLATE.formatted( actual ) );
	}

	/** Create a {@link Fault} for {@link RequirePointer#isNotNull()}. */
	public final Fault<AssertionError> isNotNull() {
		return build( NOT_NULL_MESSAGE_TEMPLATE );
	}

	/**
	 * Create a {@link Fault} for {@link RequirePointer#isTheSameAs(Object)}.
	 * @param expected The expected value
	 */
	public final Fault<AssertionError> isTheSameAs( T expected ) {
		return build( SAME_MESSAGE_TEMPLATE.formatted(
			toIdentifier( actual ),
			toIdentifier( expected )
		) );
	}

	/** Create a {@link Fault} for {@link RequirePointer#isNotTheSameAs(Object)}. */
	public final Fault<AssertionError> isNotTheSameAs() {
		return build( NOT_SAME_MESSAGE_TEMPLATE.formatted(
			toIdentifier( actual )
		) );
	}

	/** Create a {@link Fault} for {@link RequirePointer#isEqualTo(Object)}. */
	public final Fault<AssertionError> isEqualTo( T expected ) {
		return build( EQUAL_MESSAGE_TEMPLATE.formatted( actual, expected ) );
	}

	/** Create a {@link Fault} for {@link RequirePointer#isNotEqualTo(Object)}. */
	public final Fault<AssertionError> isNotEqualTo( T expected ) {
		return build( NOT_EQUAL_MESSAGE_TEMPLATE.formatted( actual, expected ) );
	}

	/** Set the displayed error message to the default. */
	public final SELF withDefaultMessage() {
		return withMessage( null );
	}

	/** Set the displayed error message. */
	public final SELF withMessage( String message ) {
		this.message = message;
		return self();
	}

	protected final Fault<AssertionError> build( String defaultMessage ) {
		return new Fault<>( AssertionError.class, choose( defaultMessage ) );
	}

	private String choose( String defaultMessage ) {
		return message == null ?
			defaultMessage :
			CUSTOM_MESSAGE_TEMPLATE.formatted( message, defaultMessage );
	}

	private String toIdentifier( T value ) {
		return value == null ?
			NULL_AS_STRING :
			Integer.toHexString( System.identityHashCode( value ) );
	}

}
