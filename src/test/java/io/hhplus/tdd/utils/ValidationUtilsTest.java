package io.hhplus.tdd.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ValidationUtilsTest {

	@Test
	@DisplayName("입력값이 1이상일 때 통과한다.")
	public void 양수_입력시_예외없음() throws Exception {
	    //given
	    long amount = 10000L;
		String errorMessage = "0보다 큰 수를 입력하시오.";
	    //when
		Throwable throwable = catchThrowable(() -> ValidationUtils.checkPositive(amount, errorMessage));

		//then
		assertThat(throwable).isNull();
	}

	@Test
	@DisplayName("입력값이 0이하면 에러를 발생시킨다.")
	public void 음수_및_0_입력시_예외발생() throws Exception {
		//given
		long amount = 0L;
		String errorMessage = "0보다 큰 수를 입력하시오.";
		//when
		Throwable throwable = catchThrowable(() -> ValidationUtils.checkPositive(amount, errorMessage));

		//then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
			.hasMessage(errorMessage);
	}
}