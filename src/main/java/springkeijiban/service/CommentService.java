package springkeijiban.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springkeijiban.dto.UserCommentDto;
import springkeijiban.entity.UserComment;
import springkeijiban.form.NewCommentForm;
import springkeijiban.mapper.CommentMapper;
import springkeijiban.mapper.UserCommentMapper;

@Service
public class CommentService {

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private UserCommentMapper userCommentMapper;


	public void insertComment(NewCommentForm newCommentForm){

		commentMapper.insertComment(newCommentForm);
	}

	public List<UserCommentDto> getComments() {
		List<UserComment> entity = userCommentMapper.getComments();
		List<UserCommentDto> dto = convertToDto(entity);
		return dto;
	}

	private List<UserCommentDto> convertToDto(List<UserComment> entity) {
		List<UserCommentDto> resultList = new LinkedList<>();
		for (UserComment entitys : entity) {
			UserCommentDto dto = new UserCommentDto();
			BeanUtils.copyProperties(entitys, dto);
			resultList.add(dto);
		}
		return resultList;
	}

	public void deleteComment(int id) {
		commentMapper.deleteComment(id);
	}
}
