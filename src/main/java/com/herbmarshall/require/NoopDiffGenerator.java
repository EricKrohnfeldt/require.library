package com.herbmarshall.require;

import java.util.Objects;

/** The default {@link DiffGenerator} which does nothing. */
final class NoopDiffGenerator implements DiffGenerator {

	private final String message;

	NoopDiffGenerator( String message ) {
		this.message = Objects.requireNonNull( message );
	}

	@Override
	public String diff( Object actual, Object expected ) {
		return message;
	}

}
