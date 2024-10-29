package com.example.project_prm.Helper;

import android.content.Context;

import com.example.project_prm.Model.UserProfile;

public class ProfileManager {
    private TinyDB tinyDB;

    // Constructor khởi tạo TinyDB
    public ProfileManager(Context context) {
        tinyDB = new TinyDB(context);
    }

    // Phương thức lấy thông tin người dùng
    public UserProfile getUserInfo() {
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(tinyDB.getString("firstName"));
        userProfile.setLastName(tinyDB.getString("lastName"));
        userProfile.setGender(tinyDB.getString("gender"));
        userProfile.setBirthday(tinyDB.getString("birthday"));
        userProfile.setPhoneNumber(tinyDB.getString("phoneNumber"));
        userProfile.setEmail(tinyDB.getString("email"));
        userProfile.setAddress(tinyDB.getString("address"));
        return userProfile;
    }

    // Phương thức cập nhật thông tin người dùng
    public void updateUserInfo(UserProfile userProfile) {
        saveUserInfo(userProfile);
    }

    // Phương thức lưu thông tin người dùng vào TinyDB
    private void saveUserInfo(UserProfile userProfile) {
        tinyDB.putString("firstName", userProfile.getFirstName());
        tinyDB.putString("lastName", userProfile.getLastName());
        tinyDB.putString("gender", userProfile.getGender());
        tinyDB.putString("birthday", userProfile.getBirthday());
        tinyDB.putString("phoneNumber", userProfile.getPhoneNumber());
        tinyDB.putString("email", userProfile.getEmail());
        tinyDB.putString("address", userProfile.getAddress());
    }
}
