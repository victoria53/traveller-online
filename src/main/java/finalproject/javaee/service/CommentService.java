package finalproject.javaee.service;

import finalproject.javaee.dto.pojoDTO.CommentDTO;
import finalproject.javaee.model.pojo.Comment;
import finalproject.javaee.model.pojo.Post;
import finalproject.javaee.model.pojo.User;
import finalproject.javaee.model.repository.CommentRepository;
import finalproject.javaee.model.repository.PostRepository;
import finalproject.javaee.model.repository.UserRepository;
import finalproject.javaee.model.util.exceptions.BaseException;
import finalproject.javaee.model.util.exceptions.postsExceptions.IllegalCommentException;
import finalproject.javaee.model.util.exceptions.postsExceptions.InvalidPostException;
import finalproject.javaee.model.util.exceptions.postsExceptions.LikedPostException;
import finalproject.javaee.model.util.exceptions.postsExceptions.NotLikedPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional(rollbackOn = BaseException.class)
public class CommentService {

    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private UserRepository userRepository;

    public CommentDTO writeComment(User user, long id, String comment){
        Post post = postRepository.findById(id);
        Comment com = new Comment(user.getId(), post.getId(), comment);
        commentRepository.save(com);
        return new CommentDTO(user.getUsername(), user.getPhoto(), comment);
    }

    public CommentDTO deleteComment(User user, long id, long commentId) throws BaseException {
        Comment com = commentRepository.findById(commentId);
        if (commentRepository.findAllByPostId(id).contains(com)) {
            if (com.getUserId() == user.getId()) {
                commentRepository.delete(com);
                return new CommentDTO(user.getUsername(), user.getPhoto(), com.getText());
            }
            else throw new IllegalCommentException("Cannot delete other's comments");
        }
        else throw new IllegalCommentException("No such a comment");
    }

    public void likeComment(User user, long postId, long commentId) throws BaseException {
        Comment comment = commentRepository.findById(commentId);
        if (!comment.getUsersWhoLiked().contains(user)) {
            if (comment.getPostId() == postId) {
                comment.getUsersWhoLiked().add(user);
                user.getLikedComments().add(comment);
                userRepository.save(user);
            }
            else throw new InvalidPostException("This post doest't have such a comment");
        }
        else throw new LikedPostException("Already liked this comment.");
    }

    public void dislikeComment(User user, long postId, long commentId) throws BaseException {
        Comment comment = commentRepository.findById(commentId);
        if (comment.getUsersWhoLiked().contains(user)) {
            if (comment.getPostId() == postId) {
                comment.getUsersWhoLiked().remove(user);
                user.getLikedPosts().remove(comment);
                userRepository.save(user);
            }
            else throw new InvalidPostException("This post doest't have such a comment");
        }
        else throw new NotLikedPostException("Not liked this comment.");
    }

}
