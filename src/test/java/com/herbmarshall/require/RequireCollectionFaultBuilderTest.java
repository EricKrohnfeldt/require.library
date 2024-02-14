package com.herbmarshall.require;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;

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
