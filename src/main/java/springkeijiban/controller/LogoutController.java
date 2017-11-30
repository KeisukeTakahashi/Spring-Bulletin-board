package springkeijiban.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import springkeijiban.form.LoginForm;

@Controller
public class LogoutController {

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Model model, HttpSession session) {

		LoginForm loginForm = new LoginForm();
		model.addAttribute("loginForm", loginForm);
		session.invalidate();

		return "redirect:/login";
	}
}
