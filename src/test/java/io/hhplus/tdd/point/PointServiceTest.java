package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;

/**
 * 현재 in-memory를 사용하고 있기 때문에
 * Mock을 사용하지 않고 테스트 작성
 */
class PointServiceTest {

	private UserPointTable userPointTable;
	private PointHistoryTable pointHistoryTable;
	private PointService pointService;

	@BeforeEach
	void setUp() {
		userPointTable = new UserPointTable();
		pointHistoryTable = new PointHistoryTable();
		pointService = new PointService(pointHistoryTable, userPointTable);
	}

	/**
	 * 포인트 충전
	 * 포인트 충전에 성공하면 userPointTable, pointHistoryTable에 값이 잘 들어갔는지 확인한다.
	 * 포인트 실패 시
	 * 포인트는 유지되고 유저는 history에 저장되지 못한다.
	 */
	@Test
	@DisplayName("1000 포인트 충전에 성공하여 히스토리와 포인트가 증가한다.")
	public void 포인트_충전에_성공() throws Exception {
	    //given
		long userId = 1L;
		long chargePoint = 1000L;

	    //when
		pointService.charge(userId, chargePoint);
		UserPoint userPoint = userPointTable.selectById(userId);
		List<PointHistory> histories = pointHistoryTable.selectAllByUserId(userId);

		//then
		assertThat(userPoint.id()).isEqualTo(userId);
		assertThat(userPoint.point()).isEqualTo(chargePoint);

		assertThat(histories).hasSize(1);
		assertThat(histories.get(0).amount()).isEqualTo(chargePoint);
		assertThat(histories.get(0).type()).isEqualTo(TransactionType.CHARGE);
	}

	@ParameterizedTest
	@ValueSource(longs = {999L, 10_000_001L})
	@DisplayName("포인트 충전 값이 999이하이거나 10_000_001이상일 때 포인트 충전에 실패하여 history에 저장하지 못한다.")
	public void 포인트_충전에_실패(long chargePoint) throws Exception {
	    //given
		long userId = 1L;

	    //when
		Throwable thrown = catchThrowable(() -> pointService.charge(userId, chargePoint));
		List<PointHistory> histories = pointHistoryTable.selectAllByUserId(userId);
		UserPoint userPoint = userPointTable.selectById(userId);

		//then
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("충전 포인트는 1000 이상, 10000000 이하이어야 합니다.");

		assertThat(histories).hasSize(0);

		assertThat(userPoint.point()).isEqualTo(0);
	}
}