package com.herbmarshall.require;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static com.herbmarshall.require.RequireStreamFaultBuilder.*;

final class RequireStreamFaultBuilderTest
	extends RequireFaultBuilderTest<Stream<Object>, RequireStreamFaultBuilder<Object>> {

	@Nested
	class isEmpty {

		@Test
		void happyPath() {
			Stream<Object> actual = randomValue();
			testBuilder(
				RequireStreamFaultBuilder::isEmpty,
				actual,
				IS_EMPTY
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				RequireStreamFaultBuilder::isEmpty,
				null,
				IS_EMPTY_NULL
			);
		}

	}

	@Nested
	class isEqualTo_list {

		@Test
		void happyPath() {
			Stream<Object> actual = randomValue();
			List<Object> list = List.of( randomElement(), randomElement(), randomElement() );
			testBuilder(
				RequireStreamFaultBuilder::isEqualTo,
				actual,
				list,
				IS_EQUAL.formatted( list )
			);
		}

		@Test
		void actual_null() {
			List<Object> list = List.of( randomElement(), randomElement(), randomElement() );
			testBuilder(
				RequireStreamFaultBuilder::isEqualTo,
				null,
				list,
				IS_EQUAL_NULL.formatted( list )
			);
		}

		@Test
		void list_null() {
			Stream<Object> actual = randomValue();
			testBuilder(
				RequireStreamFaultBuilder::isEqualTo,
				actual,
				( List<Object> ) null,
				IS_EQUAL.formatted( ( Object ) null )
			);
		}

	}

	@Nested
	class isEqualTo_set {

		@Test
		void happyPath() {
			Stream<Object> actual = randomValue();
			Set<Object> set = Set.of( randomElement(), randomElement(), randomElement() );
			testBuilder(
				RequireStreamFaultBuilder::isEqualTo,
				actual,
				set,
				IS_EQUAL.formatted( set )
			);
		}

		@Test
		void actual_null() {
			Set<Object> set = Set.of( randomElement(), randomElement(), randomElement() );
			testBuilder(
				RequireStreamFaultBuilder::isEqualTo,
				null,
				set,
				IS_EQUAL_NULL.formatted( set )
			);
		}

		@Test
		void set_null() {
			Stream<Object> actual = randomValue();
			testBuilder(
				RequireStreamFaultBuilder::isEqualTo,
				actual,
				( Set<Object> ) null,
				IS_EQUAL.formatted( ( Object ) null )
			);
		}

	}

	@Override
	protected RequireStreamFaultBuilder<Object> initializeFaultBuilder( Stream<Object> actual ) {
		return new RequireStreamFaultBuilder<>( actual );
	}

	@Override
	protected Stream<Object> randomValue() {
		return Stream.of(
			randomElement(),
			randomElement(),
			randomElement()
		);
	}

	private Object randomElement() {
		return UUID.randomUUID();
	}

}
