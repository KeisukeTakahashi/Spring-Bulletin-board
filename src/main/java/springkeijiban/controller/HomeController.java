package springkeijiban.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import springkeijiban.dto.UserCommentDto;
import springkeijiban.dto.UserDto;
import springkeijiban.dto.UserMessageDto;
import springkeijiban.form.NewCommentForm;
import springkeijiban.service.CommentService;
import springkeijiban.service.LoginService;
import springkeijiban.service.MessageService;
import springkeijiban.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private LoginService loginService;


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model, HttpServletRequest request) {

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

		List<UserMessageDto> categorys = messageService.getAllCategorys();
		request.setAttribute("categorys", categorys);

		List<UserDto> branches = userService.getBranchData();
		model.addAttribute("branches", branches);
		List<UserDto> departments = userService.getDepartmentData();
		model.addAttribute("departments", departments);

		String startDay = request.getParameter("startDay");
		String endDay = request.getParameter("endDay");
		String category = request.getParameter("category");
		int count = 0;

		if(StringUtils.isNotBlank(startDay) != true){
			startDay = "2017/09/25";
			count = count + 1;
		}
		if(StringUtils.isNotBlank(endDay) != true){
			Date d = new Date();
			SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd");
			endDay = day.format(d);
			count = count + 1;
		}

		if(checkDate(startDay) == true && checkDate(endDay) == true){
			List<UserMessageDto> messages = messageService.getMessages(startDay, endDay, category);
			List<UserCommentDto> comments = commentService.getComments();

			if(count != 2){
				model.addAttribute("calendarStart",startDay);
				model.addAttribute("calendarEnd",endDay);
			}

			NewCommentForm newCommentForm = new NewCommentForm();
			model.addAttribute("newCommentForm", newCommentForm);
			model.addAttribute("messages", messages);
			model.addAttribute("comments", comments);
			model.addAttribute("category", category);

			return "home";

		} else {
			//不正な日付なら正しい日付(初期値)を入れて表示
			startDay = "2017/09/25";

			Date d = new Date();
			SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd");
			endDay = day.format(d);

			List<UserMessageDto> messages = messageService.getMessages(startDay, endDay, category);
			List<UserCommentDto> comments = commentService.getComments();

			List<String> errormessages = new ArrayList<String>();
			errormessages.add("カレンダーのフォーマットが不正です");
			errormessages.add("カテゴリーが入力されていた場合は、現在までの日付でカテゴリーを検索して結果を表示します");
			session.setAttribute("errorMessages", errormessages);

			NewCommentForm newCommentForm = new NewCommentForm();
			model.addAttribute("newCommentForm", newCommentForm);
			model.addAttribute("messages", messages);
			model.addAttribute("comments", comments);
			model.addAttribute("category", category);
			model.addAttribute("calendarStart",startDay);
			model.addAttribute("calendarEnd",endDay);

			return "home";
		}
	}

	public static boolean checkDate(String strDate) {
        if (strDate.length() != 10) {
            return false;
        }
        strDate = strDate.replace('-', '/');
        DateFormat format = DateFormat.getDateInstance();
        format.setLenient(false);
        try {
            format.parse(strDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
