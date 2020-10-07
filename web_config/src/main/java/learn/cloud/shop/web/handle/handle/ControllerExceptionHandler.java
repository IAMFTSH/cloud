package learn.cloud.shop.web.handle.handle;

import learn.cloud.shop.util.JsonResult;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

/**
 * @author FTSH
 */
@ControllerAdvice
public class ControllerExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public JsonResult handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		Map<String, Object> errMsg = new HashedMap();

		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		for (ObjectError objectError : allErrors) {
			FieldError fieldError = (FieldError) objectError;
			log.info("错误的字段是{}，错误信息是{}", fieldError.getField(), fieldError.getDefaultMessage());
			errMsg.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return JsonResult.error("你个垃圾",errMsg);
	}
}
