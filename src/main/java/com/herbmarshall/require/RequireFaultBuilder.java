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
	permits RequirePointerFaultBuilder {

	static final String CUSTOM_MESSAGE_TEMPLATE = "%s ( %s )";

	static final String EQUAL_MESSAGE_TEMPLATE = "Expected %s to be equal to %s";
	static final String NOT_EQUAL_MESSAGE_TEMPLATE = "Expected %s to not equal %s";

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

	/** Create a {@link Fault} for {@link RequirePointer#isEqualTo(Object)}. */
	public Fault<AssertionError> isEqualTo( T expected ) {
		return build( EQUAL_MESSAGE_TEMPLATE.formatted( actual, expected ) );
	}

	/** Create a {@link Fault} for {@link RequirePointer#isNotEqualTo(Object)}. */
	public Fault<AssertionError> isNotEqualTo( T expected ) {
		return build( NOT_EQUAL_MESSAGE_TEMPLATE.formatted( actual, expected ) );
	}

	/** Set the displayed error message to the default. */
	public SELF withDefaultMessage() {
		return withMessage( null );
	}

	/** Set the displayed error message. */
	public SELF withMessage( String message ) {
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

}
