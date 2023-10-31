package com.herbmarshall.require;

/**
 * Module to provide data assertions.
 * @param <T> The type of value to operate on
 */
public final class RequirePointer<T>
	extends Require<T, RequirePointerFaultBuilder<T>, RequirePointer<T>> {

	RequirePointer( T actual ) {
		super( actual, new RequirePointerFaultBuilder<>( actual ) );
	}

}
