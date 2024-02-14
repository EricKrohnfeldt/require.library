package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;

import java.util.Set;

/**
 * Module to provide data assertion {@link Fault Faults} for {@link Set} assertions.
 * @param <E> The {@link Set} element type
 */
public final class RequireSetFaultBuilder<E>
	extends RequireCollectionFaultBuilder<E, Set<E>, RequireSetFaultBuilder<E>> {

	static final String COLLECTION_TYPE_NAME = "Set";

	RequireSetFaultBuilder( Set<E> actual ) {
		super( actual, COLLECTION_TYPE_NAME );
	}

}
