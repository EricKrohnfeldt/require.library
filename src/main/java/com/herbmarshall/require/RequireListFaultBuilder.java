package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;

import java.util.List;

/**
 * Module to provide data assertion {@link Fault Faults} for {@link List} assertions.
 * @param <E> The {@link List} element type
 */
public final class RequireListFaultBuilder<E>
	extends RequireCollectionFaultBuilder<E, List<E>, RequireListFaultBuilder<E>> {

	static final String COLLECTION_TYPE_NAME = "List";

	RequireListFaultBuilder( List<E> actual ) {
		super( actual, COLLECTION_TYPE_NAME );
	}

}
