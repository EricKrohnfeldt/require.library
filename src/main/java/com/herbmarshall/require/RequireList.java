package com.herbmarshall.require;

import java.util.List;

/**
 * Module to provide data assertions for {@link List} values.
 * @param <E> The {@link List} element type
 */
public final class RequireList<E>
	extends RequireCollection<E, List<E>, RequireListFaultBuilder<E>, RequireList<E>> {

	RequireList( List<E> actual ) {
		super( actual, new RequireListFaultBuilder<>( actual ) );
	}

}
