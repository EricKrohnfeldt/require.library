package com.herbmarshall.require;

import java.util.Set;

/**
 * Module to provide data assertions for {@link Set} values.
 * @param <E> The {@link Set} element type
 */
public final class RequireSet<E>
	extends RequireCollection<E, Set<E>, RequireSetFaultBuilder<E>, RequireSet<E>> {

	RequireSet( Set<E> actual ) {
		super( actual, new RequireSetFaultBuilder<>( actual ) );
	}

}
