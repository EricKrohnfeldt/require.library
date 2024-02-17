package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;

import java.util.Collection;
import java.util.function.Supplier;

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
	permits RequireListFaultBuilder, RequireSetFaultBuilder {

	static final String IS_EMPTY_TEMPLATE = "Required %s to be empty, but contains %s";

	static final String DOES_CONTAIN_TEMPLATE = "Required that %s is an element of %s";
	static final String DOES_NOT_CONTAIN_TEMPLATE = "Required that %s is NOT an element of %s";

	static final String MUTABLE_MESSAGE_TEMPLATE = "Required %s to be mutable, but is immutable";
	static final String IMMUTABLE_MESSAGE_TEMPLATE = "Required %s to be immutable, but is mutable";

	private final String collectionTypeName;

	RequireCollectionFaultBuilder( C actual, String collectionTypeName ) {
		super( actual );
		this.collectionTypeName = Require.notNull( collectionTypeName );
	}

	/** Create a {@link Fault} for {@link RequireCollection#isEmpty()}. */
	public final Fault<AssertionError> isEmpty() {
		return actual == null ?
			Require.notNullFault() :
			build( IS_EMPTY_TEMPLATE.formatted( collectionTypeName, actual ) );
	}

	/** Create a {@link Fault} for {@link RequireCollection#contains(Object)}. */
	public final Fault<AssertionError> contains( E element ) {
		return actual == null ?
			Require.notNullFault() :
			build( DOES_CONTAIN_TEMPLATE.formatted( element, actual ) );
	}

	/** Create a {@link Fault} for {@link RequireCollection#doesNotContain(Object)}. */
	public final Fault<AssertionError> doesNotContain( E element ) {
		return actual == null ?
			Require.notNullFault() :
			build( DOES_NOT_CONTAIN_TEMPLATE.formatted( element, actual  ) );
	}

	/** Create a {@link Fault} for {@link RequireCollection#isMutable(Supplier)}. */
	public final Fault<AssertionError> isMutable() {
		return build( MUTABLE_MESSAGE_TEMPLATE.formatted( collectionTypeName ) );
	}

	/** Create a {@link Fault} for {@link RequireCollection#isImmutable(Supplier)}. */
	public final Fault<AssertionError> isImmutable() {
		return build( IMMUTABLE_MESSAGE_TEMPLATE.formatted( collectionTypeName ) );
	}

}
