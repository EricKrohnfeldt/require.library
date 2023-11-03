package com.herbmarshall.require;

/** Module to provide data assertions for {@link Boolean} values. */
public final class RequireBoolean
	extends Require<Boolean, RequireBooleanFaultBuilder, RequireBoolean> {

	RequireBoolean( Boolean actual ) {
		super( actual, new RequireBooleanFaultBuilder( actual ) );
	}

	/**
	 * Will check {@code value} for {@code true} condition.
	 * @throws AssertionError if {@code value} is {@code false} or {@code null}
	 */
	public void isTrue() {
		if ( actual == null || ! actual )
			throw new AssertionError( fault.isTrue().getMessage() );
	}

	/**
	 * Will check {@code value} for {@code false} condition.
	 * @throws AssertionError if {@code value} is {@code true} or {@code null}
	 */
	public void isFalse() {
		if ( actual == null || actual )
			throw new AssertionError( fault.isFalse().getMessage() );
	}

}
