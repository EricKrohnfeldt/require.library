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

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings( "checkstyle:GenericWhitespace" )  // Using until UTIL-310
final class RequireSetTest
	extends RequireCollectionTest<
		Object,
		Set<Object>,
		RequireSetFaultBuilder<Object>,
		RequireSet<Object>
	> {

	RequireSetTest() {
		super( RequireTestBuilder.with(
			Require::that,
			Require::fault
		) );
	}

	@Override
	protected Set<Object> randomValue() {
		return Set.of(
			randomElement(),
			randomElement(),
			randomElement()
		);
	}

	@Override
	Object randomElement() {
		return UUID.randomUUID();
	}

	@Override
	Collector<Object, ?, Set<Object>> mutableCollector() {
		return Collectors.toSet();
	}

	@Override
	Collector<Object, ?, Set<Object>> immutableCollector() {
		return Collectors.toUnmodifiableSet();
	}

	@Override
	protected Set<Object> copyValue( Set<Object> source ) {
		return source.stream().collect( immutableCollector() );  // Set.copyOf will return same ref for Set.of sets
	}

}
