package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.error.ErrorMessage;

@ExtendWith(MockitoExtension.class)
public class UserPointServiceMockTest {
	@Mock
	private UserPointTable userPointTable;
	@Mock
	private PointHistoryTable pointHistoryTable;
	@InjectMocks
	private PointService pointService;

	/**
	 * 포인트 충전
	 * 포인트 충전에 성공하면 userPointTable, pointHistoryTable에 값이 잘 들어갔는지 확인한다.
	 * 포인트 실패 시
	 * 포인트는 유지되고 유저는 history에 저장되지 못한다.
	 */
	@Test
	@DisplayName("1000 포인트 충전에 성공하여 히스토리와 포인트가 증가한다.")
	public void 포인트_충전에_성공() throws Exception {
		// given
		long userId = 1L;
		long currentPoint = 3000L;
		long chargePoint = 1000L;
		long expectedPoint = currentPoint + chargePoint;

		UserPoint userPoint = new UserPoint(userId, currentPoint, System.currentTimeMillis());
		given(userPointTable.selectById(userId)).willReturn(userPoint);

		// when
		pointService.charge(userId, chargePoint);

		// then 행위 테스트
		verify(userPointTable).insertOrUpdate(userId, expectedPoint);
		verify(pointHistoryTable).insert(eq(userId), eq(chargePoint), eq(TransactionType.CHARGE), anyLong());
	}

	@ParameterizedTest
	@ValueSource(longs = {999L, 10_000_001L})
	@DisplayName("포인트 충전 값이 999이하이거나 10_000_001이상일 때 포인트 충전에 실패하여 history에 저장하지 못한다.")
	public void 포인트_충전에_실패(long chargePoint) throws Exception {
		//given
		long userId = 1L;
		UserPoint userPoint = new UserPoint(userId, 0L, System.currentTimeMillis());
		given(userPointTable.selectById(userId)).willReturn(userPoint);

		//when
		Throwable thrown = catchThrowable(() -> pointService.charge(userId, chargePoint));

		//then
		assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("충전 포인트는 1000 이상, 10000000 이하이어야 합니다.");
		assertThat(pointHistoryTable.selectAllByUserId(userId)).hasSize(0);
		verify(userPointTable, never()).insertOrUpdate(userId, chargePoint);
	}

	/**
	 * 포인트 사용 테스트
	 * 1. 포인트 사용 성공 시 - 결과값 검증, pointHistory, userPointTable에 저장 확인
	 * 2. 포인트 사용 실패 시 - 포인트 잔고보다 사용 포인트가 클 때 예외처리 확인
	 */
	@Test
	@DisplayName("포인트 사용에 성공하여 history, userPointTable에 저장한다.")
	public void 포인트_사용_성공() throws Exception {
		//given
		long userId = 1L;
		long currentPoint = 10000L;
		long usePoint = 2000L;
		long subtractedPoint = currentPoint - usePoint;
		UserPoint userPoint = new UserPoint(userId, currentPoint, System.currentTimeMillis());
		given(userPointTable.selectById(userId)).willReturn(userPoint);

		//when
		pointService.use(userId, usePoint);

		//then
		verify(userPointTable).insertOrUpdate(userId, subtractedPoint);
		verify(pointHistoryTable).insert(eq(userId), eq(subtractedPoint), eq(TransactionType.USE), anyLong());
	}

	@Test
	@DisplayName("잔고보다 큰 포인트를 입력하여 포인트 사용에 실패한다.")
	public void 포인트_사용_실패() throws Exception {
		//given
		long userId = 1L;
		long currentPoint = 10000L;
		long usePoint = 20000L;
		UserPoint userPoint = new UserPoint(userId, currentPoint, System.currentTimeMillis());
		given(userPointTable.selectById(userId)).willReturn(userPoint);

		//when
		Throwable throwable = catchThrowable(() -> pointService.use(userId, usePoint));

		//then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorMessage.INSUFFICIENT_POINT_MESSAGE);
	}

	/**
	 * find point
	 * 포인트 조회에 성공한다.
	 * 기존 id가 없더라도 id를 생성해서 포인트를 조회한다.
	 */
	@Test
	@DisplayName("양수인 id를 입력받아 포인트 조회에 성공한다.")
	public void 포인트_조회_성공() throws Exception {
		//given
		long id = 1L;
		long point = 10000L;
		UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
		given(userPointTable.selectById(id)).willReturn(userPoint);

		//when
		UserPoint findUserPoint = pointService.findById(id);

		//then
		assertThat(findUserPoint.point()).isEqualTo(userPoint.point());
	}

	@Test
	@DisplayName("기존 id가 없더라도 id를 생성해서 0 포인트를 조회한다.")
	public void 존재하지_않는_아이디_포인트_조회_성공() throws Exception {
		//given
		long id = 2L;
		long point = 0L;
		UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
		given(userPointTable.selectById(id)).willReturn(userPoint);

		//when
		UserPoint findUserPoint = pointService.findById(id);

		//then
		assertThat(findUserPoint.point()).isEqualTo(0);
	}

	/**
	 * point history 조회
	 * 1. 포인트 히스토리 조회에 성공한다.
	 * 2. 히스토리가 없는 아이디이면 빈 리스트를 반환한다.
	 */
	@Test
	@DisplayName("포인트 히스토리 조회에 성공한다.")
	public void 포인트_history_조회_성공() throws Exception {
		//given
		long id = 1L;
		List<PointHistory> mockHistories = List.of(
			new PointHistory(1L, id, 10_000L, TransactionType.CHARGE, System.currentTimeMillis()),
			new PointHistory(2L, id, 5_000L, TransactionType.USE, System.currentTimeMillis())
		);

		given(pointHistoryTable.selectAllByUserId(id)).willReturn(mockHistories);

		//when
		List<PointHistory> historyList = pointService.findHistoryById(id);

		//then
		assertThat(historyList.size()).isEqualTo(2);
		assertThat(historyList.get(0).amount()).isEqualTo(10_000L);
		assertThat(historyList.get(1).type()).isEqualTo(TransactionType.USE);
	}

	@Test
	@DisplayName("히스토리에 입력받은 id가 없으면 빈 리스트를 반환한다.")
	public void 존재하지_않는_아이디_포인트_history_조회_성공() throws Exception {
		//given
		long inputId = 2L;
		given(pointHistoryTable.selectAllByUserId(inputId)).willReturn(List.of());

		//when
		List<PointHistory> historyList = pointService.findHistoryById(inputId);

		//then
		assertThat(historyList.size()).isEqualTo(0);
	}
}
