package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Module to provide data assertion {@link Fault Faults} for {@link Optional} assertions.
 * @param <E> The {@link Optional} element type
 */
public final class RequireStreamFaultBuilder<E>
	extends RequireFaultBuilder<Stream<E>, RequireStreamFaultBuilder<E>> {

	static final String IS_EMPTY = "Required Stream to be empty";
	static final String IS_EMPTY_NULL = IS_EMPTY + ", however it is null";

	static final String IS_EQUAL = "Stream was required to contain same values as %s";
	static final String IS_EQUAL_NULL = IS_EQUAL + ", however it is null";

	RequireStreamFaultBuilder( Stream<E> actual ) {
		super( actual );
	}

	/** Create a {@link Fault} for {@link RequireStream#isEmpty()}. */
	public Fault<AssertionError> isEmpty() {
		return build( actual == null ? IS_EMPTY_NULL : IS_EMPTY );
	}

	/** Create a {@link Fault} for {@link RequireStream#isEqualTo(List)}. */
	public Fault<AssertionError> isEqualTo( List<E> list ) {
		return build( ( actual == null ? IS_EQUAL_NULL : IS_EQUAL ).formatted( list ) );
	}

	/** Create a {@link Fault} for {@link RequireStream#isEqualTo(Set)}. */
	public Fault<AssertionError> isEqualTo( Set<E> set ) {
		return build( ( actual == null ? IS_EQUAL_NULL : IS_EQUAL ).formatted( set ) );
	}

}
