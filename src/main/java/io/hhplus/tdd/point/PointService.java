package io.hhplus.tdd.point;

import org.springframework.stereotype.Service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointHistoryTable pointHistoryTable;
	private final UserPointTable userPointTable;

	/**
	 * 포인트 충전
	 * 아이디 확인 후 아이디가 없으면 아이디를 생성한다.
	 * 포인트가 정상범위 안에 있는 지 확인 후 userPointTable, pointHistoryTable에 저장한다.
	 */
	public void charge(long id, long point) {
		UserPoint userPoint = userPointTable.selectById(id);

		long addPoint = userPoint.addPoint(point);

		userPointTable.insertOrUpdate(id, addPoint);
		pointHistoryTable.insert(id, point, TransactionType.CHARGE, System.currentTimeMillis());
	}

	/**
	 * 포인트 사용
	 * 아이디 확인 후 아이디 없으면 생성
	 * 포인트 잔고보다 많으면 에러 처리
	 * usePointTable, pointHistoryTable에 업데이트
	 */
	public void use(long id, long amount) {
		UserPoint userPoint = userPointTable.selectById(id);

		long calPoint = userPoint.calculateUsePoint(amount);

		userPointTable.insertOrUpdate(id, calPoint);
		pointHistoryTable.insert(id, calPoint, TransactionType.USE, System.currentTimeMillis());
	}

	/**
	 * id를 입력받아 포인트를 조회한다.
	 * id가 없더라도 생성해서 반환해야한다.
	 */
	public UserPoint findById(long id) {
		return userPointTable.selectById(id);
	}
}