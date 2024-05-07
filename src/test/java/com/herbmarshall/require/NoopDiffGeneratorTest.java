/*
 * This file is part of herbmarshall.com: require.library  ( hereinafter "require.library" ).
 *
 * require.library is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either version 2 of the License,
 * or (at your option) any later version.
 *
 * require.library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with require.library.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.herbmarshall.require;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

final class NoopDiffGeneratorTest {

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
