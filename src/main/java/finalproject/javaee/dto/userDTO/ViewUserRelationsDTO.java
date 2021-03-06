package finalproject.javaee.dto.userDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ViewUserRelationsDTO {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String photo;
}
