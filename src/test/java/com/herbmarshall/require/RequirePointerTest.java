package com.herbmarshall.require;

import com.herbmarshall.require.tester.RequireTestBuilder;

import java.util.UUID;

final class RequirePointerTest extends RequireTest<UUID, RequirePointerFaultBuilder<UUID>, RequirePointer<UUID>> {

	RequirePointerTest() {
		super( RequireTestBuilder.with(
			Require::that,
			Require::fault
		) );
	}

	@Override
	protected UUID randomValue() {
		return UUID.randomUUID();
	}

	@Override
	protected UUID copyValue( UUID source ) {
		return UUID.fromString( source.toString() );
	}

}
