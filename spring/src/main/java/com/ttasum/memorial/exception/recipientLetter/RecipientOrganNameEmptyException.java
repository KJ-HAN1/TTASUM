package com.ttasum.memorial.exception.recipientLetter;

public class RecipientOrganNameEmptyException extends RuntimeException {
  public RecipientOrganNameEmptyException() {
    super("장기명을 반드시 입력해야 합니다.");
  }
}
