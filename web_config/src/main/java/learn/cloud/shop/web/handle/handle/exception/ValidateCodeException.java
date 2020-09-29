package learn.cloud.shop.web.handle.handle.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author FTSH
 */
public class ValidateCodeException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidateCodeException(String msg) {
		super(msg);
	}

}
