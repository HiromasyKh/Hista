package com.example.hista.Model;

public class PersonProfile {
    private String name;
    private String birthday;
    private String gender;
    private String profileImageURL;

    public PersonProfile (String name, String birthday, String gender, String profileImageURL) {
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.profileImageURL = profileImageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
