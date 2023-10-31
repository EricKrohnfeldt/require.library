package com.herbmarshall.require;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.herbmarshall.require.RequirePointerFaultBuilder.*;

final class RequirePointerFaultBuilderTest extends RequireFaultBuilderTest<UUID, RequirePointerFaultBuilder<UUID>> {

	@Nested
	class isNull {

		@Test
		void happyPath() {
			var actual = randomValue();
			testBuilder(
				RequirePointerFaultBuilder::isNull,
				actual,
				NULL_MESSAGE_TEMPLATE.formatted( actual )
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				RequirePointerFaultBuilder::isNull,
				null,
				NULL_MESSAGE_TEMPLATE.formatted( ( Object ) null )
			);
		}

		@Test
		void message_provided() {
			// Arrange
			var actual = randomValue();
			String message = randomString();
			testBuilder(
				RequirePointerFaultBuilder::isNull,
				actual,
				message,
				buildCustom( message, NULL_MESSAGE_TEMPLATE.formatted( actual ) )
			);
		}

	}

	@Nested
	class isNotNull {

		@Test
		void happyPath() {
			testBuilder(
				RequirePointerFaultBuilder::isNotNull,
				randomValue(),
				NOT_NULL_MESSAGE_TEMPLATE
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				RequirePointerFaultBuilder::isNotNull,
				null,
				NOT_NULL_MESSAGE_TEMPLATE
			);
		}

		@Test
		void message_provided() {
			String message = randomString();
			testBuilder(
				RequirePointerFaultBuilder::isNotNull,
				randomValue(),
				message,
				buildCustom( message, NOT_NULL_MESSAGE_TEMPLATE )
			);
		}

	}

	@Nested
	class isTheSame {

		@Test
		void happyPath() {
			var actual = randomValue();
			var expectedValue = randomValue();
			testBuilder(
				RequirePointerFaultBuilder::isTheSame,
				actual,
				expectedValue,
				SAME_MESSAGE_TEMPLATE.formatted(
					toIdentifier( actual ),
					toIdentifier( expectedValue )
				)
			);
		}

		@Test
		void actual_null() {
			var expectedValue = randomValue();
			testBuilder(
				RequirePointerFaultBuilder::isTheSame,
				null,
				expectedValue,
				SAME_MESSAGE_TEMPLATE.formatted( 0, toIdentifier( expectedValue ) )
			);
		}

		@Test
		void expected_null() {
			var actual = randomValue();
			testBuilder(
				RequirePointerFaultBuilder::isTheSame,
				actual,
				null,
				SAME_MESSAGE_TEMPLATE.formatted( toIdentifier( actual ), 0 )
			);
		}

		@Test
		void message_provided() {
			var actual = randomValue();
			var expectedValue = randomValue();
			String message = randomString();
			testBuilder(
				RequirePointerFaultBuilder::isTheSame,
				actual,
				expectedValue,
				message,
				buildCustom(
					message,
					SAME_MESSAGE_TEMPLATE.formatted(
						toIdentifier( actual ),
						toIdentifier( expectedValue )
					)
				)
			);
		}

	}

	@Nested
	class isNotTheSame {

		@Test
		void happyPath() {
			UUID actual = randomValue();
			testBuilder(
				RequirePointerFaultBuilder::isNotTheSame,
				actual,
				NOT_SAME_MESSAGE_TEMPLATE.formatted( toIdentifier( actual ) )
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				RequirePointerFaultBuilder::isNotTheSame,
				null,
				NOT_SAME_MESSAGE_TEMPLATE.formatted( 0 )
			);
		}

		@Test
		void message_provided() {
			var actual = randomValue();
			String message = randomString();
			testBuilder(
				RequirePointerFaultBuilder::isNotTheSame,
				actual,
				message,
				buildCustom( message, NOT_SAME_MESSAGE_TEMPLATE.formatted( toIdentifier( actual ) ) )
			);
		}

	}

	@Override
	protected RequirePointerFaultBuilder<UUID> initializeFaultBuilder( UUID actual ) {
		return new RequirePointerFaultBuilder<>( actual );
	}

	@Override
	protected UUID randomValue() {
		return UUID.randomUUID();
	}

	private String toIdentifier( Object value ) {
		return Integer.toHexString( System.identityHashCode( value ) );
	}

}
