package springkeijiban.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springkeijiban.dto.UserMessageDto;
import springkeijiban.entity.UserMessage;
import springkeijiban.form.NewMessageForm;
import springkeijiban.mapper.MessageMapper;
import springkeijiban.mapper.UserMessageMapper;

@Service
public class MessageService {

	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private UserMessageMapper userMessageMapper;


	public void insertMessage(NewMessageForm newMessageForm){

		messageMapper.insertMessage(newMessageForm);
	}

	public List<UserMessageDto> getMessages(String startDay, String endDay, String category) {

		endDay = endDay + " 23:59:59";
		if(StringUtils.isBlank(category) == true){
			category = "%";
		}

		String[] categorys = category.replaceAll("　", " ").split(" ",0);
		List<UserMessage> entity = userMessageMapper.getMessages(startDay, endDay, categorys);
		List<UserMessageDto> dto = convertToDto(entity);
		return dto;
	}

	private List<UserMessageDto> convertToDto(List<UserMessage> entity) {
		List<UserMessageDto> resultList = new LinkedList<>();
		for (UserMessage entitys : entity) {
			UserMessageDto dto = new UserMessageDto();
			BeanUtils.copyProperties(entitys, dto);
			resultList.add(dto);
		}
		return resultList;
	}

	public void deleteMessage(int id) {
		messageMapper.deleteMessage(id);
	}

	public List<UserMessageDto> getAllCategorys() {
		List<UserMessage> entity = userMessageMapper.getAllCategorys();
		List<UserMessageDto> dto = convertToDto(entity);
		return dto;
	}
}
