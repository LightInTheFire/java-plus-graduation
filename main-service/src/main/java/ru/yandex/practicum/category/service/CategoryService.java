package ru.yandex.practicum.category.service;

import java.util.List;

import ru.yandex.practicum.category.dto.CategoryDto;
import ru.yandex.practicum.category.dto.NewCategoryDto;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    List<CategoryDto> getAllCategoriesPaged(int from, int size);

    CategoryDto getCategoryById(Long id);

    void deleteCategoryById(Long id);

    CategoryDto updateCategory(Long catId, NewCategoryDto updateCategoryDto);
}
