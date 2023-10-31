package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;

/**
 * Module to provide data assertion {@link Fault Faults}.
 * @param <T> The type of value to operate on
 */
public final class RequirePointerFaultBuilder<T> extends RequireFaultBuilder<T, RequirePointerFaultBuilder<T>> {

	static final String NULL_MESSAGE_TEMPLATE = "Required null, but found %s";
	static final String NOT_NULL_MESSAGE_TEMPLATE = "Required pointer, but found null";

	static final String SAME_MESSAGE_TEMPLATE = "Expected %s to be the same pointer as %s";
	static final String NOT_SAME_MESSAGE_TEMPLATE = "Expected %s to be a different pointer";

	RequirePointerFaultBuilder( T actual ) {
		super( actual );
	}

	/** Create a {@link Fault} for {@link RequirePointer#isNull()}. */
	public Fault<AssertionError> isNull() {
		return build( NULL_MESSAGE_TEMPLATE.formatted( actual ) );
	}

	/** Create a {@link Fault} for {@link RequirePointer#isNotNull()}. */
	public Fault<AssertionError> isNotNull() {
		return build( NOT_NULL_MESSAGE_TEMPLATE );
	}

	/**
	 * Create a {@link Fault} for {@link RequirePointer#isTheSame(Object)}.
	 * @param expected The expected value
	 */
	public Fault<AssertionError> isTheSame( T expected ) {
		return build( SAME_MESSAGE_TEMPLATE.formatted(
			toIdentifier( actual ),
			toIdentifier( expected )
		) );
	}

	/** Create a {@link Fault} for {@link RequirePointer#isNotTheSame(Object)}. */
	public Fault<AssertionError> isNotTheSame() {
		return build( NOT_SAME_MESSAGE_TEMPLATE.formatted(
			toIdentifier( actual )
		) );
	}

	private String toIdentifier( T value ) {
		return Integer.toHexString( System.identityHashCode( value ) );
	}

}
