package springkeijiban.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springkeijiban.dto.UserDto;
import springkeijiban.entity.User;
import springkeijiban.form.SettingsForm;
import springkeijiban.mapper.UserMapper;
import springkeijiban.utils.CipherUtil;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	public List<UserDto> getAllUsers() {
		List<User> entity = userMapper.getAllUsers();
		List<UserDto> dto = convertToDto(entity);
		return dto;
	}

	private List<UserDto> convertToDto(List<User> entity) {
		List<UserDto> resultList = new LinkedList<>();
		for (User entitys : entity) {
			UserDto dto = new UserDto();
			BeanUtils.copyProperties(entitys, dto);
			resultList.add(dto);
		}
		return resultList;
	}

	public void update(SettingsForm settingsForm) {
		if (StringUtils.isEmpty(settingsForm.getPassword()) != true) {
			String encPassword = CipherUtil.encrypt(settingsForm.getPassword());
			settingsForm.setPassword(encPassword);
		}
		userMapper.update(settingsForm);
	}

	public User getUser(int id) {
		User editUser = userMapper.getEditUser(id);
		return editUser;
	}

	public List<UserDto> getBranchData() {
		List<User> entity = userMapper.getBranchDatas();
		List<UserDto> dto = convertToDto(entity);
		return dto;
	}

	public List<UserDto> getDepartmentData() {
		List<User> entity = userMapper.getDepartmentDatas();
		List<UserDto> dto = convertToDto(entity);
		return dto;
	}

	public void updateIsWorking(int id, int isWorking) {
		userMapper.updateIsWorking(id, isWorking);
	}

	public User getLoginId(String loginId) {
		User user = userMapper.getLoginId(loginId);
		return user;
	}
}
