package com.gunyoung.tmb.precondition;

/**
 * 메소드 매개 변수의 유효성 검사를 위한 유틸리티 클래스 <br>
 * 만약 유효성 검사에 실패하게 된다면 모두 {@link PreconditionViolationException} 예외를 발생시켜 일관되도록 하였다. <br> 
 * 그리고 각각의 예외 상황에 대한 설명은 이 클래스 메소드들의 매개변수 message에 담아주면 된다. 
 * @author kimgun-yeong
 *
 */
public final class Preconditions {
	
	private Preconditions() {
		throw new AssertionError();
	}

	/**
	 * object가 null 값이 아닌지 확인, null인 경우에는 {@link PreconditionViolationException} 발생
	 * @param <T> any class
	 * @param object null 값인지 확인하려는 객체
	 * @param message 만약 null 값이라면 예외에 담길 메시지
	 * @throws PreconditionViolationException if object == null
	 * @return object
	 * @author kimgun-yeong
	 */
	public static <T> T notNull(T object, String message) throws PreconditionViolationException {
		condition(object != null, message);
		return object;
	}
	
	/**
	 * object가 another 이상인지 확인, notLessThan에서의 오토 박싱에 의한 성능 감소를 고려하여 추가한 메소드
	 * @param object another보다 크거나 같은지 확인하려는 int 
	 * @param another object와의 비교를 위한 int
	 * @param message object가 another보다 작은 경우 예외에 담길 메시지
	 * @throws PreconditionViolationException if object is less than anohter
	 * @return object
	 * @author kimgun-yeong
	 */
	public static int notLessThanInt(int object, int another, String message) throws PreconditionViolationException {
		condition(Integer.compare(object, another) >= 0, message);
		return object;
	}
	
	/**
	 * object가 another 이하인지 확인, notMoreThan에서의 오토 박싱에 의한 성능 감소를 고려하여 추가한 메소드
	 * @param object another보다 작거나 같은지 확인하려는 int 
	 * @param another object와의 비교를 위한 int
	 * @param message object가 another보다 큰 경우 예외에 담길 메시지
	 * @throws PreconditionViolationException if object is more than anohter
	 * @return object
	 * @author kimgun-yeong
	 */
	public static int notMoreThanInt(int object, int another, String message) throws PreconditionViolationException {
		condition(Integer.compare(object, another) <= 0, message);
		return object;
	}
	
	/**
	 * object가 another 이상인지 확인, 크다의 기준은 Comparable.compareTo 의 값이 양수 또는 0인 경우(논리적으로 크기가 같은 경우) <br>
	 * 그렇지 않은 경우 {@link PreconditionViolationException} 발생
	 * @param <T> {@code Comparable<T>}를 구현한 클래스여야 한다. 내부적으로 Comparable.compareTo 를 호출하기 때문
	 * @param object another보다 크거나 같은지 확인하려는 객체
	 * @param another object와의 비교를 위한 객체
	 * @param message object가 another보다 작은 경우 예외에 담길 메시지 
	 * @throws PreconditionViolationException if object is less than anohter by Comparable.compareTo
	 * @return object
	 * @author kimgun-yeong
	 */
	public static <T extends Comparable<T>> T notLessThan(T object, T another, String message) throws PreconditionViolationException {
		condition(object.compareTo(another) >= 0, message);
		return object;
	}
	
	/**
	 * object가 another 이하인지 확인, 작다의 기준은 Comparable.compareTo 의 값이 음수 또는 0인 경우(논리적으로 크기가 같은 경우) <br>
	 * 그렇지 않은 경우 {@link PreconditionViolationException} 발생
	 * @param <T> {@code Comparable<T>}를 구현한 클래스여야 한다. 내부적으로 Comparable.compareTo 를 호출하기 때문
	 * @param object another보다 작거나 같은지 확인하려는 객체
	 * @param another object와의 비교를 위한 객체
	 * @param message object가 another보다 큰 경우 예외에 담길 메시지 
	 * @throws PreconditionViolationException if object is more than anohter by Comparable.compareTo
	 * @return object
	 * @author kimgun-yeong
	 */
	public static <T extends Comparable<T>> T notMoreThan(T object, T another, String message) throws PreconditionViolationException {
		condition(object.compareTo(another) <= 0, message);
		return object;
	}
	
	private static void condition(boolean predicate, String message) {
		if(!predicate) 
			throw new PreconditionViolationException(message);
	}
}
