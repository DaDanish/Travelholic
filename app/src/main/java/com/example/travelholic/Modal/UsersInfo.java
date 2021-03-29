package com.example.travelholic.Modal;

public class UsersInfo {
    String introduction;
    String gender;
    String city;
    String profession;

    public UsersInfo() {
    }

    public UsersInfo(String introduction, String gender, String city, String profession) {
        this.introduction = introduction;
        this.gender = gender;
        this.city = city;
        this.profession = profession;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
