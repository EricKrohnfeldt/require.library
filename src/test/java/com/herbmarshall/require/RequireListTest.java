package com.herbmarshall.require;

import com.herbmarshall.require.tester.RequireTestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class RequireListTest extends RequireTest<List<Object>, RequireListFaultBuilder<Object>, RequireList<Object>> {

	RequireListTest() {
		super( RequireTestBuilder.with(
			Require::that,
			Require::fault
		) );
	}

	@Nested
	class isMutable {

		@Test
		void standardTests() {
			builder.<Supplier<Object>>testComplex(
				RequireList::isMutable,
				( builder, supplier ) -> builder.isMutable()
			)
				.pass( randomMutable(), RequireListTest::random )
				.fault( randomImmutable(), RequireListTest::random )
				.fault( null, RequireListTest::random, Require.notNullFault() )
				.fault( randomMutable(), null, Require.notNullFault() );
		}

		@Test
		void noAlter_randomValueMissing() {
			// Arrange
			List<Object> actual = randomMutable();
			List<Object> expected = new ArrayList<>( actual );
			// Act
			Require.that( actual ).isMutable( RequireListTest::random );
			// Assert
			Assertions.assertEquals( expected, actual );
		}

		@Test
		void noAlter_randomValuePresent() {
			// Arrange
			Object element = random();
			List<Object> actual = randomMutable();
			actual.add( element );

			List<Object> expected = new ArrayList<>( actual );
			Supplier<Object> supplier = () -> element;
			// Act
			Require.that( actual ).isMutable( supplier );
			// Assert
			Assertions.assertEquals( expected, actual );
		}

	}

	@Nested
	class isImmutable {

		@Test
		void standardTests() {
			builder.<Supplier<Object>>testComplex(
				RequireList::isImmutable,
				( builder, supplier ) -> builder.isImmutable()
			)
				.pass( randomImmutable(), RequireListTest::random )
				.fault( randomMutable(), RequireListTest::random )
				.fault( null, RequireListTest::random, Require.notNullFault() )
				.fault( randomImmutable(), null, Require.notNullFault() );
		}

		@Test
		void randomValueMissing() {
			// Arrange
			List<Object> actual = randomMutable();
			// Act
			try {
				Require.that( actual ).isMutable( RequireListTest::random );
			}
			// Assert
			catch ( AssertionError e ) {
				Require.fault( actual ).isImmutable().validate( e );
			}
		}

		@Test
		void randomValuePresent() {
			// Arrange
			Object element = random();
			List<Object> actual = Stream.of( randomMutable(), List.of( element ) )
				.flatMap( Collection::stream )
				.collect( Collectors.toList() );
			Supplier<Object> supplier = () -> element;
			// Act
			try {
				Require.that( actual ).isImmutable( supplier );
			}
			// Assert
			catch ( AssertionError e ) {
				Require.fault( actual ).isImmutable().validate( e );
			}
		}

	}

	@Override
	protected List<Object> randomValue() {
		return List.of(
			random(),
			random(),
			random()
		);
	}

	private List<Object> randomImmutable() {
		return randomValue();
	}

	private List<Object> randomMutable() {
		return new ArrayList<>( randomValue() );
	}

	@Override
	protected List<Object> copyValue( List<Object> source ) {
		return source.stream().toList();  // List.copyOf will return same ref for List.of lists
	}

	private static Object random() {
		return UUID.randomUUID();
	}

}
