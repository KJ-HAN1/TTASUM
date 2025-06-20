package com.ttasum.memorial.exception.recipientLetter;

import com.ttasum.memorial.exception.common.badRequest.BadRequestException;

public class RecipientInvalidOrganCodeException extends BadRequestException {
  public RecipientInvalidOrganCodeException() {
    super("유효하지 않은 장기입니다.");
  }
}
