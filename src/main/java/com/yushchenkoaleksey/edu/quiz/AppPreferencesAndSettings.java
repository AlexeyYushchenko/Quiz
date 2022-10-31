package com.yushchenkoaleksey.edu.quiz;

import java.util.prefs.*;

public class AppPreferencesAndSettings {
    public static final Preferences prefs = Preferences.userRoot().node(AppPreferencesAndSettings.class.getName());
    public static final String DEFAULT_SAVE_LOCATION = "C:\\Users\\User\\Documents";
    public static final String DEFAULT_LOAD_LOCATION = "C:\\Users\\User\\Documents";
    private static String currentUser = "";

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String user) {
        currentUser = user;
    }

    private AppPreferencesAndSettings(){}

    public static Preferences getPreferences(){
        return prefs;
    }

}
