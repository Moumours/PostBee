package com.example.mobile_app.model;
public class UserStatic {
    public static String access;
    private static String refresh;
    private static String first_name;
    private static String last_name;
    private static String email;
    private static int ensisaGroup;
    private static int profile_picture;
    private static String is_staff;
    private static String message;

    public static String getAccess() {
        return access;
    }

    public static void setAccess(String access) {
        UserStatic.access = access;
    }

    public static String getRefresh() {
        return refresh;
    }

    public static void setRefresh(String refresh) {
        UserStatic.refresh = refresh;
    }

    public static String getFirst_name() {
        return first_name;
    }

    public static void setFirst_name(String first_name) {
        UserStatic.first_name = first_name;
    }

    public static String getLast_name() {
        return last_name;
    }

    public static void setLast_name(String last_name) {
        UserStatic.last_name = last_name;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserStatic.email = email;
    }

    public static int getEnsisaGroup() {
        return ensisaGroup;
    }

    public static void setEnsisaGroup(int ensisaGroup) {
        UserStatic.ensisaGroup = ensisaGroup;
    }

    public static int getProfile_picture() {
        return profile_picture;
    }

    public static void setProfile_picture(int profile_picture) {
        UserStatic.profile_picture = profile_picture;
    }

    public static String getIs_staff() {
        return is_staff;
    }

    public static void setIs_staff(String is_staff) {
        UserStatic.is_staff = is_staff;
    }

    public static String getMessage() {
        return message;
    }

    public static void setMessage(String message) {
        UserStatic.message = message;
    }
}

