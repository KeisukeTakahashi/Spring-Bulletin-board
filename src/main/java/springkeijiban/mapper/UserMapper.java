package springkeijiban.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import springkeijiban.entity.User;
import springkeijiban.form.SettingsForm;
import springkeijiban.form.SignUpForm;

public interface UserMapper {
	    User getUser(@Param("loginId") String loginId, @Param("password") String password);

	    User getSignUp(SignUpForm signUpForm);

	    List<User> getAllUsers();

	    User update(SettingsForm settingsForm);

	    User getEditUser(int id);

	    List<User> getBranchDatas();

	    List<User> getDepartmentDatas();

	    User updateIsWorking(@Param("id") int id, @Param("isWorking") int isWorking);

	    User getLoginUser(int id);

	    User getLoginId(String loginId);
}
