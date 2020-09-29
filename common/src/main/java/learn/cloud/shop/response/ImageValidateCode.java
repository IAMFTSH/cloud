package learn.cloud.shop.response;

import learn.cloud.shop.response.ValidateCode;

import java.awt.image.BufferedImage;

/**
 * @author FTSH
 */
public class ImageValidateCode extends ValidateCode {
	/**
	 * 图片验证码的图片
	 */
	private BufferedImage image;

	public ImageValidateCode(String code, int expireIn, BufferedImage image) {
		super(code, expireIn);
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
