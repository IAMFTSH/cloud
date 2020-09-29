package learn.cloud.shop.web.controller;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/err")
public class ErrorController {

	@GetMapping
	public ModelAndView showError() {
		int x = 1 / 0;
		return new ModelAndView("index.html", MapUtils.EMPTY_MAP);
	}


	@GetMapping("/notArg")
	public String notArg(String bug) {
		return "bug";
	}
}
