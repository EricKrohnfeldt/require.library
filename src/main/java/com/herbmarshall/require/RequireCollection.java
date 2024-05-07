/*
 * This file is part of herbmarshall.com: require.library  ( hereinafter "require.library" ).
 *
 * require.library is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either version 2 of the License,
 * or (at your option) any later version.
 *
 * require.library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with require.library.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.herbmarshall.require;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Module to provide data assertions for {@link Collection} values.
 * @param <E> The {@link Collection} element type
 * @param <C> The {@link Collection} type
 * @param <F> The type of {@link RequireCollectionFaultBuilder} to operate with
 * @param <SELF> Self type reference
 */
@SuppressWarnings( "checkstyle:GenericWhitespace" )  // Using until UTIL-310
public abstract sealed class RequireCollection<
		E,
		C extends Collection<E>,
		F extends RequireCollectionFaultBuilder<E, C, F>,
		SELF extends Require<C, F, SELF>
	>
	extends Require<C, F, SELF>
	permits RequireList, RequireSet {

	RequireCollection( C actual, F fault ) {
		super( actual, fault );
	}

	/**
	 * Checks if {@code actual} is empty, contains no elements.
	 * @throws AssertionError if {@code actual} is {@code null}
	 * @throws AssertionError if {@code actual} contains one or more elements
	 * @return A self reference.
	 */
	public SELF isEmpty() {
		Require.notNull( actual );
		if ( actual.isEmpty() )
			return self();
		throw fault.isEmpty().build();
	}

	/**
	 * Checks if {@code element} is contained within {@code actual}.
	 * @param element The value to check for containment.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} does not contain {@code element}
	 * @throws AssertionError if {@code actual} is {@code null}
	 * @see Collection#contains(Object)
	 */
	public SELF contains( E element ) {
		Require.notNull( actual );
		if ( actual.contains( element ) )
			return self();
		throw fault.contains( element ).build();
	}

	/**
	 * Checks if {@code element} is contained within {@code actual}.
	 * @param element The value to check for containment.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} does not contain {@code element}
	 * @throws AssertionError if {@code actual} is {@code null}
	 * @see Collection#contains(Object)
	 */
	public SELF doesNotContain( E element ) {
		Require.notNull( actual );
		if ( actual.contains( element ) )
			throw fault.doesNotContain( element ).build();
		return self();
	}

	/**
	 * Will check {@code actual} for a mutable state.
	 * <p><b>Inefficient for very large {@link Collection}</b></p>
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is immutable or {@code null} itself
	 * @throws AssertionError if {@code elementSupplier} supplies a {@code null} value or is null itself
	 * @throws NullPointerException possibly if {@code actual} contains a {@code null} element
	 */
	public SELF isMutable( Supplier<E> elementSupplier ) {
		Require.notNull( actual );
		Require.notNull( elementSupplier );

		try {
			removeLastElement()
				.ifPresentOrElse(
					actual::add,  // If present, replace element
					() -> {  // Only ran if collection is empty ( calculated by empty removeLastElement result )
						E element = Require.notNull( elementSupplier.get() );
						actual.add( element );
						actual.remove( element );
					}
				);
		}
		catch ( UnsupportedOperationException e ) {
			throw fault.isMutable().build( e );
		}
		return self();
	}

	/**
	 * Will check {@code actual} for an immutable state.
	 * <p><b>Inefficient for very large {@link Collection}</b></p>
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is mutable or {@code null} itself
	 * @throws AssertionError if {@code elementSupplier} supplies a {@code null} value or is {@code null} itself
	 * @throws NullPointerException possibly if {@code actual} contains a {@code null} element
	 */
	public SELF isImmutable( Supplier<E> elementSupplier ) {
		Require.notNull( actual );
		Require.notNull( elementSupplier );

		try {
			removeLastElement()
				.ifPresentOrElse(
					actual::add,   // Replace removed element and throw
					() -> {  // Only ran if collection is empty ( calculated by empty removeLastElement result )
						E element = Require.notNull( elementSupplier.get() );
						actual.add( element );
						actual.remove( element );
					}
				);
			throw fault.isImmutable().build();
		}
		catch ( UnsupportedOperationException e ) {
			// Pass
		}

		return self();
	}

	private Optional<E> removeLastElement() {
		Iterator<E> iterator = Require.notNull( actual ).iterator();
		while ( iterator.hasNext() ) {
			E target = iterator.next();
			if ( ! iterator.hasNext() ) {
				iterator.remove();
				return Optional.of( target );
			}
		}
		return Optional.empty();
	}

}
