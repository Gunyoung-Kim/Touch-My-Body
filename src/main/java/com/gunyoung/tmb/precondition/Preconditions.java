package com.gunyoung.tmb.precondition;

public final class Preconditions {
	
	private Preconditions() {
		throw new AssertionError();
	}
	
	public static <T> T notNull(T object, String message) {
		condition(object != null, message);
		return object;
	}
	
	private static void condition(boolean predicate, String message) {
		if(!predicate) 
			throw new PreconditionViolationException(message);
	}
}
