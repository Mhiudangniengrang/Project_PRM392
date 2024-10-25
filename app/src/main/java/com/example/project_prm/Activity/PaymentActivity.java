package com.example.project_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.Helper.ManagmentCart;
import com.example.project_prm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvTotalAmount, tvMessage;
    private EditText edAddress, edPhoneNumber;
    private Button btnPay;
    private double totalAmount; // To hold the total amount from MyCart
    private ManagmentCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        // Initialize views
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        edAddress = findViewById(R.id.edAddress);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        tvMessage = findViewById(R.id.tvMessage);
        btnPay = findViewById(R.id.btnPay);

        // Get the total amount from MyCart
        managementCart = new ManagmentCart(this);
        totalAmount = managementCart.getTotalFee(); // Get the total fee from cart
        tvTotalAmount.setText(String.format("Total: $%.2f", totalAmount)); // Display the total

        // Set up the payment button to initiate MoMo payment
        btnPay.setOnClickListener(v -> initiateMoMoPayment());
    }

    private void initiateMoMoPayment() {
        String address = edAddress.getText().toString().trim();
        String phoneNumber = edPhoneNumber.getText().toString().trim();

        if (address.isEmpty() || phoneNumber.isEmpty()) {
            tvMessage.setText("Please enter both address and phone number.");
            return;
        }

        // Set MoMo action to PAYMENT
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        // Prepare payment details
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put("merchantname", "Demo SDK");
        eventValue.put("merchantcode", "SCB01");
        eventValue.put("amount", totalAmount); // Set total amount as integer
        eventValue.put("orderId", "orderId123456789");
        eventValue.put("orderLabel", "Mã đơn hàng");
        eventValue.put("merchantnamelabel", "Dịch vụ");
        eventValue.put("description", "Payment for Order #123");
        eventValue.put("requestId", "merchant_billId_" + System.currentTimeMillis());
        eventValue.put("partnerCode", "SCB01");

        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("address", address);
            objExtraData.put("phone_number", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        // Request MoMo payment
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == RESULT_OK) {
            if (data != null) {
                int status = data.getIntExtra("status", -1);
                if (status == 0) {
                    String token = data.getStringExtra("data");
                    String phone = data.getStringExtra("phonenumber");
                    tvMessage.setText("Payment Success! Token: " + token + "\nPhone: " + phone);
                    // TODO: Send token to your server to complete the transaction
                } else {
                    tvMessage.setText("Payment Failed: " + data.getStringExtra("message"));
                }
            } else {
                tvMessage.setText("No information received from MoMo");
            }
        } else {
            tvMessage.setText("Payment information could not be retrieved");
        }
    }
}
