package finalproject.javaee.controller;

import finalproject.javaee.model.dao.UserDao;
import finalproject.javaee.model.pojo.User;
import finalproject.javaee.model.repository.UserRepository;
import finalproject.javaee.model.util.exceprions.BaseException;
import finalproject.javaee.model.util.exceprions.InvalidLoginException;
import finalproject.javaee.model.util.exceprions.UserLoggedInException;
import finalproject.javaee.model.util.exceprions.usersRegistrationExcepions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class UserController extends BaseController {


    @Autowired
    private UserRepository userRepository;
    private UserDao userDao;

    @PostMapping(value = "/register")
    public User userRegistration(@RequestBody User user, HttpServletResponse response) throws RegistrationException, IOException {
        //TODO DTO
        validateUsername(user.getUsername());
        validatePassword(user.getPassword(), user.getVerifyPassword());
        validateFirstName(user.getFirstName());
        validateLastName(user.getLastName());
        validateEmail(user.getEmail());
        validateGender(user.getGender());
        userRepository.save(user);
//        response.sendRedirect("/login");
        return user;
    }

    @PostMapping(value = "/login")
    public User login(@RequestBody User user, HttpSession session) throws BaseException {
        //TODO DTO
        if(!isLoggedIn(session)){
            validateUsernameAndPassword(user.getUsername(), user.getPassword());
            session.setAttribute("User",user);
            session.setAttribute("Username", user.getUsername());
            return user;
        }else{
            throw new UserLoggedInException();
        }
    }

    /* ************* Validations ************* */

    public static boolean isLoggedIn(HttpSession session){
        return !(session.isNew() || session.getAttribute("Username") == null);
    }

    private void validateUsername(String username)throws RegistrationException {
        if(username == null || username.isEmpty()){
            throw new InvalidUsernameException();
        }
        if(userRepository.findByUsername(username) != null){
            throw new UsernameExistException();
        }
    }

    private void validatePassword(String password, String verifyPassword) throws RegistrationException {
        if((password == null || verifyPassword ==null)||(password.isEmpty() || verifyPassword.isEmpty()) ||
                (password.length()<6 || verifyPassword.length() <6) ){
            throw new InvalidPasswordException();
        }
        if(!password.equals(verifyPassword)){
            throw new MismatchPasswordException();
        }
    }

    private void validateFirstName(String firstName) throws RegistrationException {
        if(firstName == null || firstName.isEmpty()){
            throw new InvalidFirstNameException();
        }
    }
    private void validateLastName(String lastName) throws RegistrationException {
        if(lastName == null || lastName.isEmpty()){
            throw new InvalidLastNameException();
        }
    }

    private void validateEmail(String email) throws RegistrationException {
        try {
            if(email != null) {
                InternetAddress internetAddress = new InternetAddress(email);
                internetAddress.validate();
            }
            else {
                throw new InvalidEmailException();
            }
        } catch (AddressException e) {
            throw new InvalidEmailException();
        }
        if(userRepository.findByEmail(email) != null){
            throw new EmailExistException();
        }
    }

    private void validateGender(String gender) throws RegistrationException{
        if(gender == null ||!(gender.equalsIgnoreCase(("M")) || gender.equalsIgnoreCase(("F")))){
            throw new InvalidGenderException();
        }
    }

    private void validateUsernameAndPassword(String username, String password) throws InvalidLoginException {
        if(username.isEmpty() || password.isEmpty()){
            throw new InvalidLoginException();
        }
        else{
            User user = userRepository.findByUsername(username);
            if(user == null || !user.getPassword().equals(password)){
                throw new InvalidLoginException();
            }
        }
    }
    public User getUserById(long id){
        return getUserById(id);
    }

}