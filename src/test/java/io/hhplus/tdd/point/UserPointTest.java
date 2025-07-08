package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.hhplus.tdd.error.ErrorMessage;

class UserPointTest {
	private static final long USER_ID = 1L;

	/**
	 * 포인트 충전 테스트
	 * 정상동작 성공 케이스(1_000, 10_000_000)
	 * 최솟값(1000) 이하일 때(999) 실패 케이스
	 * 최댓값(10_000_000) 이상일 때(10_000_001) 실패 케이스
	 */
	@ParameterizedTest
	@ValueSource(longs = {1000L, 10_000_000L})
	@DisplayName("최소, 최댓값 사이 포인트 입력 시 포인트를 충전할 수 있다.")
	void charge_경계값_성공_케이스(long point) throws Exception {
		// given
		UserPoint userPoint =  UserPoint.empty(USER_ID);
		// when
		long addPoint = userPoint.addPoint(point);

		// then
		assertThat(addPoint).isEqualTo(point);
	}
	
	@ParameterizedTest
	@ValueSource(longs = {999L, 10_000_001L})
	@DisplayName("최솟값 미만과 최댓값 초과 충전 포인트 입력 시 포인트를 충전할 수 없다.")
	public void charge_경계값_실패_케이스(long point) throws Exception {
		// given
		UserPoint userPoint =  UserPoint.empty(USER_ID);
		// when
		Throwable thrown = catchThrowable(() -> userPoint.addPoint(point));

		// then
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("충전 포인트는 1000 이상, 10000000 이하이어야 합니다.");
	}

	/**
	 * 포인트 사용 테스트
	 * 1. 기존 포인트 10000 에서 5000 사용 - 포인트 사용 성공 테스트
	 * 2. 기존 포인트 10000 에서 11000 사용 - 포인트 사용 실패 테스트
	 */
	@Test
	@DisplayName("10000 - 5000 포인트 사용에 성공한다.")
	public void 포인트_사용_성공() throws Exception {
	    //given
		long point = 10000L;
		long usePoint = 5000L;
		UserPoint userPoint = new UserPoint(USER_ID, point, System.currentTimeMillis());

		//when
		long calculatePoint = userPoint.calculateUsePoint(usePoint);

		//then
		assertThat(calculatePoint).isEqualTo(point - usePoint);
	}

	@Test
	@DisplayName("10000 - 11000 포인트 사용에 실패한다.")
	public void 포인트_사용_실패() throws Exception {
		//given
		long point = 10000L;
		long usePoint = 11000L;
		UserPoint userPoint = new UserPoint(USER_ID, point, System.currentTimeMillis());

		//when
		Throwable throwable = catchThrowable(() -> userPoint.calculateUsePoint(usePoint));

		//then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorMessage.INSUFFICIENT_POINT_MESSAGE);
	}
}
