package io.hhplus.tdd.point;

import io.hhplus.tdd.error.ErrorMessage;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {
    // 포인트 충전 최솟값과 최댓값
    public static final long MIN_CHARGE = 1000L;
    public static final long MAX_CHARGE = 10_000_000L;

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public long addPoint(long chargePoint) {
        validateChargePoint(chargePoint);
        return this.point + chargePoint;
    }

    private void validateChargePoint(long chargePoint) {
        if (chargePoint < MIN_CHARGE || chargePoint > MAX_CHARGE) {
            throw new IllegalArgumentException("충전 포인트는 " + MIN_CHARGE + " 이상, " + MAX_CHARGE + " 이하이어야 합니다.");
        }
    }

    // 요청 포인트는 보유 포인트보다 이하여야 한다.(잔고 - 요청 값 >= 0)
    // 계산하는 메서드가 필요 거기 안에서 벨리데이션 하기
    public long calculateUsePoint(long usePoint) {
        validateAvailablePoint(usePoint);
        return this.point - usePoint;
    }

    private void validateAvailablePoint(long amount) {
        if (this.point < amount) {
            throw new IllegalArgumentException(ErrorMessage.INSUFFICIENT_POINT_MESSAGE);
        }
    }
}