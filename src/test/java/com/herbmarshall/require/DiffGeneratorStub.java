package com.herbmarshall.require;

final class DiffGeneratorStub implements DiffGenerator {

	private final String output;

	private Object actual;
	private Object expected;

	DiffGeneratorStub( String output ) {
		this.output = output;
	}

	@Override
	public String diff( Object actual, Object expected ) {
		this.actual = actual;
		this.expected = expected;
		return output;
	}

	Object getActual() {
		return actual;
	}

	Object getExpected() {
		return expected;
	}

}
