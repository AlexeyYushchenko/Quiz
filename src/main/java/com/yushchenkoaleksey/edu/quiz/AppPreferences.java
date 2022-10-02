package com.yushchenkoaleksey.edu.quiz;

import java.util.prefs.*;

public class AppPreferences {
    public static final Preferences prefs = Preferences.userRoot().node(AppPreferences.class.getName());
    public static final String DEFAULT_SAVE_LOCATION = "C:\\Users\\User\\Documents";
    public static final String DEFAULT_LOAD_LOCATION = "C:\\Users\\User\\Documents";

    private AppPreferences(){}

    public static Preferences getPreferences(){
        return prefs;
    }

}
