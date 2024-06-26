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

import java.util.Optional;
import java.util.UUID;

import static com.herbmarshall.require.RequireOptionalFaultBuilder.*;

final class RequireOptionalFaultBuilderTest
	extends RequireFaultBuilderTest<Optional<Object>, RequireOptionalFaultBuilder<Object>> {

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

	@Nested
	class contains {

		@Test
		void actual_populated() {
			Object expected = randomObject();
			Optional<Object> actual = Optional.of( expected );
			testBuilder(
				RequireOptionalFaultBuilder::contains,
				actual,
				expected,
				CONTAINS.formatted( expected )
			);
		}

		@Test
		void actual_null() {
			Object expected = randomObject();
			testBuilder(
				RequireOptionalFaultBuilder::contains,
				null,
				expected,
				CONTAINS_NULL.formatted( expected )
			);
		}

		@Test
		void actual_empty() {
			Object expected = randomObject();
			Optional<Object> actual = Optional.empty();
			testBuilder(
				RequireOptionalFaultBuilder::contains,
				actual,
				expected,
				CONTAINS.formatted( expected )
			);
		}

		@Test
		void actual_empty_expected_null() {
			Optional<Object> actual = Optional.empty();
			testBuilder(
				RequireOptionalFaultBuilder::contains,
				actual,
				null,
				CONTAINS.formatted( ( Object ) null )
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
