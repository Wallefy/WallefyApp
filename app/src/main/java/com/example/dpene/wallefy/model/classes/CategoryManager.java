package com.example.dpene.wallefy.model.classes;

public class CategoryManager {

    private static Category category;
    private static CategoryManager instance;

    private CategoryManager(){

    }

    public CategoryManager getInstance(){
        if(instance == null){
            instance = new CategoryManager();
        }
        return instance;
    }

    //TODO initializing category's methods, DAO

    public String getTitle(){
        return category.getTitle();
    }

    public String getColor(){
        return category.getColor();
    }

    public int getIcon(){
        return category.getIcon();
    }

}
