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

import java.util.Collection;

import static com.herbmarshall.require.RequireCollectionFaultBuilder.*;
import static com.herbmarshall.require.RequireFaultBuilder.NOT_NULL_MESSAGE_TEMPLATE;
import static com.herbmarshall.require.RequireListFaultBuilder.IMMUTABLE_MESSAGE_TEMPLATE;
import static com.herbmarshall.require.RequireListFaultBuilder.MUTABLE_MESSAGE_TEMPLATE;

@SuppressWarnings( "checkstyle:GenericWhitespace" )  // Using until UTIL-310
abstract class RequireCollectionFaultBuilderTest<
		E,
		C extends Collection<E>,
		B extends RequireCollectionFaultBuilder<E, C, B>
	>
	extends RequireFaultBuilderTest<C, B> {

	private final String collectionTypeName;

	RequireCollectionFaultBuilderTest( String collectionTypeName ) {
		this.collectionTypeName = collectionTypeName;
	}

	abstract E randomElement();

	@Nested
	class isEmpty {

		@Test
		void happyPath() {
			C actual = randomValue();
			E element = randomElement();
			testBuilder(
				RequireCollectionFaultBuilder::isEmpty,
				actual,
				IS_EMPTY_TEMPLATE.formatted( collectionTypeName, actual )
			);
		}

		@Test
		void actual_null() {
			E element = randomElement();
			testBuilder(
				builder -> builder.contains( element ),
				null,
				NOT_NULL_MESSAGE_TEMPLATE
			);
		}

	}

	@Nested
	class contains {

		@Test
		void happyPath() {
			C actual = randomValue();
			E element = randomElement();
			testBuilder(
				builder -> builder.contains( element ),
				actual,
				DOES_CONTAIN_TEMPLATE.formatted( element, actual )
			);
		}

		@Test
		void actual_null() {
			E element = randomElement();
			testBuilder(
				builder -> builder.contains( element ),
				null,
				NOT_NULL_MESSAGE_TEMPLATE
			);
		}

		@Test
		void element_null() {
			C actual = randomValue();
			testBuilder(
				builder -> builder.contains( null ),
				actual,
				DOES_CONTAIN_TEMPLATE.formatted( null, actual )
			);
		}

	}

	@Nested
	class doesNotContain {

		@Test
		void happyPath() {
			C actual = randomValue();
			E element = randomElement();
			testBuilder(
				builder -> builder.doesNotContain( element ),
				actual,
				DOES_NOT_CONTAIN_TEMPLATE.formatted( element, actual )
			);
		}

		@Test
		void actual_null() {
			E element = randomElement();
			testBuilder(
				builder -> builder.doesNotContain( element ),
				null,
				NOT_NULL_MESSAGE_TEMPLATE
			);
		}

		@Test
		void element_null() {
			C actual = randomValue();
			testBuilder(
				builder -> builder.doesNotContain( null ),
				actual,
				DOES_NOT_CONTAIN_TEMPLATE.formatted( null, actual )
			);
		}

	}

	@Nested
	class isMutable {

		@Test
		void happyPath() {
			C actual = randomValue();
			testBuilder(
				B::isMutable,
				actual,
				MUTABLE_MESSAGE_TEMPLATE.formatted( collectionTypeName )
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				B::isMutable,
				null,
				MUTABLE_MESSAGE_TEMPLATE.formatted( collectionTypeName )
			);
		}

		@Test
		void message_provided() {
			C actual = randomValue();
			String message = randomString();
			testBuilder(
				B::isMutable,
				actual,
				message,
				buildCustom( message, MUTABLE_MESSAGE_TEMPLATE.formatted( collectionTypeName ) )
			);
		}

	}

	@Nested
	class isImmutable {
		@Test
		void happyPath() {
			C actual = randomValue();
			testBuilder(
				B::isImmutable,
				actual,
				IMMUTABLE_MESSAGE_TEMPLATE.formatted( collectionTypeName )
			);
		}


		@Test
		void actual_null() {
			testBuilder(
				B::isImmutable,
				null,
				IMMUTABLE_MESSAGE_TEMPLATE.formatted( collectionTypeName )
			);
		}
		@Test

		void message_provided() {
			C actual = randomValue();
			String message = randomString();
			testBuilder(
				B::isImmutable,
				actual,
				message,
				buildCustom( message, IMMUTABLE_MESSAGE_TEMPLATE.formatted( collectionTypeName ) )
			);
		}

	}

}
