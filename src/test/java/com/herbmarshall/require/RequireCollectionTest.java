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

import com.herbmarshall.require.tester.RequireTestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings( "checkstyle:GenericWhitespace" )  // Using until UTIL-310
abstract class RequireCollectionTest<
		E,
		C extends Collection<E>,
		F extends RequireCollectionFaultBuilder<E, C, F>,
		R extends RequireCollection<E, C, F, R>
	>
	extends RequireTest<C, F, R> {

	RequireCollectionTest( RequireTestBuilder<C, F, R> builder ) {
		super( builder );
	}

	abstract E randomElement();

	abstract Collector<E, ?, C> mutableCollector();

	abstract Collector<E, ?, C> immutableCollector();

	@Nested
	class isEmpty {

		@Test
		void standardTests() {
			C empty = randomMutable();
			empty.clear();
			builder.test(
				RequireCollection::isEmpty,
				RequireCollectionFaultBuilder::isEmpty
			)
				.pass( empty )
				.fault( randomMutable() )
				.fault( randomImmutable() )
				.fault( null );
		}

	}

	@Nested
	class contains {

		@Test
		void standardTests() {
			C collection = randomMutable();
			E contained = randomElement();
			E nonContained = randomElement();
			collection.add( contained );
			builder.<E>test(
				RequireCollection::contains,
				RequireCollectionFaultBuilder::contains
			)
				.pass( collection, contained )
				.fault( collection, nonContained )
				.fault( null, randomElement() );
		}

		@Test
		void nullElement() {
			C hasNull = randomMutable();
			C noNull = randomMutable();
			hasNull.add( null );
			builder.<E>test(
				RequireCollection::contains,
				RequireCollectionFaultBuilder::contains
			)
				.pass( hasNull, null )
				.fault( noNull, null );
		}

	}

	@Nested
	class doesNotContain {

		@Test
		void standardTests() {
			C collection = randomMutable();
			E contained = randomElement();
			E nonContained = randomElement();
			collection.add( contained );
			builder.<E>test(
				RequireCollection::doesNotContain,
				RequireCollectionFaultBuilder::doesNotContain
			)
				.pass( collection, nonContained )
				.fault( collection, contained )
				.fault( null, randomElement() );
		}

		@Test
		void nullElement() {
			C hasNull = randomMutable();
			C noNull = randomMutable();
			hasNull.add( null );
			builder.<E>test(
				RequireCollection::doesNotContain,
				RequireCollectionFaultBuilder::doesNotContain
			)
				.pass( noNull, null )
				.fault( hasNull, null );
		}

	}

	@Nested
	class isMutable {

		@Test
		void standardTests() {
			builder.<Supplier<E>>test(
				RequireCollection::isMutable,
				( builder, supplier ) -> builder.isMutable()
			)
				.pass( randomMutable(), RequireCollectionTest.this::randomElement )
				.fault( randomImmutable(), RequireCollectionTest.this::randomElement )
				.fault( null, RequireCollectionTest.this::randomElement, Require.notNullFault() )
				.fault( randomMutable(), null, Require.notNullFault() );
		}

		@Test
		void noAlter_populatedList() {
			// Arrange
			C actual = randomMutable();
			C expected = copyValue( actual );
			// Act
			builder.that( actual ).isMutable( RequireCollectionTest.this::randomElement );
			// Assert
			Assertions.assertEquals( expected, actual );
		}

		@Test
		void noAlter_emptyList() {
			// Arrange
			C actual = emptyMutable();
			// Act
			builder.that( actual ).isMutable( RequireCollectionTest.this::randomElement );
			// Assert
			Assertions.assertTrue( actual.isEmpty() );
		}

		@Test
		void noAlter_doubleElement() {
			// Arrange
			C basis = randomImmutable();
			C actual = Stream.of( basis, basis )
				.flatMap( Collection::stream )
				.collect( mutableCollector() );
			C expected = copyValue( actual );
			// Act
			builder.that( actual ).isMutable( RequireCollectionTest.this::randomElement );
			// Assert
			Assertions.assertEquals( expected, actual );
		}

	}

	@Nested
	class isImmutable {

		@Test
		void standardTests() {
			builder.<Supplier<E>>test(
				RequireCollection::isImmutable,
				( builder, supplier ) -> builder.isImmutable()
			)
				.pass( randomImmutable(), RequireCollectionTest.this::randomElement )
				.fault( randomMutable(), RequireCollectionTest.this::randomElement )
				.fault( null, RequireCollectionTest.this::randomElement, Require.notNullFault() )
				.fault( randomImmutable(), null, Require.notNullFault() );
		}

		// noAlter tests use mutable here because even in the event of failure, need to not alter collection

		@Test
		void noAlter_populatedList() {
			// Arrange
			C actual = randomMutable();
			C expected = copyValue( actual );
			// Act
			try {
				builder.that( actual ).isImmutable( RequireCollectionTest.this::randomElement );
				Assertions.fail();
			}
			// Assert
			catch ( AssertionError e ) {
				builder.fault( actual ).isImmutable().validate( e );
				Assertions.assertEquals( expected, actual );
			}
		}

		@Test
		void noAlter_emptyList() {
			// Arrange
			C actual = emptyMutable();
			// Act
			try {
				builder.that( actual ).isImmutable( RequireCollectionTest.this::randomElement );
				Assertions.fail();
			}
			// Assert
			catch ( AssertionError e ) {
				builder.fault( actual ).isImmutable().validate( e );
				Assertions.assertTrue( actual.isEmpty() );
			}
		}

		@Test
		void noAlter_doubleElement() {
			// Arrange
			C basis = randomImmutable();
			C actual = Stream.of( basis, basis )
				.flatMap( Collection::stream )
				.collect( mutableCollector() );
			C expected = copyValue( actual );
			// Act
			try {
				builder.that( actual ).isImmutable( RequireCollectionTest.this::randomElement );
				Assertions.fail();
			}
			// Assert
			catch ( AssertionError e ) {
				builder.fault( actual ).isImmutable().validate( e );
				Assertions.assertEquals( expected, actual );
			}
		}

	}

	private C randomMutable() {
		return randomStream().collect( mutableCollector() );
	}

	private C randomImmutable() {
		return randomStream().collect( immutableCollector() );
	}

	private C emptyMutable() {
		return Stream.<E>empty().collect( mutableCollector() );
	}

	private Stream<E> randomStream() {
		return Stream.of(
			randomElement(),
			randomElement(),
			randomElement()
		);
	}

}
