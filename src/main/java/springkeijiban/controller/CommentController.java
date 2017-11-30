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
import springkeijiban.form.NewCommentForm;
import springkeijiban.service.CommentService;

@Controller
public class CommentController {

	@Autowired
	private CommentService commentService;

	@RequestMapping(value = "/insertComment", method = RequestMethod.POST)
	public String getCommentForm(@ModelAttribute NewCommentForm newCommentForm, Model model,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		List<String> messages = new ArrayList<String>();
		UserDto user = (UserDto) session.getAttribute("loginUser");

		if (isValid(request, messages) == true) {
			newCommentForm.setUserId(user.getId());
			newCommentForm.setBranchId(user.getBranchId());
			newCommentForm.setDepartmentId(user.getDepartmentId());
			newCommentForm.setMessageId(Integer.parseInt(request.getParameter("messageId")));

			commentService.insertComment(newCommentForm);
			return "redirect:/";
		}
		request.setAttribute("comment", request.getParameter("comment"));
		session.setAttribute("errorMessages", messages);
		return "redirect:/";
	}

	@RequestMapping(value = "/DeleteComment", method = RequestMethod.POST)
	public String deleteComment(Model model, HttpServletRequest request) {

		commentService.deleteComment(Integer.parseInt(request.getParameter("id")));
		return "redirect:/";
	}

	private boolean isValid(HttpServletRequest request, List<String> messages) {

		String message = request.getParameter("comment");

		if (StringUtils.isNotBlank(message) != true) {
			messages.add("コメントを入力してください");
		}
		if (500 < message.length()) {
			messages.add("コメントは500文字以下で入力してください");
		}
		if (messages.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
