package learn.cloud.shop.web.lab.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 伪造的消息队列
 * 
 * @author jsjxy
 *
 */
@Component
public class MockQueue {

	private static final Logger log = LoggerFactory.getLogger(MockQueue.class);

	/**
	 * 等待接收数据的区域，如果有值表示有请求正在处理，应用2 会一直监听这个数据域
	 */
	private String placeOrder;
	/**
	 * 表示数据处理完毕，如果有值，表示应用2已经将结果放在了队列中 应用1的线程2会一直监听该区域
	 */
	private String completeOrder;

	/**
	 * 因为没有应用2所以直接在下单的时候就开心的线程处理信息 模拟应用2的操作
	 * 
	 * @param placeOrder
	 */
	public void setPlaceOrder(String placeOrder) {
		new Thread(() -> {
			log.info("接收到下单请求，应用2正在处理，且单号为{}", placeOrder);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.completeOrder = placeOrder;
			log.info("应用2正在处理完毕，且单号为{}", placeOrder);
		}).start();
	}

	public String getPlaceOrder() {
		return placeOrder;
	}

	public String getCompleteOrder() {
		return completeOrder;
	}

	public void setCompleteOrder(String completeOrder) {
		this.completeOrder = completeOrder;
	}

}
