package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;

import java.util.List;

/**
 * Module to provide data assertion {@link Fault Faults} for {@link List} assertions.
 * @param <E> The {@link List} element type
 */
public final class RequireListFaultBuilder<E> extends RequireFaultBuilder<List<E>, RequireListFaultBuilder<E>> {

	static final String MUTABLE_MESSAGE_TEMPLATE = "Required List to be mutable, but is immutable";
	static final String IMMUTABLE_MESSAGE_TEMPLATE = "Required List to be immutable, but is mutable";

	RequireListFaultBuilder( List<E> actual ) {
		super( actual );
	}

	/** Create a {@link Fault} for {@link RequirePointer#isNull()}. */
	public Fault<AssertionError> isMutable() {
		return build( MUTABLE_MESSAGE_TEMPLATE );
	}

	/** Create a {@link Fault} for {@link RequirePointer#isNotNull()}. */
	public Fault<AssertionError> isImmutable() {
		return build( IMMUTABLE_MESSAGE_TEMPLATE );
	}

}
