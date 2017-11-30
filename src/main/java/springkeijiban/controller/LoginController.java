package springkeijiban.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import springkeijiban.dto.UserDto;
import springkeijiban.form.LoginForm;
import springkeijiban.service.LoginService;

@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {

		LoginForm loginForm = new LoginForm();
		model.addAttribute("loginForm", loginForm);
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String getFormInfo(@ModelAttribute LoginForm loginForm, Model model, HttpServletRequest request) {

		HttpSession session = request.getSession();

		UserDto user = loginService.getLogin(loginForm.getLoginId(), loginForm.getPassword());
		if (user != null) {
			session.setAttribute("loginUser", user);
			return "redirect:/";
		}else {
			loginForm.setLoginId(loginForm.getLoginId());
			loginForm.setPassword("");
			model.addAttribute("loginForm", loginForm);
			model.addAttribute("errorMessages", "ログインに失敗しました");
			return "login";
		}
	}
}
