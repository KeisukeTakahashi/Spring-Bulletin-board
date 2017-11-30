package springkeijiban.mapper;

import springkeijiban.entity.Message;
import springkeijiban.form.NewMessageForm;

public interface MessageMapper {

	Message insertMessage(NewMessageForm newMessageForm);
	Message deleteMessage(int id);
}
