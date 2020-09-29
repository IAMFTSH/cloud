package learn.cloud.shop.web.config.properties;

import learn.cloud.shop.response.ProjectConstant;

/**
 * @author FTSH
 */
public class ValidateCodeProperties {

	private Integer width = 73;
	private Integer height = 21;
	private Integer length = 4;
	/**
	 * 有效期，以S为单位
	 */
	private Integer expireIn = 60 * 60 * 2;
	/**
	 * 图片验证码的拦截路径 如果有多个，中间以逗号隔开
	 */
	private String imageValidateCodeProcessUrls = ProjectConstant.LOGIN_PROCESSING_URL;

	public String getImageValidateCodeProcessUrls() {
		return imageValidateCodeProcessUrls;
	}

	public void setImageValidateCodeProcessUrls(String imageValidateCodeProcessUrls) {
		this.imageValidateCodeProcessUrls = imageValidateCodeProcessUrls;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getExpireIn() {
		return expireIn;
	}

	public void setExpireIn(Integer expireIn) {
		this.expireIn = expireIn;
	}

}