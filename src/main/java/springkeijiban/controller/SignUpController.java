package springkeijiban.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import springkeijiban.dto.UserDto;
import springkeijiban.form.SignUpForm;
import springkeijiban.service.LoginService;
import springkeijiban.service.SignUpService;
import springkeijiban.service.UserService;

@Controller
public class SignUpController {

	@Autowired
	private SignUpService signUpService;

	@Autowired
	private UserService userService;

	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model, HttpServletRequest request) {

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

		List<UserDto> branches = userService.getBranchData();
		List<UserDto> departments = userService.getDepartmentData();

		model.addAttribute("branches", branches);
		model.addAttribute("departments", departments);

		SignUpForm signUpForm = new SignUpForm();
		model.addAttribute("signUpForm", signUpForm);
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String getFormInfo(@ModelAttribute SignUpForm signUpForm, Model model, HttpServletRequest request) {

		List<String> messages = new ArrayList<String>();
		List<UserDto> branches = userService.getBranchData();
		List<UserDto> departments = userService.getDepartmentData();

		if (isValid(request, messages) == true) {

			signUpService.getSignUp(signUpForm);
			return "redirect:control";
		}
		signUpForm.setPassword("");
		signUpForm.setPassword2("");
		model.addAttribute("signUpForm", signUpForm);
		model.addAttribute("branches", branches);
		model.addAttribute("departments", departments);
		model.addAttribute("errorMessages", messages);
		request.setAttribute("branchId", request.getParameter("branchId"));
		request.setAttribute("departmentId", request.getParameter("departmentId"));
		return "signup";
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		String loginId = request.getParameter("loginId");
		String password1 = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String name = request.getParameter("name");
		int branchId = Integer.parseInt(request.getParameter("branchId"));
		int departmentId = Integer.parseInt(request.getParameter("departmentId"));

		if (loginId.matches("^[0-9a-zA-Z]{6,20}$") == true) {
			if(userService.getLoginId(loginId) != null){
				messages.add("このログインIDはすでに使用されています");
			}
		}
		if (loginId.matches("^[0-9a-zA-Z]{6,20}$") != true) {
			messages.add("ログインIDは半角英数字で6～20文字以内で入力してください");
		}
		if (password1.equals(password2) != true) {
			messages.add("パスワードが一致しません");
		}
		if (password1.matches("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]{6,20}$") != true) {
			messages.add("パスワードは半角文字で6～20文字以内で入力してください");
		}
		if (password2.matches("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]{6,20}$") != true) {
			messages.add("確認用パスワードは半角文字で6～20文字以内で入力してください");
		}
		if (name.matches("^.{1,10}$") != true) {
			messages.add("名前は1～10文字以内で入力してください");
		}
		if (branchId != 1 && departmentId == 1 || branchId != 1 && departmentId == 2 || branchId == 1 && departmentId == 3 || branchId == 1 && departmentId == 4) {
			messages.add("正しい支店と部署・役職の組み合わせを選んで下さい");
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
