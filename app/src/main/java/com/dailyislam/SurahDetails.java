package com.dailyislam;

import android.widget.Button;

public class SurahDetails {
    public String ayat;

    public String getAyatAudioLink() {
        return ayatAudioLink;
    }

    public void setAyatAudioLink(String ayatAudioLink) {
        this.ayatAudioLink = ayatAudioLink;
    }

    public SurahDetails(String ayat, String ayatAudioLink, int number) {
        this.ayat = ayat;
        this.ayatAudioLink = ayatAudioLink;
        this.number = number;
    }

    public String ayatAudioLink;
    public int number;

    public String getAyat() {
        return ayat;
    }

    public void setAyat(String ayat) {
        this.ayat = ayat;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public SurahDetails() {
    }


}