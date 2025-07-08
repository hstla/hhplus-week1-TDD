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

		userPoint.validateChargePoint(point);

		userPointTable.insertOrUpdate(id, point);
		pointHistoryTable.insert(id, point, TransactionType.CHARGE, System.currentTimeMillis());
	}
}