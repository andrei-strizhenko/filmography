package com.example.filmography.controller.mvc;

import com.example.filmography.dto.AddFilmDto;
import org.apache.commons.io.IOUtils;
import com.example.filmography.dto.FilmDto;
import com.example.filmography.dto.FilmWithDirectorsDto;
import com.example.filmography.mapper.DirectorMapper;
import com.example.filmography.mapper.FilmMapper;
import com.example.filmography.mapper.FilmWithDirectorsMapper;
import com.example.filmography.model.Film;
import com.example.filmography.service.DirectorService;
import com.example.filmography.service.FilmService;
import com.example.filmography.service.OrderService;
import com.example.filmography.service.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Hidden
@Controller
@RequestMapping("/films")
public class MVCFilmController {

    private final FilmMapper mapper;
    private final FilmService service;
    private final FilmWithDirectorsMapper filmWithDirectorsMapper;
    private final OrderService orderService;
    private final DirectorMapper directorMapper;
    private final DirectorService directorService;

    public MVCFilmController(FilmMapper mapper, FilmService service, FilmWithDirectorsMapper filmWithDirectorsMapper,
                             OrderService orderService, DirectorMapper directorMapper, DirectorService directorService) {
        this.mapper = mapper;
        this.service = service;
        this.filmWithDirectorsMapper = filmWithDirectorsMapper;
        this.orderService = orderService;
        this.directorMapper = directorMapper;
        this.directorService = directorService;
    }

    @GetMapping("")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        Page<Film> filmPage;
        if (getPrincipal().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            filmPage = service.listAllPaginatedForUsers(pageRequest);
        } else {
            filmPage = service.listAllPaginated(pageRequest);
        }
        List<FilmWithDirectorsDto> filmDtos = filmPage
                .stream()
                .map(filmWithDirectorsMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("films", new PageImpl<>(filmDtos, pageRequest, filmPage.getTotalElements()));
        return "films/viewAllFilms";
    }

    @GetMapping("/add")
    public String create() {
        return "films/addFilm";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("filmForm") FilmDto filmDto) {
        service.create(mapper.toEntity(filmDto));
        return "redirect:/films";
    }

    @GetMapping("/update/{id}")
    public String update(Model model, @PathVariable Long id) {
        model.addAttribute("film", mapper.toDto(service.getOne(id)));
        return "films/updateFilm";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("filmForm") FilmDto filmDto) {
        Film film = service.getOne(filmDto.getId());
        film.setTitle(filmDto.getTitle());
        film.setPremierYear(filmDto.getPremierYear());
        film.setCountry(filmDto.getCountry());
        film.setGenre(filmDto.getGenre());
        service.update(film);
//        service.update(mapper.toEntity(filmDto));
        return "redirect:/films";
    }

    @PostMapping("/search")
    public String searchByTitle(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            Model model,
            @ModelAttribute("searchFilms") FilmDto filmDto
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        Page<Film> filmPage;
        if (filmDto.getTitle().trim().equals("")) {
            if (getPrincipal().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                filmPage = service.listAllPaginatedForUsers(pageRequest);
            } else {
                filmPage = service.listAllPaginated(pageRequest);
            }
        } else {
            if (getPrincipal().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                filmPage = service.searchByTitleContainingForUsers(pageRequest, filmDto.getTitle());
            } else {
                filmPage = service.searchByTitleContaining(pageRequest, filmDto.getTitle());
            }
        }
        List<FilmWithDirectorsDto> filmWithDirectorsDtos = filmPage
                .stream()
                .map(filmWithDirectorsMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("films", new PageImpl<>(filmWithDirectorsDtos, pageRequest, filmPage.getTotalElements()));
        return "films/viewAllFilms";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        model.addAttribute("film", filmWithDirectorsMapper.toDto(service.getOne(id)));
        return "films/viewFilm";
    }

    @GetMapping("/block/{id}")
    public String blockFilm(@PathVariable Long id) {
        service.block(id);
        return "redirect:/films";
    }

    @GetMapping("/unblock/{id}")
    public String unblockFilm(@PathVariable Long id) {
        service.unblock(id);
        return "redirect:/films";
    }

    private CustomUserDetails getPrincipal() {
        return (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @GetMapping(
            value = "/{id}/download",
            produces = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public @ResponseBody byte[] downloadFilm(@PathVariable Long id) throws IOException {
        if (orderService.isPurchasedOrder(getPrincipal().getUserId(), id)) {
            InputStream in = new FileInputStream("src/main/resources/cinema.txt");
            return IOUtils.toByteArray(in);
        } else {
            throw new AccessDeniedException("This film is not purchased.");
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteFilm(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/films";
    }

    @GetMapping("/add-director/{filmId}")
    public String addDirector(Model model, @PathVariable Long filmId) {
        model.addAttribute("directors", directorMapper.toDtos(directorService.listAll()));
        model.addAttribute("filmId", filmId);
        model.addAttribute("film", mapper.toDto(service.getOne(filmId)).getTitle());
        return "films/addFilmDirector";
    }

    @PostMapping("/add-director")
    public String addDirector(@ModelAttribute("directorFilmForm") AddFilmDto addFilmDto) {
        directorService.addFilm(addFilmDto);
        return "redirect:/films";
    }
}