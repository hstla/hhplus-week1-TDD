package io.hhplus.tdd.point;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.hhplus.tdd.error.ErrorMessage;
import io.hhplus.tdd.point.dto.ChargeReqDto;

/**
 * PointController 단위 테스트
 */
@WebMvcTest(controllers = PointController.class)
class PointControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PointService pointService;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Charge
	 * 1. 포인트 충전에 성공 테스트 작성
	 * 2. 0 이하 아이디를 입력하여 에러 발생
	 * 3. 0 이하 포인트를 입력하여 에러 발생
	 */
	@Test
	@DisplayName("양수인 id와 1000~10_000_000 사이 포인트 값을 넣어 포인트 충전에 성공한다.")
	public void 포인트_충전_성공() throws Exception {
	    //given
		long id = 1L;
		long chargePoint = 10_000L;
		ChargeReqDto chargeReqDto = new ChargeReqDto(chargePoint);

		//when then
		mockMvc.perform(MockMvcRequestBuilders.patch("/point/" + id + "/charge")
				.content(objectMapper.writeValueAsString(chargeReqDto))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
		verify(pointService).charge(id, chargePoint);
	}

	@ParameterizedTest
	@ValueSource(longs = {-1L, 0L})
	@DisplayName("0이하 id값을 입력하여 포인트 충전에 실패한다.")
	public void 포인트_충전_id_실패(long id) throws Exception {
		//given
		long chargePoint = 10_000L;
		ChargeReqDto chargeReqDto = new ChargeReqDto(chargePoint);

		//when then
		mockMvc.perform(MockMvcRequestBuilders.patch("/point/" + id + "/charge")
				.content(objectMapper.writeValueAsString(chargeReqDto))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.code").value("500"))
			.andExpect(jsonPath("$.message").value(ErrorMessage.NEGATIVE_USER_ID));
	}

	@ParameterizedTest
	@ValueSource(longs = {-1L, 0L})
	@DisplayName("0이하 point값을 입력하여 포인트 충전에 실패한다.")
	public void 포인트_충전_point_실패() throws Exception {
		//given
		long id = 1L;
		long chargePoint = -10_000L;
		ChargeReqDto chargeReqDto = new ChargeReqDto(chargePoint);

		//when then
		mockMvc.perform(MockMvcRequestBuilders.patch("/point/" + id + "/charge")
				.content(objectMapper.writeValueAsString(chargeReqDto))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.code").value("500"))
			.andExpect(jsonPath("$.message").value(ErrorMessage.NEGATIVE_POINT));
	}

	/**
	 * Use
	 * 1. 포인트 충전에 성공 테스트 작성
	 * 2. 0 이하 아이디를 입력하여 에러 발생
	 * 3. 0 이하 포인트를 입력하여 에러 발생
	 */
	@Test
	@DisplayName("양수인 id와 양수인 포인트 값을 넣어 포인트 사용에 성공한다.")
	public void 포인트_사용_성공() throws Exception {
		//given
		long id = 1L;
		long chargePoint = 10_000L;
		ChargeReqDto chargeReqDto = new ChargeReqDto(chargePoint);

		//when then
		mockMvc.perform(MockMvcRequestBuilders.patch("/point/" + id + "/use")
				.content(objectMapper.writeValueAsString(chargeReqDto))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
		verify(pointService).use(id, chargePoint);
	}

	@ParameterizedTest
	@ValueSource(longs = {-1L, 0L})
	@DisplayName("0이하 id값을 입력하여 포인트 사용에 실패한다.")
	public void 포인트_사용_id_실패(long id) throws Exception {
		//given
		long chargePoint = 10_000L;
		ChargeReqDto chargeReqDto = new ChargeReqDto(chargePoint);

		//when then
		mockMvc.perform(MockMvcRequestBuilders.patch("/point/" + id + "/use")
				.content(objectMapper.writeValueAsString(chargeReqDto))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.code").value("500"))
			.andExpect(jsonPath("$.message").value(ErrorMessage.NEGATIVE_USER_ID));
	}

	@ParameterizedTest
	@ValueSource(longs = {-1L, 0L})
	@DisplayName("0이하 point값을 입력하여 포인트 사용에 실패한다.")
	public void 포인트_사용_point_실패() throws Exception {
		//given
		long id = 1L;
		long chargePoint = -10_000L;
		ChargeReqDto chargeReqDto = new ChargeReqDto(chargePoint);

		//when then
		mockMvc.perform(MockMvcRequestBuilders.patch("/point/" + id + "/use")
				.content(objectMapper.writeValueAsString(chargeReqDto))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.code").value("500"))
			.andExpect(jsonPath("$.message").value(ErrorMessage.NEGATIVE_POINT));
	}

	/**
	 * 입력받은 id로 포인트를 조회한다.
	 * 아이디가 음수 & 0 인지 검증한다.
	 */
	@Test
	@DisplayName("포인트 조회에 성공한다.")
	public void 포인트_조회_성공() throws Exception {
	    //given
		long id = 1L;
		long currentPoint = 10_000L;
		given(pointService.findById(id)).willReturn(new UserPoint(id, currentPoint, System.currentTimeMillis()));

	    //when then
		mockMvc.perform(MockMvcRequestBuilders.get("/point/" + id))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(id))
			.andExpect(jsonPath("$.point").value(currentPoint));
		verify(pointService).findById(id);
	}

	@Test
	@DisplayName("음수 & 0인 id값을 입력받아 포인트 조회에 실패한다.")
	public void 포인트_조회_실패() throws Exception {
		//given
		long id = -1L;

		//when then
		mockMvc.perform(MockMvcRequestBuilders.get("/point/" + id))
			.andDo(print())
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.message").value(ErrorMessage.NEGATIVE_USER_ID));
	}

	/**
	 * point history 조회
	 * 1. 입력받은 id로 포인트 history를 조회에 성공한다.
	 * 2. 아이디가 음수 & 0 인지 검증한다.
	 */
	@Test
	@DisplayName("포인트 history 조회에 성공한다.")
	public void 포인트_history_조회_성공() throws Exception {
		//given
		long id = 1L;
		List<PointHistory> histories = new ArrayList<>();
		histories.add(new PointHistory(1L, id, 10_000L, TransactionType.CHARGE, System.currentTimeMillis()));
		histories.add(new PointHistory(2L, id, 5_000L, TransactionType.USE, System.currentTimeMillis() + 1));
		given(pointService.findHistoryById(id)).willReturn(histories);

		//when then
		mockMvc.perform(MockMvcRequestBuilders.get("/point/" + id + "/histories"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[1].amount").value(5_000L));
	}

	@Test
	@DisplayName("음수 & 0인 id값을 입력받아 포인트 history 조회에 실패한다.")
	public void 포인트_history_조회_실패() throws Exception {
		//given
		long id = -1L;

		//when then
		mockMvc.perform(MockMvcRequestBuilders.get("/point/" + id + "/histories"))
			.andDo(print())
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.message").value(ErrorMessage.NEGATIVE_USER_ID));
	}
}