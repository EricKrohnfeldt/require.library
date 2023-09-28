package com.herbmarshall.require;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class NoopDiffGeneratorTest {

	@Test
	void diff() {
		String actual = randomString();
		String expected = randomString();
		diff( actual, expected );
		diff( actual, randomUUID() );
		diff( null, expected );
		diff( actual, null );
		diff( null, null );
	}

	private void diff( Object actual, Object expected ) {
		// Arrange
		String message = randomString();
		NoopDiffGenerator generator = new NoopDiffGenerator( message );
		// Act
		String output = generator.diff( actual, expected );
		// Assert
		Assertions.assertEquals( message, output );
	}

	private String randomString() {
		return UUID.randomUUID().toString();
	}

	private UUID randomUUID() {
		return UUID.randomUUID();
	}

}
