package io.hhplus.tdd.point;

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

    public void validateChargePoint(long chargePoint) {
        if (chargePoint < MIN_CHARGE || chargePoint > MAX_CHARGE) {
            throw new IllegalArgumentException("충전 포인트는 " + MIN_CHARGE + " 이상, " + MAX_CHARGE + " 이하이어야 합니다.");
        }
    }

    // 여기서 도메인단을 검증한다.
    // 포인트 충전 요청이 1000 이상 10_000_000 이하일때만 적용 가능하다.
    // 요청 포인트는 보유 포인트보다 이하여야 한다.(잔고 - 요청 값 >= 0)
}