package springkeijiban.mapper;

import springkeijiban.entity.Comment;
import springkeijiban.form.NewCommentForm;

public interface CommentMapper {

	Comment insertComment(NewCommentForm newCommentForm);
	Comment deleteComment(int id);
}
