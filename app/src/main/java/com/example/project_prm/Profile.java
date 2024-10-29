package com.example.project_prm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.Helper.ProfileManager;
import com.example.project_prm.Model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class Profile extends AppCompatActivity {
    private ImageView profileImage; // Khai báo biến ở cấp độ lớp
    private TextView firstName, lastName, birthday, phoneNumber, email, address; // Change EditText to TextView for birthday
    private Spinner genderSpinner; // Đặt biến cho Spinner
    private Button updateButton, backButton;
    private ProfileManager profileManager;
    private FirebaseAuth mAuth;
    private ArrayAdapter<CharSequence> adapter; // Khai báo adapter ở cấp độ lớp
    private boolean isEditing = false; // Cờ theo dõi trạng thái

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        profileManager = new ProfileManager(this);
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ các view từ XML
        profileImage = findViewById(R.id.imageView);

        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.textView2);
        genderSpinner = findViewById(R.id.gender_spinner);

        // Thiết lập adapter cho Spinner giới tính
        adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        birthday = findViewById(R.id.textView4);
        phoneNumber = findViewById(R.id.textView5);
        email = findViewById(R.id.textView6);
        address = findViewById(R.id.textView7);
        updateButton = findViewById(R.id.button);
        backButton = findViewById(R.id.button2);

        loadUserInfo();
        setFieldsEnabled(false);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditing) {
                    // Lần nhấn đầu tiên, cho phép chỉnh sửa
                    setFieldsEnabled(true);
                    updateButton.setText("Save");
                    isEditing = true;
                } else {
                    // Lần nhấn thứ hai, lưu thông tin
                    updateUserInfo();
                    setFieldsEnabled(false);
                    updateButton.setText("Update");
                    isEditing = false;
                }
            }
        });

        // Thiết lập sự kiện cho nút Back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kết thúc Activity và quay lại trang trước đó
            }
        });

        // Set an OnClickListener on the birthday TextView
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Profile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Format the selected date and set it to the TextView
                                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                                birthday.setText(selectedDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String emailStr = user.getEmail();
            UserProfile userProfile = profileManager.getUserInfo();

            if (userProfile == null ||
                    (userProfile.getFirstName() == null || userProfile.getFirstName().isEmpty()) &&
                            (userProfile.getLastName() == null || userProfile.getLastName().isEmpty()) &&
                            (userProfile.getGender() == null || userProfile.getGender().isEmpty()) &&
                            (userProfile.getBirthday() == null || userProfile.getBirthday().isEmpty()) &&
                            (userProfile.getPhoneNumber() == null || userProfile.getPhoneNumber().isEmpty()) &&
                            (userProfile.getEmail() == null || userProfile.getEmail().isEmpty()) &&
                            (userProfile.getAddress() == null || userProfile.getAddress().isEmpty())) {

                // Tạo thông tin ngẫu nhiên nếu tất cả trường đều null hoặc rỗng
                firstName.setText("FirstName"); // Tạo ngẫu nhiên
                lastName.setText("LastName");
                genderSpinner.setSelection(0); // Chọn giới tính đầu tiên trong danh sách
                birthday.setText("Birthday");
                phoneNumber.setText("1234567890");
                email.setText(emailStr);
                address.setText("Address");

            } else {
                firstName.setText(userProfile.getFirstName());
                lastName.setText(userProfile.getLastName());
                // Thiết lập giá trị cho Spinner từ userProfile
                String gender = userProfile.getGender();
                if (gender != null) {
                    int spinnerPosition = adapter.getPosition(gender);
                    genderSpinner.setSelection(spinnerPosition); // Đặt vị trí Spinner theo giới tính
                }
                profileImage.setImageResource(R.drawable.img_profile); // Sử dụng biến đã khởi tạo
                birthday.setText(userProfile.getBirthday());
                phoneNumber.setText(userProfile.getPhoneNumber());
                email.setText(userProfile.getEmail());
                address.setText(userProfile.getAddress());
            }
        }
    }

    private void updateUserInfo() {
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(firstName.getText().toString().trim());
        userProfile.setLastName(lastName.getText().toString().trim());
        userProfile.setGender(genderSpinner.getSelectedItem().toString()); // Lấy giới tính từ Spinner
        userProfile.setBirthday(birthday.getText().toString().trim()); // Use the TextView for birthday
        userProfile.setPhoneNumber(phoneNumber.getText().toString().trim());
        userProfile.setEmail(email.getText().toString().trim());
        userProfile.setAddress(address.getText().toString().trim());

        profileManager.updateUserInfo(userProfile);
        Toast.makeText(this, "User information updated successfully", Toast.LENGTH_SHORT).show();
    }

    private void setFieldsEnabled(boolean enabled) {
        firstName.setEnabled(enabled);
        lastName.setEnabled(enabled);
        genderSpinner.setEnabled(enabled); // Kích hoạt hoặc vô hiệu hóa Spinner
        birthday.setEnabled(enabled); // Note: Keep this enabled as user selects date through dialog
        phoneNumber.setEnabled(enabled);
        email.setEnabled(enabled);
        address.setEnabled(enabled);
    }
}
