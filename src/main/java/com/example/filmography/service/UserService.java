package com.example.filmography.service;


import com.example.filmography.dto.LoginDto;
import com.example.filmography.model.Film;
import com.example.filmography.model.User;
import com.example.filmography.repository.OrderRepository;
import com.example.filmography.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class UserService extends GenericService<User> {

    private final OrderRepository orderRepository;
    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String filmographyMail;

    protected UserService(OrderRepository orderRepository, UserRepository repository,
                          BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService,
                          JavaMailSender javaMailSender) {
        super(repository);
        this.orderRepository = orderRepository;
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
        this.javaMailSender = javaMailSender;
    }

    //Список всех арендованных/купленных фильмов у пользователя
    public Set<Film> getAllFilmsByUserId(Long userId) {
        System.out.println(userId);
        return orderRepository.findAllFilmsByUserId(userId);
    }

    @Override
    public User create(User user) {
        user.setCreatedBy("REGISTRATION");
        user.setRole(roleService.getOne(1L));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public User createManager(User user) {
        user.setCreatedBy("ADMIN");
        user.setRole(roleService.getOne(2L));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public User getByLogin(String login) {
        return repository.findUserByLoginAndIsDeletedFalse(login);
    }

    public boolean checkPassword(LoginDto loginDto) {
        return bCryptPasswordEncoder.matches(loginDto.getPassword(), getByLogin(loginDto.getLogin()).getPassword());
    }

    public Page<User> listAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void banUser(Long userId) {
        User user = getOne(userId);
        user.setDeleted(true);
        user.setDeletedWhen(LocalDateTime.now());
        user.setDeletedBy("ADMIN");
        update(user);
    }

    public void unbanUser(Long userId) {
        User user = getOne(userId);
        user.setDeleted(false);
        user.setDeletedWhen(null);
        user.setDeletedBy(null);
        user.setUpdatedWhen(LocalDateTime.now());
        user.setUpdatedBy("ADMIN");
        update(user);
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void sendChangePasswordEmail(String email) {
        UUID uuid = UUID.randomUUID();
        User user = getUserByEmail(email);

        user.setChangePasswordToken(uuid.toString());
        update(user);
        SimpleMailMessage message = createMessage(email,
                "Восстановление пароля на сайте Онлайн Фильмотека",
                "Добрый день.\nВы получили это письмо, так как с вашего аккаунта была отправлена заявка на восстановление пароля.\n"
                + "Для восстановления пароля перейдите по ссылке: http://localhost:9090/users/change-password?uuid=" + uuid);
        javaMailSender.send(message);
    }

    private SimpleMailMessage createMessage(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(filmographyMail);
        return message;
    }

    private User findByToken(String token) {
        return repository.findByChangePasswordToken(token);
    }

    public void changePassword(String uuid, String password) {
        User user = findByToken(uuid);
        user.setChangePasswordToken(null);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        update(user);
    }

    public void setPassword(Long userId, String oldPassword, String newPassword) {
        User user = repository.findUserByLoginAndIsDeletedFalse(getOne(userId).getLogin());
        LoginDto loginDto = new LoginDto();
        loginDto.setLogin(getOne(userId).getLogin());
        loginDto.setPassword(oldPassword);
        if (checkPassword(loginDto)) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            update(user);
        } else {
            throw new NoSuchElementException("User with such login and password is not found");
        }
    }
}


