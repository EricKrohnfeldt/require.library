package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;

/** Module to provide data assertion {@link Fault Faults} for {@link Boolean} assertions. */
public final class RequireBooleanFaultBuilder extends RequireFaultBuilder<Boolean, RequireBooleanFaultBuilder> {

	static final String TRUE_MESSAGE_TEMPLATE = "Required True, but found %s";
	static final String FALSE_MESSAGE_TEMPLATE = "Required False, but found %s";

	RequireBooleanFaultBuilder( Boolean actual ) {
		super( actual );
	}

	/** Create a {@link Fault} for {@link RequireBoolean#isTrue()}. */
	public Fault<AssertionError> isTrue() {
		return build( TRUE_MESSAGE_TEMPLATE.formatted( actual ) );
	}

	/** Create a {@link Fault} for {@link RequireBoolean#isFalse()}. */
	public Fault<AssertionError> isFalse() {
		return build( FALSE_MESSAGE_TEMPLATE.formatted( actual ) );
	}

}
