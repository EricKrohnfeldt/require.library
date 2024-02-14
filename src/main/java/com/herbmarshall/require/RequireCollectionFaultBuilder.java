package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;

import java.util.Collection;

/**
 * Module to provide data assertion {@link Fault Faults} for {@link Collection} assertions.
 * @param <E> The {@link Collection} element type
 * @param <C> The {@link Collection} type
 * @param <SELF> Self type reference
 */
@SuppressWarnings( "checkstyle:GenericWhitespace" )  // Using until UTIL-310
public abstract sealed class RequireCollectionFaultBuilder<
		E,
		C extends Collection<E>,
		SELF extends RequireFaultBuilder<C, SELF>
	>
	extends RequireFaultBuilder<C, SELF>
	permits
		RequireListFaultBuilder {

	static final String MUTABLE_MESSAGE_TEMPLATE = "Required %s to be mutable, but is immutable";
	static final String IMMUTABLE_MESSAGE_TEMPLATE = "Required %s to be immutable, but is mutable";

	private final String collectionTypeName;

	RequireCollectionFaultBuilder( C actual, String collectionTypeName ) {
		super( actual );
		this.collectionTypeName = Require.notNull( collectionTypeName );
	}

	/** Create a {@link Fault} for {@link RequirePointer#isNull()}. */
	public final Fault<AssertionError> isMutable() {
		return build( MUTABLE_MESSAGE_TEMPLATE.formatted( collectionTypeName ) );
	}

	/** Create a {@link Fault} for {@link RequirePointer#isNotNull()}. */
	public final Fault<AssertionError> isImmutable() {
		return build( IMMUTABLE_MESSAGE_TEMPLATE.formatted( collectionTypeName ) );
	}

}
