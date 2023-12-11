package com.herbmarshall.require;

/** Module to provide data assertions for {@link Boolean} values. */
public final class RequireBoolean
	extends Require<Boolean, RequireBooleanFaultBuilder, RequireBoolean> {

	RequireBoolean( Boolean actual ) {
		super( actual, new RequireBooleanFaultBuilder( actual ) );
	}

	/**
	 * Will check {@code value} for {@code true} condition.
	 * @return A self reference
	 * @throws AssertionError if {@code value} is {@code false} or {@code null}
	 */
	public RequireBoolean isTrue() {
		if ( actual == null || ! actual )
			throw new AssertionError( fault.isTrue().getMessage() );
		return self();
	}

	/**
	 * Will check {@code value} for {@code false} condition.
	 * @return A self reference
	 * @throws AssertionError if {@code value} is {@code true} or {@code null}
	 */
	public RequireBoolean isFalse() {
		if ( actual == null || actual )
			throw new AssertionError( fault.isFalse().getMessage() );
		return self();
	}

}
