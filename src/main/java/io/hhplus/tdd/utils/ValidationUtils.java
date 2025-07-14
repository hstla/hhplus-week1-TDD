package io.hhplus.tdd.utils;

public class ValidationUtils {
	public static void checkPositive(long checkAmount, String errorMessage) {
		if (0 >= checkAmount) {
			throw new IllegalArgumentException(errorMessage);
		}
	}
}
