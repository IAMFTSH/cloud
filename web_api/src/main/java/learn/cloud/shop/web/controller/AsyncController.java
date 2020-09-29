package learn.cloud.shop.web.controller;

import learn.cloud.shop.util.JsonResult;
import learn.cloud.shop.web.lab.async.DeferredResultHolder;
import learn.cloud.shop.web.lab.async.MockQueue;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/async")
public class AsyncController {

	@Autowired
	private MockQueue mockQueue;

	@Autowired
	private DeferredResultHolder deferredResultHolder;

	private static final Logger log = LoggerFactory.getLogger(AsyncController.class);

	//DeferredResult可以让所有线程结束后再返回给前端
	@GetMapping("/order/{id}")
	public DeferredResult<JsonResult> getOrder(@PathVariable String id) {
		log.info("主线程开启");

		String orderNumber = RandomStringUtils.randomNumeric(8);
		mockQueue.setPlaceOrder(orderNumber);

		DeferredResult<JsonResult> result = new DeferredResult<JsonResult>();
		deferredResultHolder.getMap().put(orderNumber, result);
		/*
		 * Callable<JsonResult> result = new Callable<JsonResult>() {
		 * 
		 * @Override public JsonResult call() throws Exception { log.info("副线程开启");
		 * Thread.sleep(1000); log.info("副线程结束"); return JsonResult.isOk("success"); }
		 * 
		 * };
		 */
		return result;
	}
}
