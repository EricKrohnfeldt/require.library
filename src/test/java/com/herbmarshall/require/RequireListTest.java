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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings( "checkstyle:GenericWhitespace" )  // Using until UTIL-310
final class RequireListTest
	extends RequireCollectionTest<
		Object,
		List<Object>,
		RequireListFaultBuilder<Object>,
		RequireList<Object>
	> {

	RequireListTest() {
		super( RequireTestBuilder.with(
			Require::that,
			Require::fault
		) );
	}

	@Override
	protected List<Object> randomValue() {
		return List.of(
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
	Collector<Object, ?, List<Object>> mutableCollector() {
		return Collectors.toList();
	}

	@Override
	Collector<Object, ?, List<Object>> immutableCollector() {
		return Collectors.toUnmodifiableList();
	}

	@Override
	protected List<Object> copyValue( List<Object> source ) {
		return source.stream().toList();  // List.copyOf will return same ref for List.of lists
	}

}
