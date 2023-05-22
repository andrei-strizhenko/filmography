package com.example.filmography.controller.mvc;

import com.example.filmography.dto.RememberPasswordDto;
import com.example.filmography.dto.SetPasswordDto;
import com.example.filmography.dto.UserDto;
import com.example.filmography.mapper.UserMapper;
import com.example.filmography.model.User;
import com.example.filmography.repository.UserRepository;
import com.example.filmography.service.UserService;
import com.example.filmography.service.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Hidden
@Controller
@RequestMapping("/users")
public class MVCUserController {
  private final UserRepository userRepository;
  private final UserService service;
  private final UserMapper mapper;

  public MVCUserController(UserService service, UserMapper mapper,
                           UserRepository userRepository) {
    this.service = service;
    this.mapper = mapper;
    this.userRepository = userRepository;
  }

  @GetMapping("/registration")
  public String registration() {
    return "registration";
  }

  @PostMapping("/registration")
  public String registration(@ModelAttribute("userForm") UserDto userDto) {
    service.create(mapper.toEntity(userDto));
    return "login";
  }

  @GetMapping("/profile/{id}")
  public String getProfile(@PathVariable Long id, Model model) {
    CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!Objects.isNull(customUserDetails.getUserId())) {
      if (!id.equals(customUserDetails.getUserId())) {
        return HttpStatus.FORBIDDEN.toString();
      }
    }
    model.addAttribute("user", mapper.toDto(service.getOne(id)));
    return "profile/viewProfile";
  }

  @GetMapping("/profile/update/{id}")
  public String update(@PathVariable Long id, Model model) {
    CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!id.equals(customUserDetails.getUserId())) {
      return HttpStatus.FORBIDDEN.toString();
    }
    model.addAttribute("user", mapper.toDto(service.getOne(id)));
    return "profile/updateProfile";
  }

  @PostMapping("/profile/update")
  public String update(@ModelAttribute("userForm") UserDto userDto) {
    User foundedUser = service.getOne(userDto.getId());

    //foundedUser.setLogin(userDto.getLogin());
    foundedUser.setFirstName(userDto.getFirstName());
    foundedUser.setLastName(userDto.getLastName());
    foundedUser.setMiddleName(userDto.getMiddleName());
    foundedUser.setEmail(userDto.getEmail());
    foundedUser.setBirthDate(userDto.getBirthDate());
    foundedUser.setPhone(userDto.getPhone());
    foundedUser.setAddress(userDto.getAddress());
    foundedUser.setUpdatedBy("user_update");
    foundedUser.setUpdatedWhen(LocalDateTime.now());
    userDto = mapper.toDto(foundedUser);
    service.update(mapper.toEntity(userDto));

    return "redirect:/users/profile/" + userDto.getId();
  }

  @GetMapping("")
  public String getAllPaginated(
          @RequestParam(value = "page", defaultValue = "1") int page,
          @RequestParam(value = "size", defaultValue = "5") int pageSize,
          Model model) {
    PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));
    Page<User> userPage = service.listAllPaginated(pageRequest);
    List<UserDto> userDtos = userPage
            .stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    model.addAttribute("users", new PageImpl<>(userDtos, pageRequest, userPage.getTotalElements()));
    return "users/viewAllUsers";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    service.delete(id);
    return "redirect:/users";
  }

  @GetMapping("/ban/{userId}")
  public String banUser(@PathVariable Long userId) {
    service.banUser(userId);
    return "redirect:/users";
  }

  @GetMapping("/unban/{userId}")
  public String unbanUser(@PathVariable Long userId) {
    service.unbanUser(userId);
    return "redirect:/users";
  }

  @GetMapping("/add-manager")
  public String createManager(@ModelAttribute("userForm") UserDto userDto) {
    return "users/addManager";
  }

  @PostMapping("/add-manager")
  public String createManager(@ModelAttribute("userForm") @Valid UserDto userDto, BindingResult result) {
    if (result.hasErrors()) {
      return "/users/addManager";
    } else {
      service.createManager(mapper.toEntity(userDto));
      return "redirect:/users";
    }
  }

  @GetMapping("/remember-password")
  public String rememberPassword() {
    return "rememberPassword";
  }

  @PostMapping("/remember-password")
  public String rememberPassword(@ModelAttribute("email")RememberPasswordDto rememberPasswordDto) {
    UserDto userDto = mapper.toDto(service.getUserByEmail(rememberPasswordDto.getEmail()));
    if(Objects.isNull(userDto)) {
      return "redirect:/error-with-message?message=User not found";
    } else {
      service.sendChangePasswordEmail(userDto.getEmail());
      return "redirect:/login";
    }
  }

  @GetMapping("/change-password")
  public String changePassword(@PathParam(value = "uuid") String uuid, Model model) {
    model.addAttribute("uuid", uuid);
    return "changePassword";
  }

  @PostMapping("/change-password")
  public String changePassword(@PathParam(value = "uuid") String uuid, @ModelAttribute("changePasswordForm") UserDto userDto) {
    service.changePassword(uuid, userDto.getPassword());
    return "redirect:/login";
  }

  @GetMapping("/set-password")
  public String setPassword(Model model) {
    model.addAttribute("user", service.getOne(getPrincipal().getUserId()));
    return "profile/setPassword";
  }

  @PostMapping("/set-password")
  public String setPassword(@ModelAttribute("setPasswordForm") SetPasswordDto setPasswordDto) {
    if (getPrincipal().getUserId().equals(setPasswordDto.getUserId())) {
      try {
        service.setPassword(setPasswordDto.getUserId(), setPasswordDto.getOldPassword(), setPasswordDto.getNewPassword());
      } catch (Exception e) {
        return "redirect:/error-with-message?message=" + e.getMessage();
      }
      return "redirect:/users/profile/" + getPrincipal().getUserId();
    } else {
      return HttpStatus.FORBIDDEN.toString();
    }
  }

  private CustomUserDetails getPrincipal() {
    return (CustomUserDetails) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
  }
}

