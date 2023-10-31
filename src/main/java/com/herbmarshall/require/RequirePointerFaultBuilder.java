package com.herbmarshall.require;

import com.herbmarshall.fault.Fault;

/**
 * Module to provide data assertion {@link Fault Faults}.
 * @param <T> The type of value to operate on
 */
public final class RequirePointerFaultBuilder<T> extends RequireFaultBuilder<T, RequirePointerFaultBuilder<T>> {

	RequirePointerFaultBuilder( T actual ) {
		super( actual );
	}

}
