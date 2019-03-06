package finalproject.javaee.model.repository;

import finalproject.javaee.model.pojo.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {

    List<Media> findAllByPostId(long id);
    boolean existsByMediaUrl(String mediaUrl);

}
