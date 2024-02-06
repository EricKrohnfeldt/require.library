package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;

import java.util.Optional;

@SuppressWarnings( { "OptionalUsedAsFieldOrParameterType", "OptionalAssignedToNull" } )
public final class RequireOptionalFaultBuilder<T> extends RequireFaultBuilder<Optional<T>, RequireOptionalFaultBuilder<T>> {

	static final String IS_PRESENT = "Required Optional to contain value, it is empty";
	static final String IS_PRESENT_NULL = "Required Optional to contain value, however is is null ( as opposed to empty )";

	static final String IS_EMPTY_BASIC = "Required Optional to be empty";
	static final String IS_EMPTY = IS_EMPTY_BASIC + ", it contains '%s'";
	static final String IS_EMPTY_NULL = IS_EMPTY_BASIC + ", however it is null";

	RequireOptionalFaultBuilder( Optional<T> actual ) {
		super( actual );
	}

	public Fault<AssertionError> isPresent() {
		return build( actual == null ? IS_PRESENT_NULL : IS_PRESENT );
	}

	public Fault<AssertionError> isEmpty() {
		return build( actual == null ?
			IS_EMPTY_NULL :
			actual
				.map( IS_EMPTY::formatted )
				.orElse( IS_EMPTY_BASIC )
		);
	}

}
