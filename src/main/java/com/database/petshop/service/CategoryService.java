package com.database.petshop.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.database.petshop.entity.CategoryEntity;
import com.database.petshop.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository catRepo;

    public List<CategoryEntity> findAllCategories() {
        return catRepo.findAll();
    }

    public CategoryEntity findCategoryById(Long id) {
        return catRepo.findById(id).orElse(null);
    }

    public CategoryEntity saveCategory(CategoryEntity category) {
        return catRepo.save(category);
    }

    public CategoryEntity updateCategory(Long id, CategoryEntity details) {
        Optional<CategoryEntity> optional = catRepo.findById(id);
        if (optional.isPresent()) {
            CategoryEntity existing = optional.get();
            existing.setCategoryName(details.getCategoryName());
            return catRepo.save(existing);
        }
        return null;
    }

    public void deleteCategory(Long id) {
        catRepo.deleteById(id);
    }

    public List<Map<String, Object>> getCategorySalesReport() {
        return catRepo.getSalesByCategoryReport(); 
    }
}
