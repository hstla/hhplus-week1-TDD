package io.hhplus.tdd.point.dto;

import io.hhplus.tdd.error.ErrorMessage;
import io.hhplus.tdd.utils.ValidationUtils;

public class UseReqDto {
	private long amount;

	public UseReqDto() {}

	public UseReqDto(long amount) {
		this.amount = amount;
	}

	public long getAmount() {
		return amount;
	}

	public void validate() {
		ValidationUtils.checkPositive(amount, ErrorMessage.NEGATIVE_POINT);
	}
}