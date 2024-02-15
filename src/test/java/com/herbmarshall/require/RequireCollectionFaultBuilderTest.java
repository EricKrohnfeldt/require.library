package com.herbmarshall.require;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static com.herbmarshall.require.RequireCollectionFaultBuilder.DOES_CONTAIN_TEMPLATE;
import static com.herbmarshall.require.RequireCollectionFaultBuilder.DOES_NOT_CONTAIN_TEMPLATE;
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
				addCollectionName( MUTABLE_MESSAGE_TEMPLATE )
			);
		}

		@Test
		void actual_null() {
			testBuilder(
				B::isMutable,
				null,
				addCollectionName( MUTABLE_MESSAGE_TEMPLATE )
			);
		}

		@Test
		void message_provided() {
			// Arrange
			C actual = randomValue();
			String message = randomString();
			testBuilder(
				B::isMutable,
				actual,
				message,
				buildCustom( message, addCollectionName( MUTABLE_MESSAGE_TEMPLATE ) )
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
				addCollectionName( IMMUTABLE_MESSAGE_TEMPLATE )
			);
		}


		@Test
		void actual_null() {
			testBuilder(
				B::isImmutable,
				null,
				addCollectionName( IMMUTABLE_MESSAGE_TEMPLATE )
			);
		}
		@Test

		void message_provided() {
			// Arrange
			C actual = randomValue();
			String message = randomString();
			testBuilder(
				B::isImmutable,
				actual,
				message,
				buildCustom( message, addCollectionName( IMMUTABLE_MESSAGE_TEMPLATE ) )
			);
		}

	}

	private String addCollectionName( String template ) {
		return template.formatted( collectionTypeName );
	}

}
