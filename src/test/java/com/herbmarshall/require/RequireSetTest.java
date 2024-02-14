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
