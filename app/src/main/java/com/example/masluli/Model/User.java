package com.example.masluli.Model;

import java.util.HashMap;
import java.util.Map;

public class User {
    final public static String COLLECTION_NAME = "users";

    String name = "";
    String email = "";
    String age = "";
    String area = "";
    String imageUrl;

    public User(String name, String email, String age, String area) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> userJson = new HashMap<String, Object>();

        userJson.put("email",email);
        userJson.put("name",name);
        userJson.put("area",area);
        userJson.put("age",age);
        userJson.put("imageUrl",imageUrl);

        return userJson;
    }

    public static User createUser(Map<String, Object> userJson) {
        String email = (String) userJson.get("email");
        String name = (String) userJson.get("name");
        String area = (String) userJson.get("area");
        String age = (String) userJson.get("age");
        String imageUrl = (String) userJson.get("imageUrl");

        User user = new User(name, email, age, area);
        user.setImageUrl(imageUrl);

        return user;
    }
}
