package springkeijiban.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import springkeijiban.entity.UserMessage;

public interface UserMessageMapper {

	List<UserMessage> getMessages(@Param("startDay") String startDay, @Param("endDay") String endDay, @Param("categorys") String[] categorys);

	List<UserMessage> getAllCategorys();
}
