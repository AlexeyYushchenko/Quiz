package com.yushchenkoaleksey.edu.quiz.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class CategoryInfo {

    @JsonAlias({"category_id", "categoryId"})
    private Integer categoryId;
    @JsonAlias({"category_question_count", "categoryQuestionCount"})
    private CategoryQuestionCount categoryQuestionCount;

    public CategoryInfo() {
    }

    /**
     * @param categoryId
     * @param categoryQuestionCount
     */

    public CategoryInfo(Integer categoryId, CategoryQuestionCount categoryQuestionCount) {
        super();
        this.categoryId = categoryId;
        this.categoryQuestionCount = categoryQuestionCount;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public CategoryQuestionCount getCategoryQuestionCount() {
        return categoryQuestionCount;
    }

    public void setCategoryQuestionCount(CategoryQuestionCount categoryQuestionCount) {
        this.categoryQuestionCount = categoryQuestionCount;
    }

    @Override
    public String toString() {
        return categoryQuestionCount.toString();
    }
}