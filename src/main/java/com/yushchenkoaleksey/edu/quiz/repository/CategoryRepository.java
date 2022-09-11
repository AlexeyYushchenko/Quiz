package com.yushchenkoaleksey.edu.quiz.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yushchenkoaleksey.edu.quiz.model.Category;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class CategoryRepository {
    private final List<Category> categories;

    public CategoryRepository(String urlS) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        URL url = new URL(urlS);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try(BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream())){
            Map<String, List<Category>> map = objectMapper.readValue(bis, new TypeReference<>() {});
            this.categories = map.get("trivia_categories");
            this.categories.add(0, new Category(0, "Any Category"));
        }
    }

    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("\n");
        categories.stream().map(Category::getName).forEach(sj::add);
        return "CategoryRepository{" +
                "categories=\n" + sj +
                '}';
    }
}
