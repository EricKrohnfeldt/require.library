package com.herbmarshall.require;

import java.util.List;
import java.util.UUID;

final class RequireListFaultBuilderTest
	extends RequireCollectionFaultBuilderTest<Object, List<Object>, RequireListFaultBuilder<Object>> {

	RequireListFaultBuilderTest() {
		super( RequireListFaultBuilder.COLLECTION_TYPE_NAME );
	}

	@Override
	protected RequireListFaultBuilder<Object> initializeFaultBuilder( List<Object> actual ) {
		return new RequireListFaultBuilder<>( actual );
	}

	@Override
	protected List<Object> randomValue() {
		return List.of( random(), random(), random() );
	}

	private Object random() {
		return UUID.randomUUID();
	}

}
