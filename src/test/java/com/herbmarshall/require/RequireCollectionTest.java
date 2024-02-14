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
	class isMutable {

		@Test
		void standardTests() {
			builder.<Supplier<E>>testComplex(
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
			builder.<Supplier<E>>testComplex(
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
