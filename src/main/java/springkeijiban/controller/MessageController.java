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

import springkeijiban.dto.UserDto;
import springkeijiban.dto.UserMessageDto;
import springkeijiban.form.NewMessageForm;
import springkeijiban.service.MessageService;

@Controller
public class MessageController {

	@Autowired
	private MessageService messageService;

	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public String post(Model model, HttpServletRequest request) {

		List<UserMessageDto> categorys = messageService.getAllCategorys();
		NewMessageForm newMessageForm = new NewMessageForm();
		request.setAttribute("categorys", categorys);
		model.addAttribute("newMessageForm", newMessageForm);
		return "message";
	}

	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public String getFormInfo(@ModelAttribute NewMessageForm newMessageForm, Model model, HttpServletRequest request) {

		HttpSession session = request.getSession();
		List<String> messages = new ArrayList<String>();

		List<UserMessageDto> categorys = messageService.getAllCategorys();
		request.setAttribute("categorys", categorys);

		UserDto user = (UserDto) session.getAttribute("loginUser");

		if (isValid(request, messages) == true) {
			newMessageForm.setUserId(user.getId());
			newMessageForm.setBranchId(user.getBranchId());
			newMessageForm.setDepartmentId(user.getDepartmentId());

			messageService.insertMessage(newMessageForm);
			return "redirect:/";
		}
		request.setAttribute("subject", request.getParameter("subject"));
		request.setAttribute("category", request.getParameter("category"));
		request.setAttribute("message", request.getParameter("message"));
		request.setAttribute("errorMessages", messages);
		return "message";
	}

	@RequestMapping(value = "/DeleteMessage", method = RequestMethod.POST)
	public String deleteComment(Model model, HttpServletRequest request) {

		messageService.deleteMessage(Integer.parseInt(request.getParameter("id")));
		return "redirect:/";
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		String subject = request.getParameter("subject");
		String category = request.getParameter("category");
		String message = request.getParameter("message");

		if (category.indexOf(" ") != -1 || category.indexOf("　") != -1) {
			messages.add("空白を含む文字列はカテゴリーとして登録できません");
		}
		if (StringUtils.isNotBlank(subject) != true) {
			messages.add("件名を入力してください");
		}
		if (StringUtils.isNotBlank(category) != true) {
			messages.add("カテゴリーを入力してください");
		}
		if (StringUtils.isNotBlank(message) != true) {
			messages.add("本文を入力してください");
		}
		if (30 < subject.length()) {
			messages.add("件名は30文字以下で入力してください");
		}
		if (10 < category.length()) {
			messages.add("カテゴリーは10文字以下で入力してください");
		}
		if (1000 < message.length()) {
			messages.add("本文は1000文字以下で入力してください");
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}