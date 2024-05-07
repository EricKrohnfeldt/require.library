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

import com.herbmarshall.require.tester.RequireTestBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class RequireStreamTest
	extends IdentityEqualsRequireTest<Stream<Object>, RequireStreamFaultBuilder<Object>, RequireStream<Object>> {

	RequireStreamTest() {
		super( RequireTestBuilder.with(
			Require::that,
			Require::fault
		) );
	}

	@Nested
	class isEmpty {

		@Test
		void standardTests() {
			builder.test(
				RequireStream::isEmpty,
				RequireStreamFaultBuilder::isEmpty
			)
				.pass( Stream.empty() )
				.fault( randomValue() )
				.fault( null );
		}

	}

	@Nested
	class isEqualTo_list {

		@Test
		void standardTests() {
			var tester = builder.<List<Object>>test(
				RequireStream::isEqualTo,
				RequireStreamFaultBuilder::isEqualTo
			)
				.pass( Stream.empty(), List.of() )
				.pass( null, null )
				.fault( randomValue(), null )
				.fault( null, randomList() )
				.fault( Stream.empty(), null )
				.fault( null, List.of() )
				.fault( randomValue(), randomList() );

			List<Object> expected = randomList();
			Stream<Object> actual = expected.stream();
			tester.pass( actual, expected );
		}

	}

	@Nested
	class isEqualTo_set {

		@Test
		void standardTests() {
			var tester = builder.<Set<Object>>test(
					RequireStream::isEqualTo,
					RequireStreamFaultBuilder::isEqualTo
				)
				.pass( Stream.empty(), Set.of() )
				.pass( null, null )
				.fault( randomValue(), null )
				.fault( null, randomSet() )
				.fault( Stream.empty(), null )
				.fault( null, Set.of() )
				.fault( randomValue(), randomSet() );

			Set<Object> expected = randomSet();
			Stream<Object> actual = expected.stream();
			tester.pass( actual, expected );
		}

	}

	@Nested
	class toRequireList {

		@Test
		void actual_isPopulated() {
			// Arrange
			List<Object> expected = randomList();
			RequireStream<Object> require = Require.that( expected.stream() );
			// Act
			RequireList<Object> output = require.toRequireList();
			// Assert
			Require.that( output.actual ).isEqualTo( expected );
		}

		@Test
		void actual_empty() {
			// Arrange
			RequireStream<Object> require = Require.that( Stream.empty() );
			// Act
			RequireList<Object> output = require.toRequireList();
			// Assert
			Require.that( output.actual ).isEmpty();
		}

		@Test
		void actual_null() {
			// Arrange
			RequireStream<Object> require = Require.that( ( Stream<Object> ) null );
			// Act
			RequireList<Object> output = require.toRequireList();
			// Assert
			Require.that( output.actual ).isNull();
		}

	}

	@Nested
	class toRequireSet {

		@Test
		void actual_isPopulated() {
			// Arrange
			Set<Object> expected = randomSet();
			RequireStream<Object> require = Require.that( expected.stream() );
			// Act
			RequireSet<Object> output = require.toRequireSet();
			// Assert
			Require.that( output.actual ).isEqualTo( expected );
		}

		@Test
		void actual_empty() {
			// Arrange
			RequireStream<Object> require = Require.that( Stream.empty() );
			// Act
			RequireSet<Object> output = require.toRequireSet();
			// Assert
			Require.that( output.actual ).isEmpty();
		}

		@Test
		void actual_null() {
			// Arrange
			RequireStream<Object> require = Require.that( ( Stream<Object> ) null );
			// Act
			RequireSet<Object> output = require.toRequireSet();
			// Assert
			Require.that( output.actual ).isNull();
		}

	}

	@Nested
	class done {

		@Test
		void actual_populated() {
			// Arrange
			List<Object> expected = randomList();
			Stream<Object> actual = expected.stream();
			// Act
			Stream<Object> output = builder.that( actual ).done();
			// Assert
			Require.that( output.toList() ).isEqualTo( expected );
		}

		@Test
		void actual_empty() {
			// Arrange
			Stream<Object> actual = Stream.empty();
			// Act
			Stream<Object> output = builder.that( actual ).done();
			// Assert
			Require.that( output ).isEmpty();
		}

		@Test
		void actual_null() {
			// Arrange
			// Act
			Stream<Object> output = builder.that( null ).done();
			// Assert
			Require.that( output ).isNull();
		}

	}

	@Override
	protected Stream<Object> randomValue() {
		return Stream.of(
			randomElement(),
			randomElement(),
			randomElement()
		);
	}

	private List<Object> randomList() {
		return randomValue().toList();
	}

	private Set<Object> randomSet() {
		return randomValue().collect( Collectors.toUnmodifiableSet() );
	}

	private static Object randomElement() {
		return UUID.randomUUID();
	}

}
