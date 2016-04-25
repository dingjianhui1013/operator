package com.itrus.ca.modules.sys.security;

import org.apache.shiro.authc.AuthenticationException;

public class LoginTypeException extends AuthenticationException{
	private static final long serialVersionUID = 1L;

	public LoginTypeException() {
		super();
	}

	public LoginTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginTypeException(String message) {
		super(message);
	}

	public LoginTypeException(Throwable cause) {
		super(cause);
	}
}
