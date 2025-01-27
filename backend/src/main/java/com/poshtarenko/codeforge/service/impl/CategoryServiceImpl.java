package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.CategoryMapper;
import com.poshtarenko.codeforge.dto.response.ViewCategoryDTO;
import com.poshtarenko.codeforge.entity.test.Category;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.CategoryRepository;
import com.poshtarenko.codeforge.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public ViewCategoryDTO find(long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(Category.class, id));
    }

    @Override
    public List<ViewCategoryDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
