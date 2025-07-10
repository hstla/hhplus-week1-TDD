package io.hhplus.tdd.database;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.hhplus.tdd.point.UserPoint;

class UserPointTableTest {

	private UserPointTable userPointTable = new UserPointTable();

	@AfterEach
	void tearDown() {
		userPointTable.clear();
	}

	@ParameterizedTest
	@CsvSource({
		"1, 0",
		"2, 1000"
	})
	@DisplayName("유저가 있으면 해당 유저의 포인트 조회, 없으면 생성 후 0 포인트를 조회한다.")
	public void 유저_포인트_조회(long id, long point) throws Exception {
	    //given
		userPointTable.insertOrUpdate(2L, 1000L);

	    //when
		UserPoint userPoint = userPointTable.selectById(id);

		//then
		assertThat(userPoint.id()).isEqualTo(id);
		assertThat(userPoint.point()).isEqualTo(point);
	}

	@ParameterizedTest
	@CsvSource({
		"1, 0",
		"2, 1000"
	})
	@DisplayName("기존 유저가 있으면 입력된 포인트를 업데이트하고, 기존 유저가 없으면 유저를 생성한다.")
	public void 유저_포인트_생성_또는_업데이트(long id, long point) throws Exception {
		//given
		userPointTable.insertOrUpdate(2L, 1000L);

		//when
		UserPoint userPoint = userPointTable.insertOrUpdate(id, point);
		UserPoint findPoint = userPointTable.selectById(id);

		//then
		assertThat(userPoint.id()).isEqualTo(findPoint.id());
		assertThat(userPoint.point()).isEqualTo(findPoint.point());
	}
}