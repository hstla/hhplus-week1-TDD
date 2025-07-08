package io.hhplus.tdd.point;

import io.hhplus.tdd.error.ErrorMessage;
import io.hhplus.tdd.utils.ValidationUtils;

public class ChargeReqDto {
	private long amount;

	public ChargeReqDto() {}

	public ChargeReqDto(long amount) {
		this.amount = amount;
	}

	public long getAmount() {
		return amount;
	}

	public void validate() {
		ValidationUtils.checkPositive(amount, ErrorMessage.NEGATIVE_POINT);
	}
}