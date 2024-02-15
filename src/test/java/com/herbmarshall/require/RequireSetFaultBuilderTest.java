package com.herbmarshall.require;

import java.util.Set;
import java.util.UUID;

final class RequireSetFaultBuilderTest
	extends RequireCollectionFaultBuilderTest<Object, Set<Object>, RequireSetFaultBuilder<Object>> {

	RequireSetFaultBuilderTest() {
		super( RequireSetFaultBuilder.COLLECTION_TYPE_NAME );
	}

	@Override
	protected RequireSetFaultBuilder<Object> initializeFaultBuilder( Set<Object> actual ) {
		return new RequireSetFaultBuilder<>( actual );
	}

	@Override
	protected Set<Object> randomValue() {
		return Set.of( randomElement(), randomElement(), randomElement() );
	}

	@Override
	Object randomElement() {
		return UUID.randomUUID();
	}

}
