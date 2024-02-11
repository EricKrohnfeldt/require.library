package com.herbmarshall.require;

/** Module to provide data assertions for {@link Boolean} values. */
public final class RequireBoolean
	extends Require<Boolean, RequireBooleanFaultBuilder, RequireBoolean> {

	RequireBoolean( Boolean actual ) {
		super( actual, new RequireBooleanFaultBuilder( actual ) );
	}

	/**
	 * Will check {@code actual} for {@code true} condition.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is {@code false} or {@code null}
	 */
	public RequireBoolean isTrue() {
		if ( actual == null || ! actual )
			throw fault.isTrue().build();
		return self();
	}

	/**
	 * Will check {@code actual} for {@code false} condition.
	 * @return A self reference
	 * @throws AssertionError if {@code actual} is {@code true} or {@code null}
	 */
	public RequireBoolean isFalse() {
		if ( actual == null || actual )
			throw fault.isFalse().build();
		return self();
	}

}
