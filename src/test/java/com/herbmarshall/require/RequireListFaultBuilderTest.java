package com.herbmarshall.require;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.herbmarshall.require.RequireListFaultBuilder.IMMUTABLE_MESSAGE_TEMPLATE;
import static com.herbmarshall.require.RequireListFaultBuilder.MUTABLE_MESSAGE_TEMPLATE;

final class RequireListFaultBuilderTest extends RequireFaultBuilderTest<List<Object>, RequireListFaultBuilder<Object>> {

	@Nested
	class isMutable {

		@Test
		void happyPath() {
			List<Object> actual = randomValue();
			testBuilder(
				RequireListFaultBuilder::isMutable,
				actual,
				MUTABLE_MESSAGE_TEMPLATE
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				RequireListFaultBuilder::isMutable,
				null,
				MUTABLE_MESSAGE_TEMPLATE
			);
		}

		@Test
		void message_provided() {
			// Arrange
			List<Object> actual = randomValue();
			String message = randomString();
			testBuilder(
				RequireListFaultBuilder::isMutable,
				actual,
				message,
				buildCustom( message, MUTABLE_MESSAGE_TEMPLATE )
			);
		}

	}

	@Nested
	class isImmutable {
		@Test
		void happyPath() {
			List<Object> actual = randomValue();
			testBuilder(
				RequireListFaultBuilder::isImmutable,
				actual,
				IMMUTABLE_MESSAGE_TEMPLATE
			);
		}


		@Test
		void actual_null() {
			testBuilder(
				RequireListFaultBuilder::isImmutable,
				null,
				IMMUTABLE_MESSAGE_TEMPLATE
			);
		}
		@Test

		void message_provided() {
			// Arrange
			List<Object> actual = randomValue();
			String message = randomString();
			testBuilder(
				RequireListFaultBuilder::isImmutable,
				actual,
				message,
				buildCustom( message, IMMUTABLE_MESSAGE_TEMPLATE )
			);
		}

	}

	@Override
	protected RequireListFaultBuilder<Object> initializeFaultBuilder( List<Object> actual ) {
		return new RequireListFaultBuilder<>( actual );
	}

	@Override
	protected List<Object> randomValue() {
		return List.of(
			random(),
			random(),
			random()
		);
	}

	private Object random() {
		return UUID.randomUUID();
	}

}
