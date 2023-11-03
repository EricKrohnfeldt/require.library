package com.herbmarshall.require;

import com.herbmarshall.require.tester.RequireTestBuilder;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class RequireBooleanTest extends SingletonRequireTest<Boolean, RequireBooleanFaultBuilder, RequireBoolean> {

	RequireBooleanTest() {
		super( RequireTestBuilder.with(
			Require::that,
			Require::fault
		) );
	}

	@Test
	final void isTrue() {
		builder.test( RequireBoolean::isTrue, RequireBooleanFaultBuilder::isTrue )
			.pass( true )
			.fault( false )
			.fault( null );
	}

	@Test
	final void isFalse() {
		builder.test( RequireBoolean::isFalse, RequireBooleanFaultBuilder::isFalse )
			.pass( false )
			.fault( true )
			.fault( null );
	}

	@Override
	protected Boolean randomValue() {
		return Instant.now().getNano() % 2 == 0;
	}

}
