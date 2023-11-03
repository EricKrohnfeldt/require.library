package com.herbmarshall.require;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.herbmarshall.require.RequireBooleanFaultBuilder.FALSE_MESSAGE_TEMPLATE;
import static com.herbmarshall.require.RequireBooleanFaultBuilder.TRUE_MESSAGE_TEMPLATE;

final class RequireBooleanFaultBuilderTest extends RequireFaultBuilderTest<Boolean, RequireBooleanFaultBuilder> {

	@Nested
	class isTrue {

		@Test
		void happyPath() {
			Boolean actual = randomValue();
			testBuilder(
				RequireBooleanFaultBuilder::isTrue,
				actual,
				TRUE_MESSAGE_TEMPLATE.formatted( actual )
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				RequireBooleanFaultBuilder::isTrue,
				null,
				TRUE_MESSAGE_TEMPLATE.formatted( ( Object ) null )
			);
		}

		@Test
		void message_provided() {
			// Arrange
			Boolean actual = randomValue();
			String message = randomString();
			testBuilder(
				RequireBooleanFaultBuilder::isTrue,
				actual,
				message,
				buildCustom( message, TRUE_MESSAGE_TEMPLATE.formatted( actual ) )
			);
		}

	}

	@Nested
	class isFalse {

		@Test
		void happyPath() {
			Boolean actual = randomValue();
			testBuilder(
				RequireBooleanFaultBuilder::isFalse,
				actual,
				FALSE_MESSAGE_TEMPLATE.formatted( actual )
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				RequireBooleanFaultBuilder::isFalse,
				null,
				FALSE_MESSAGE_TEMPLATE.formatted( ( Object ) null )
			);
		}

		@Test
		void message_provided() {
			// Arrange
			Boolean actual = randomValue();
			String message = randomString();
			testBuilder(
				RequireBooleanFaultBuilder::isFalse,
				actual,
				message,
				buildCustom( message, FALSE_MESSAGE_TEMPLATE.formatted( actual ) )
			);
		}

	}

	@Override
	protected RequireBooleanFaultBuilder initializeFaultBuilder( Boolean actual ) {
		return new RequireBooleanFaultBuilder( actual );
	}

	@Override
	protected Boolean randomValue() {
		return Instant.now().getNano() % 2 == 0;
	}

}
