package io.hhplus.tdd.database;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;

class PointHistoryTableTest {

	private final PointHistoryTable pointHistoryTable = new PointHistoryTable();

	@Test
	@DisplayName("기존 히스토리가 없으면 빈 리스트를 반환한다.")
	public void 빈_히스토리_조회() throws Exception {
	    //given
		long id = 1L;

	    //when
		List<PointHistory> histories = pointHistoryTable.selectAllByUserId(id);

		//then
		assertThat(histories).hasSize(0);
	}

	@Test
	@DisplayName("히스토리 생성에 성공한다.")
	public void 히스토리_생성() throws Exception {
		//given
		long id = 2L;
		long amount = 10_000L;
		TransactionType type = TransactionType.CHARGE;

		//when
		long updateMillis = System.currentTimeMillis();
		PointHistory pointHistory = pointHistoryTable.insert(id, amount, type, updateMillis);

		//then
		assertThat(pointHistory.userId()).isEqualTo(id);
		assertThat(pointHistory.amount()).isEqualTo(amount);
		assertThat(pointHistory.type()).isEqualTo(type);
		assertThat(pointHistory.updateMillis()).isEqualTo(updateMillis);
	}

	@Test
	@DisplayName("기존 히스토리를 반환한다.")
	public void 히스토리_조회() throws Exception {
	    //given
		long id = 3L;
		long amount = 10_000L;
		TransactionType type = TransactionType.CHARGE;

		pointHistoryTable.insert(id, amount, type, System.currentTimeMillis());

		//when
		List<PointHistory> histories = pointHistoryTable.selectAllByUserId(id);

		//then
		assertThat(histories).hasSize(1);
		assertThat(histories.get(0).amount()).isEqualTo(amount);
		assertThat(histories.get(0).type()).isEqualTo(type);
	}
}