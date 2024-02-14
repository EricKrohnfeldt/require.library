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
