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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Module to provide data assertions for {@link Stream} values.
 * <p><b>The stream {@code actual} will be terminated</b></p>
 * @param <E> The {@link Stream} element type
 */
public final class RequireStream<E>
	extends Require<Stream<E>, RequireStreamFaultBuilder<E>, RequireStream<E>> {

	private final Supplier<Optional<Stream<E>>> streamSupplier;

	RequireStream( Stream<E> actual ) {
		super( actual, new RequireStreamFaultBuilder<>( actual ) );
		this.streamSupplier = buildSupplier( actual );
	}

	/**
	 * Checks if {@code actual} is empty.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is not empty
	 */
	public RequireStream<E> isEmpty() {
		return actualToSet()
			.filter( Collection::isEmpty )
			.map( set -> self() )
			.orElseThrow( () -> fault.isEmpty().build() );
	}

	/**
	 * Check if {@code actual} once converted to a {@link List} will be equal to {@code expected}.
	 * @param expected the expected {@link List}
	 * @return A self reference
	 * @throws AssertionError if {@code actual} and  {@code expected} are not equal ( including {@code null} equality )
	 */
	public RequireStream<E> isEqualTo( List<? extends E> expected ) {
		if ( isEqualTo( expected, this::actualToList ) )
			return self();
		throw fault.isEqualTo( expected ).build();
	}

	/**
	 * Check if {@code actual} once converted to a {@link Set} will be equal to {@code expected}.
	 * @param expected the expected {@link Set}
	 * @return A self reference
	 * @throws AssertionError if {@code actual} and  {@code expected} are not equal ( including {@code null} equality )
	 */
	public RequireStream<E> isEqualTo( Set<? extends E> expected ) {
		if ( isEqualTo( expected, this::actualToSet ) )
			return self();
		throw fault.isEqualTo( expected ).build();
	}

	private <C extends Collection<? extends E>> boolean isEqualTo(
		C expected,
		Supplier<Optional<? extends C>> supplier
	) {
		return Objects.equals(
			supplier.get().orElse( null ),
			expected
		);
	}

	/**
	 * Will convert this {@link RequireStream} to a {@link RequireList}.
	 * <p><b>{@code null} will transfer as well</b></p>
	 */
	public RequireList<E> toRequireList() {
		return Require.that( actualToList().orElse( null ) );
	}

	/**
	 * Will convert this {@link RequireStream} to a {@link RequireSet}.
	 * <p><b>{@code null} will transfer as well</b></p>
	 */
	public RequireSet<E> toRequireSet() {
		return Require.that( actualToSet().orElse( null ) );
	}

	private Optional<List<E>> actualToList() {
		return streamSupplier.get().map( Stream::toList );
	}

	private Optional<Set<E>> actualToSet() {
		return streamSupplier.get().map( RequireStream::toSet );
	}

	/** Will return an equivalent, but not terminated {@link Stream} to {@code actual}. */
	@Override
	public Stream<E> done() {
		return streamSupplier.get().orElse( null );
	}

	private static <E> Supplier<Optional<Stream<E>>> buildSupplier( Stream<E> stream ) {
		Optional<List<E>> list = Optional.ofNullable( stream )
			.map( Stream::toList );
		return () -> list.map( Collection::stream );
	}

	private static <E> Set<E> toSet( Stream<E> stream ) {
		return stream.collect( Collectors.toUnmodifiableSet() );
	}

}
