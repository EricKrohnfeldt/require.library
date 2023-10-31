package com.herbmarshall.require;

import java.util.UUID;

final class RequirePointerFaultBuilderTest extends RequireFaultBuilderTest<UUID, RequirePointerFaultBuilder<UUID>> {

	@Override
	protected RequirePointerFaultBuilder<UUID> initializeFaultBuilder( UUID actual ) {
		return new RequirePointerFaultBuilder<>( actual );
	}

	@Override
	protected UUID randomValue() {
		return UUID.randomUUID();
	}

}
