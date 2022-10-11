package ru.practicum.main_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.CategoryDto;
import ru.practicum.main_server.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        log.info("get categories");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{id}")
    CategoryDto getCategoryById(@PathVariable long id) {
        log.info("get category id={}", id);
        return categoryService.getCategoryById(id);
    }
}
