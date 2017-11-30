package springkeijiban.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import springkeijiban.dto.UserDto;
import springkeijiban.service.LoginService;
import springkeijiban.service.UserService;

@Controller
public class ControlController {
	@Autowired
	private UserService userService;

	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/control", method = RequestMethod.GET)
	public String control(Model model, HttpServletRequest request) {

		HttpSession session = request.getSession();

		UserDto user = (UserDto) request.getSession().getAttribute("loginUser");
		if (user != null) {
			user = loginService.getLoginUser(user.getId());
			request.setAttribute("loginUser", user);
			if (user.getIsWorking() == 0) {
				session.removeAttribute("loginUser");
				session.setAttribute("errorMessages", "ログインを行ってください");
				return "redirect:login";
			}
		}

		List<UserDto> users = userService.getAllUsers();
		List<UserDto> branches = userService.getBranchData();
		List<UserDto> departments = userService.getDepartmentData();

		model.addAttribute("users", users);
		model.addAttribute("branches", branches);
		model.addAttribute("departments", departments);
		return "control";
	}

	@RequestMapping(value = "/control", method = RequestMethod.POST)
	public String isWorking(Model model, HttpServletRequest request) {

		int id = Integer.parseInt(request.getParameter("id"));
		int isWorking = Integer.parseInt(request.getParameter("isWorking"));

		userService.updateIsWorking(id, isWorking);

		List<UserDto> users = userService.getAllUsers();
		List<UserDto> branches = userService.getBranchData();
		List<UserDto> departments = userService.getDepartmentData();

		model.addAttribute("users", users);
		model.addAttribute("branches", branches);
		model.addAttribute("departments", departments);

		return "control";
	}
}
