package com.dailyislam;

public class Surah {
    public String engName,nameArabic;
    public int number;

    public Surah() {
    }

    public String getNameArabic() {
        return nameArabic;
    }

    public void setNameArabic(String nameArabic) {
        this.nameArabic = nameArabic;
    }

    public Surah(String engName, int number, String nameArabic) {
        this.engName = engName;
        this.number = number;
        this.nameArabic=nameArabic;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
