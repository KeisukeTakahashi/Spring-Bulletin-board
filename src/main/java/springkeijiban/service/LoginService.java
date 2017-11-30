package springkeijiban.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springkeijiban.dto.UserDto;
import springkeijiban.entity.User;
import springkeijiban.mapper.UserMapper;
import springkeijiban.utils.CipherUtil;

@Service
public class LoginService {

	@Autowired
	private UserMapper userMapper;

	public UserDto getLogin(String loginId, String password) {

		String encPassword = CipherUtil.encrypt(password);
		UserDto dto = new UserDto();
		User entity = userMapper.getUser(loginId, encPassword);

		if (entity != null) {
			BeanUtils.copyProperties(entity, dto);
			return dto;
		}else {
			return null;
		}
	}

	public UserDto getLoginUser(int id) {

		UserDto dto = new UserDto();
		User entity = userMapper.getLoginUser(id);

		if (entity != null) {
			BeanUtils.copyProperties(entity, dto);
			return dto;
		}else {
			return null;
		}
	}
}
