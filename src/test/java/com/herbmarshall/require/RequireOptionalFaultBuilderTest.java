package com.herbmarshall.require;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static com.herbmarshall.require.RequireOptionalFaultBuilder.*;

final class RequireOptionalFaultBuilderTest extends RequireFaultBuilderTest<Optional<Object>, RequireOptionalFaultBuilder<Object>> {

	@Nested
	class isPresent {

		@Test
		void happyPath() {
			Optional<Object> actual = randomValue();
			testBuilder(
				RequireOptionalFaultBuilder::isPresent,
				actual,
				IS_PRESENT
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				RequireOptionalFaultBuilder::isPresent,
				null,
				IS_PRESENT_NULL
			);
		}

		@Test
		void message_provided() {
			Optional<Object> actual = randomValue();
			String message = randomString();
			testBuilder(
				RequireOptionalFaultBuilder::isPresent,
				actual,
				message,
				buildCustom( message, IS_PRESENT )
			);
		}

	}

	@Nested
	class isEmpty {

		@Test
		void happyPath() {
			Object value = randomObject();
			Optional<Object> actual = Optional.of( value );
			testBuilder(
				RequireOptionalFaultBuilder::isEmpty,
				actual,
				IS_EMPTY.formatted( value )
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				RequireOptionalFaultBuilder::isEmpty,
				null,
				IS_EMPTY_NULL
			);
		}

		@Test
		void actual_empty() {
			Optional<Object> actual = Optional.empty();
			testBuilder(
				RequireOptionalFaultBuilder::isEmpty,
				actual,
				IS_EMPTY_BASIC
			);
		}

		@Test
		void message_provided() {
			Object value = randomObject();
			Optional<Object> actual = Optional.of( value );
			String message = randomString();
			testBuilder(
				RequireOptionalFaultBuilder::isEmpty,
				actual,
				message,
				buildCustom( message, IS_EMPTY.formatted( value ) )
			);
		}

	}

	@Override
	protected RequireOptionalFaultBuilder<Object> initializeFaultBuilder( Optional<Object> actual ) {
		return new RequireOptionalFaultBuilder<>( actual );
	}

	@Override
	protected Optional<Object> randomValue() {
		return Optional.of( randomObject() );
	}

	private Object randomObject() {
		return UUID.randomUUID();
	}

}
