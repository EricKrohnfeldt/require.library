package com.herbmarshall.require;

import com.herbmarshall.require.tester.RequireTestBuilder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class RequirePointerTest extends RequireTest<UUID, RequirePointerFaultBuilder<UUID>, RequirePointer<UUID>> {

	RequirePointerTest() {
		super( RequireTestBuilder.with(
			Require::that,
			Require::fault
		) );
	}

	@Test
	void isNull() {
		builder.test( RequirePointer::isNull, RequirePointerFaultBuilder::isNull )
			.pass( null )
			.fault( randomValue() );
	}

	@Test
	void isNotNull() {
		builder.test( RequirePointer::isNotNull, RequirePointerFaultBuilder::isNotNull )
			.fault( null )
			.pass( randomValue() );
	}

	@Test
	void isTheSame() {
		UUID actual = randomValue();
		builder.test( RequirePointer::isTheSame, RequirePointerFaultBuilder::isTheSame )
			.pass( actual, actual )
			.pass( null, null )
			.fault( actual, checkedCopyValue( actual ) )
			.fault( null, randomValue() )
			.fault( actual, null );
	}

	@Test
	void isNotTheSame() {
		UUID actual = randomValue();
		builder.test(
			RequirePointer::isNotTheSame,
			( faultBuilder, expected ) -> faultBuilder.isNotTheSame()
		)
			.pass( actual, checkedCopyValue( actual ) )
			.pass( actual, null )
			.pass( null, randomValue() )
			.fault( actual, actual )
			.fault( null, null );
	}

	@Override
	protected UUID randomValue() {
		return UUID.randomUUID();
	}

	@Override
	protected UUID uncheckedCopyValue( UUID source ) {
		return UUID.fromString( source.toString() );
	}

}
