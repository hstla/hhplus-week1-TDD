package io.hhplus.tdd.point;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.hhplus.tdd.error.ErrorMessage;
import io.hhplus.tdd.point.dto.ChargeReqDto;
import io.hhplus.tdd.point.dto.UseReqDto;
import io.hhplus.tdd.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);
    private final PointService pointService;

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    public UserPoint point(
            @PathVariable long id
    ) {
        ValidationUtils.checkPositive(id, ErrorMessage.NEGATIVE_USER_ID);
        return pointService.findById(id);
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    public List<PointHistory> history(
            @PathVariable long id
    ) {
        return List.of();
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     * id와 amountReq 유효성 검사를 진행한다.
     * 정상동작일 시 200을 응답한다.
     */
    @PatchMapping("{id}/charge")
    public ResponseEntity charge(
            @PathVariable long id,
            @RequestBody ChargeReqDto amountReq
    ) {
        ValidationUtils.checkPositive(id, ErrorMessage.NEGATIVE_USER_ID);
        amountReq.validate();
        pointService.charge(id, amountReq.getAmount());
        return ResponseEntity.ok().build();
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     * id와 amountReq로 사용자 유효성 검증한다.(음수이거나 0이면 예외처리)
     * 정상동작 시 200 응답
     */
    @PatchMapping("{id}/use")
    public ResponseEntity use(
            @PathVariable long id,
            @RequestBody UseReqDto amountReq
    ) {
        ValidationUtils.checkPositive(id, ErrorMessage.NEGATIVE_USER_ID);
        amountReq.validate();
        pointService.use(id, amountReq.getAmount());
        return ResponseEntity.ok().build();
    }
}