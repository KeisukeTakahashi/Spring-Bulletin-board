package springkeijiban.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import springkeijiban.dto.UserDto;
import springkeijiban.entity.User;
import springkeijiban.form.SettingsForm;
import springkeijiban.service.LoginService;
import springkeijiban.service.UserService;

@Controller
public class SettingsController {

	@Autowired
	private UserService userService;

	@Autowired
	private LoginService loginService;


	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String settings(@RequestParam(value = "id", required = false) String id, Model model,
			HttpServletRequest request) {

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
		List<String> messages = new ArrayList<String>();

		model.addAttribute("branches", branches);
		model.addAttribute("departments", departments);

		if (StringUtils.isNotBlank(id) != true) {
			messages.add("IDが入力されていないため、管理画面に遷移しました");
			session.setAttribute("errorMessages", messages);
			return "redirect:control";
		} else if (id.matches("^[0-9]*$") != true) {
			messages.add("不正な値が入力されたため、管理画面に遷移しました");
			session.setAttribute("errorMessages", messages);
			return "redirect:control";
		}

		User editUser = userService.getUser(Integer.parseInt(id));
		if (editUser == null) {
			messages.add("存在しないIDが入力されたため、管理画面に遷移しました");
			session.setAttribute("errorMessages", messages);
			return "redirect:control";
		}
		editUser.setPassword("");
		editUser.setPassword2("");
		model.addAttribute("settingsForm", editUser);
		return "settings";
	}

	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public String getFormInfo(@ModelAttribute SettingsForm settingsForm, Model model, @RequestParam("id") int id,
			HttpServletRequest request) {

		List<String> messages = new ArrayList<String>();
		List<UserDto> branches = userService.getBranchData();
		List<UserDto> departments = userService.getDepartmentData();

		if (isValid(request, messages) == true) {
			userService.update(settingsForm);
			return "redirect:control";
		} else {
			settingsForm.setPassword("");
			settingsForm.setPassword2("");
			model.addAttribute("settingsForm", settingsForm);
			model.addAttribute("errorMessages", messages);
			model.addAttribute("branches", branches);
			model.addAttribute("departments", departments);
			request.setAttribute("branchId", request.getParameter("branchId"));
			request.setAttribute("departmentId", request.getParameter("departmentId"));
			return "settings";
		}
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		int userId = Integer.parseInt(request.getParameter("id"));
		String loginId = request.getParameter("loginId");
		String password1 = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String name = request.getParameter("name");
		int branchId = Integer.parseInt(request.getParameter("branchId"));
		int departmentId = Integer.parseInt(request.getParameter("departmentId"));

		if (loginId.matches("^[0-9a-zA-Z]{6,20}$") == true) {
			User user = userService.getLoginId(loginId);
			if (user != null) {
				if (user.getId() != userId) {
					messages.add("このログインIDはすでに使用されています");
				}
			}
		}
		if (loginId.matches("^[0-9a-zA-Z]{6,20}$") != true) {
			messages.add("ログインIDは半角英数字で6～20文字以内で入力してください");
		}
		if (password1.equals(password2) != true) {
			messages.add("パスワードが一致しません");
		}
		if (StringUtils.isEmpty(request.getParameter("password")) != true) {
			if (password1.matches("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]{6,20}$") != true) {
				messages.add("パスワードは空白、または半角文字で6～20文字以内で入力してください");
			}
		}
		if (StringUtils.isEmpty(request.getParameter("password2")) != true) {
			if (password2.matches("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]{6,20}$") != true) {
				messages.add("確認用パスワードは空白、または半角文字で6～20文字以内で入力してください");
			}
		}
		if (name.matches("^.{1,10}$") != true) {
			messages.add("名前は1～10文字以内で入力してください");
		}
		if (branchId != 1 && departmentId == 1 || branchId != 1 && departmentId == 2
				|| branchId == 1 && departmentId == 3 || branchId == 1 && departmentId == 4) {
			messages.add("正しい支店と部署・役職の組み合わせを選んで下さい");
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
