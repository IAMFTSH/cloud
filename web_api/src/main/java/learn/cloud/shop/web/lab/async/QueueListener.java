package learn.cloud.shop.web.lab.async;

import learn.cloud.shop.util.JsonResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

	
	private static final Logger log = LoggerFactory.getLogger(QueueListener.class);

	
	@Autowired
	private MockQueue queue;

	@Autowired
	private DeferredResultHolder holder;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		new Thread(()->{
			while (true) {
				if(StringUtils.isNotBlank(queue.getCompleteOrder())) {
					String orderNumber=queue.getCompleteOrder();
					log.info("监听到应用2结果处理完毕，单号为{}",orderNumber);
					holder.getMap().get(orderNumber).setResult(JsonResult.isOk("placeOrder success"+orderNumber));
					queue.setCompleteOrder(null);
				}else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
