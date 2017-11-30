package springkeijiban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springkeijiban.form.SignUpForm;
import springkeijiban.mapper.UserMapper;
import springkeijiban.utils.CipherUtil;

@Service
public class SignUpService {

	@Autowired
	private UserMapper userMapper;

	public void getSignUp(SignUpForm signUpForm) {

		String encPassword = CipherUtil.encrypt(signUpForm.getPassword());
		signUpForm.setPassword(encPassword);

		userMapper.getSignUp(signUpForm);
	}
}
