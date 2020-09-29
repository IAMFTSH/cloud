package learn.cloud.shop.response;

import java.time.LocalDateTime;

/**
 * 所有验证码实体类封装的父类 是短信验证码，图片验证码
 * 
 * @author jsjxy
 *
 */
public class ValidateCode {

	/**
	 * 验证码的字符串
	 */
	private String code;

	/**
	 * 过期时间
	 */
	private LocalDateTime expireTime;
	
	public ValidateCode() {
	}

	public Boolean isExpired() {
		return LocalDateTime.now().isAfter(expireTime);
	}

	/**
	 * 构造验证码 
	 * @param code 验证码字符串
	 * @param expireIn 验证码有效期
	 */
	public ValidateCode(String code, int expireIn) {
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
	}

	public ValidateCode(String code, LocalDateTime expireTime) {
		this.code = code;
		this.expireTime = expireTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDateTime getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(LocalDateTime expireTime) {
		this.expireTime = expireTime;
	}

}
