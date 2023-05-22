package com.example.filmography.controller.mvc;

import com.example.filmography.dto.AddFilmDto;
import com.example.filmography.dto.DirectorDto;
import com.example.filmography.mapper.DirectorMapper;
import com.example.filmography.mapper.DirectorWithFilmsMapper;
import com.example.filmography.mapper.FilmMapper;
import com.example.filmography.model.Director;
import com.example.filmography.service.DirectorService;
import com.example.filmography.service.FilmService;
import com.example.filmography.service.userDetails.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/directors")
public class MVCDirectorController {

    private final DirectorService service;
    private final FilmService filmService;
    private final DirectorMapper mapper;
    private final FilmMapper filmMapper;
    private final DirectorWithFilmsMapper directorWithFilmsMapper;

    public MVCDirectorController(DirectorService service, FilmService filmService, DirectorMapper mapper, FilmMapper filmMapper, DirectorWithFilmsMapper directorWithFilmsMapper) {
        this.service = service;
        this.filmService = filmService;
        this.mapper = mapper;
        this.filmMapper = filmMapper;
        this.directorWithFilmsMapper = directorWithFilmsMapper;
    }

    @GetMapping("")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorFIO"));
        Page<Director> directorPage = null;
        if (getPrincipal().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            directorPage = service.listAllPaginatedForUsers(pageRequest);
        } else {
            directorPage = service.listAllPaginated(pageRequest);
        }
        List<DirectorDto> directorDtos = directorPage
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("directors", new PageImpl<>(directorDtos, pageRequest, directorPage.getTotalElements()));
        return "directors/viewAllDirectors";
    }

    @GetMapping("/update/{id}")
    public String update(Model model, @PathVariable Long id) {
        model.addAttribute("director", mapper.toDto(service.getOne(id)));
        return "directors/updateDirector";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("directorForm") DirectorDto directorDto) {
        Director director = service.getOne(directorDto.getId());
        director.setDirectorFIO(directorDto.getDirectorFIO());
        director.setPosition(directorDto.getPosition());
        //        service.update(mapper.toEntity(directorDto));
        service.update(director);
        return "redirect:/directors";
    }

    @GetMapping("/add")
    public String create(@ModelAttribute("directorForm") DirectorDto directorDto) {
        return "directors/addDirector";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("directorForm") @Valid DirectorDto directorDto, BindingResult result) {
        if (result.hasErrors()) {
            return "/directors/addDirector";
        } else {
            service.create(mapper.toEntity(directorDto));
            return "redirect:/directors";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/directors";
    }

    //TODO поиск разблокированных и заблокированных записей
    @PostMapping("/search")
    public String searchByDirectorFIO(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            Model model,
            @ModelAttribute("searchDirectors") DirectorDto directorDto
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorFIO"));
        Page<Director> directorPage;

        if (directorDto.getDirectorFIO().trim().equals("")) {
            if (getPrincipal().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                directorPage = service.listAllPaginatedForUsers(pageRequest);
            } else {
                directorPage = service.listAllPaginated(pageRequest);
            }
        } else {
            if (getPrincipal().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                directorPage = service.searchByDirectorFIOContainingForUsers(pageRequest, directorDto.getDirectorFIO());
            } else {
                directorPage = service.searchByDirectorFIOContaining(pageRequest, directorDto.getDirectorFIO());
            }
        }

        List<DirectorDto> directorDtos = directorPage
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute(
                "directors",
                new PageImpl<>(directorDtos, pageRequest, directorPage.getTotalElements()));

        return "directors/viewAllDirectors";
    }

    @GetMapping("/add-film/{directorId}")
    public String addFilm(Model model, @PathVariable Long directorId) {
        model.addAttribute("films", filmMapper.toDtos(filmService.listAll()));
        model.addAttribute("directorId", directorId);
        model.addAttribute("director", mapper.toDto(service.getOne(directorId)).getDirectorFIO());
        return "directors/addDirectorFilm";
    }

    @PostMapping("/add-film")
    public String addFilm(@ModelAttribute("directorFilmForm") AddFilmDto addFilmDto) {
        service.addFilm(addFilmDto);
        return "redirect:/directors";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        model.addAttribute("director", directorWithFilmsMapper.toDto(service.getOne(id)));
        return "directors/viewDirector";
    }

    @GetMapping("/block/{id}")
    public String blockDirector(@PathVariable Long id) {
        service.block(id);
        return "redirect:/directors";
    }

    @GetMapping("/unblock/{id}")
    public String unblockDirector(@PathVariable Long id) {
        service.unblock(id);
        return "redirect:/directors";
    }

    private CustomUserDetails getPrincipal() {
        return (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}