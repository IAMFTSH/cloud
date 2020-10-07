package learn.cloud.shop.web.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import learn.cloud.shop.response.ProjectConstant;
import learn.cloud.shop.response.ImageValidateCode;
import learn.cloud.shop.web.config.properties.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;


/**
 * 生成图片用的
 * @author FTSH
 */
@Controller
//@RequestMapping
public class ValidateCodeController {


	@Autowired
	private ProjectProperties properties;

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

	@GetMapping(value = ProjectConstant.IMAGE_VALIDATE_CODE_CREATE_URL)
	public void createValidateCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
		/**
		 * 创建
		 */
		ImageValidateCode imageValidateCode = createImageValidateCode();
		/**
		 * 存储
		 */
		sessionStrategy.setAttribute(new ServletWebRequest(request, response),ProjectConstant.VALIDATE_CODE_KEY_IN_SESSION, imageValidateCode);
		/**
		 * 发送
		 */
		ImageIO.write(imageValidateCode.getImage(), "JPEG", response.getOutputStream());
	}

	/**
	 * 可配置验证码生成参数 可配置的验证码验证地址 可配置的验证生成逻辑
	 * 
	 * @return
	 */
	public ImageValidateCode createImageValidateCode() {

		int width = properties.getValidateCode().getWidth();
		int height = properties.getValidateCode().getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();

		Random random = new Random();

		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		StringBuilder sRand = new StringBuilder();
		for (int i = 0; i < properties.getValidateCode().getLength(); i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand.append(rand);
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}

		g.dispose();

		return new ImageValidateCode(sRand.toString(), properties.getValidateCode().getExpireIn(), image);
	}

	/**
	 * 生成随机背景条纹
	 *
	 * @param fc 前景色
	 * @param bc 背景色
	 * @return RGB颜色
	 */
	public Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
