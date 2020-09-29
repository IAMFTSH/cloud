package learn.cloud.shop.web.lab.async;

import java.util.Map;

import learn.cloud.shop.util.JsonResult;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
public class DeferredResultHolder {

	private Map<String, DeferredResult<JsonResult>> map = new HashedMap();

	public Map<String, DeferredResult<JsonResult>> getMap() {
		return map;
	}

	public void setMap(Map<String, DeferredResult<JsonResult>> map) {
		this.map = map;
	}

}
