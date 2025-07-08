package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserPointTest {
	private static final long USER_ID = 1L;

	private UserPoint userPoint;

	@BeforeEach
	void setUp() {
		userPoint = UserPoint.empty(USER_ID);
	}

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
		// given when
		long addPoint = userPoint.addPoint(point);

		// then
		assertThat(addPoint).isEqualTo(point);
	}
	
	@ParameterizedTest
	@ValueSource(longs = {999L, 10_000_001L})
	@DisplayName("최솟값 미만과 최댓값 초과 충전 포인트 입력 시 포인트를 충전할 수 없다.")
	public void charge_경계값_실패_케이스(long point) throws Exception {
		//given when
		Throwable thrown = catchThrowable(() -> userPoint.addPoint(point));

		// then
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("충전 포인트는 1000 이상, 10000000 이하이어야 합니다.");

	}
}
