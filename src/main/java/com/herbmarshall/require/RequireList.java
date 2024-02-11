package com.herbmarshall.require;

import java.util.List;
import java.util.function.Supplier;

/**
 * Module to provide data assertions for {@link List} values.
 * @param <E> The {@link List} element type
 */
public final class RequireList<E>
	extends Require<List<E>, RequireListFaultBuilder<E>, RequireList<E>> {

	RequireList( List<E> actual ) {
		super( actual, new RequireListFaultBuilder<>( actual ) );
	}

	/**
	 * Will check {@code actual} for a mutable state.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is immutable
	 */
	public RequireList<E> isMutable( Supplier<E> elementSupplier ) {
		Require.notNull( actual );
		Require.notNull( elementSupplier );

		E element = elementSupplier.get();
		boolean isPresent = actual.contains( element );
		try {
			if ( isPresent ) {
				actual.remove( element );
				actual.add( element );
			}
			else {
				actual.add( element );
				actual.remove( element );
			}
		}
		catch ( UnsupportedOperationException e ) {
			throw fault.isMutable().build( e );
		}
		return self();
	}

	/**
	 * Will check {@code actual} for an immutable state.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is mutable
	 */
	public RequireList<E> isImmutable( Supplier<E> elementSupplier ) {
		Require.notNull( actual );
		Require.notNull( elementSupplier );

		E element = elementSupplier.get();
		boolean isPresent = actual.contains( element );
		try {
			if ( isPresent ) actual.remove( element );
			else actual.add( element );
			throw fault.isImmutable().build();
		}
		catch ( UnsupportedOperationException e ) {
			System.out.println( e.getMessage() );
			// Pass
		}
		return self();
	}

}
