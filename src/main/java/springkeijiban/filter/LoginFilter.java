package springkeijiban.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import springkeijiban.dto.UserDto;

@WebFilter("/*")
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpSession session = ((HttpServletRequest) request).getSession();

		String path = ((HttpServletRequest) request).getServletPath();
		UserDto user = (UserDto) ((HttpServletRequest) request).getSession().getAttribute("loginUser");

		List<String> errormessages = new ArrayList<String>();

		if (!path.equals("/login") && !path.matches(".*css.*")) {
			if (user == null || user.getIsWorking() == 0) {
				errormessages.add("ログインを行ってください");
				session.setAttribute("errorMessages", errormessages);
				((HttpServletResponse) response).sendRedirect("login");
				return;
			}
		}

		if (path.equals("/control") || path.equals("/signup") || path.equals("/settings")) {
			if (user.getDepartmentId() != 1) {
				errormessages.add("該当ページに対する権限が無いため、ホーム画面に遷移しました");
				session.setAttribute("errorMessages", errormessages);
				((HttpServletResponse) response).sendRedirect("./");
				return;
			}
		}

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}
}
