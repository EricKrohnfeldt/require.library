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
