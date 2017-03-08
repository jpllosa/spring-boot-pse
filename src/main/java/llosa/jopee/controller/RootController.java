package llosa.jopee.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import llosa.jopee.PseInvestorApplication;

@Controller
public class RootController {
	
	Logger logger = PseInvestorApplication.getLogger(RootController.class);
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String root(Model model) {
		model.addAttribute("title", "Philippine Stock Exchange Investing App");
		return "index";
	}

}
