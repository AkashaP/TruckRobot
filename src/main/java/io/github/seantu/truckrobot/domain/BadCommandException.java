package io.github.seantu.truckrobot.domain;

public class BadCommandException extends RuntimeException {
  private final String code;

  public BadCommandException(String code, String message) {
    super(message);
    this.code = code;
  }

  public String getCode() { return code; }
}