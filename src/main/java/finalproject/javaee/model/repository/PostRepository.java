package finalproject.javaee.model.repository;

import finalproject.javaee.model.pojo.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {


}
