package ru.practicum.main_server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.CategoryDto;
import ru.practicum.main_server.dto.EventFullDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.repository.CategoryRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<EventShortDto> getCategories(int from, int size) {
        return null;
    }

    public EventFullDto getCategoryById(long id) {
        return null;
    }

    public CategoryDto updateCategory(CategoryDto categoryDto) {
        return null;
    }

    public void deleteCategory(Long catId) {

    }
}
