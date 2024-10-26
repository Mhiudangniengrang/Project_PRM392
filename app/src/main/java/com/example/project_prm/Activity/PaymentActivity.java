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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;

import com.paypal.android.sdk.payments.PayPalService;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvTotalAmount, tvMessage;
    private EditText edAddress, edPhoneNumber;
    private Button btnPay, btnPayWithPayPal;
    private double totalAmount;
    private ManagmentCart managementCart;
    private static final String PAYPAL_CLIENT_ID = "AQWhkHlsvSeXXjc_7u_vQnlhB_oHyJL1wZJEEAcYCvcAaX2z99FilLA60PY2v7WA30UdW29ee1OX_GbF";
    private static final int PAYMENT_REQUEST_CODE = 123;
    private static final int PAYPAL_REQUEST_CODE = 12345;

    // PayPal Configuration
    public static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Start PayPal Service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);

        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        // Initialize views
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        edAddress = findViewById(R.id.edAddress);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        tvMessage = findViewById(R.id.tvMessage);
        btnPay = findViewById(R.id.btnPay);
        btnPayWithPayPal = findViewById(R.id.btnPayWithPayPal);

        // Get the total amount from MyCart
        managementCart = new ManagmentCart(this);
        totalAmount = managementCart.getTotalFee();
        tvTotalAmount.setText(String.format("Total: $%.2f", totalAmount));

        // Set up the MoMo payment button
        btnPay.setOnClickListener(v -> initiateMoMoPayment());

        // Set up the PayPal payment button
        btnPayWithPayPal.setOnClickListener(v -> initiatePayPalPayment());
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
        eventValue.put("amount", totalAmount);
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
    private void initiatePayPalPayment() {
        String address = edAddress.getText().toString().trim();
        String phoneNumber = edPhoneNumber.getText().toString().trim();

        if (address.isEmpty() || phoneNumber.isEmpty()) {
            tvMessage.setText("Please enter both address and phone number.");
            return;
        }

        // Create PayPal payment object
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(totalAmount)), "USD",
                "Order Payment", PayPalPayment.PAYMENT_INTENT_SALE);

        // Create intent for PayPal payment
        Intent payPalIntent = new Intent(PaymentActivity.this, com.paypal.android.sdk.payments.PaymentActivity.class);

        payPalIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        payPalIntent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(payPalIntent, PAYPAL_REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle MoMo payment result
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == RESULT_OK) {
            if (data != null) {
                int status = data.getIntExtra("status", -1);
                if (status == 0) {
                    String token = data.getStringExtra("data");
                    String phone = data.getStringExtra("phonenumber");
                    tvMessage.setText("Payment Success! Token: " + token + "\nPhone: " + phone);
                } else {
                    tvMessage.setText("Payment Failed: " + data.getStringExtra("message"));
                }
            } else {
                tvMessage.setText("No information received from MoMo");
            }
        }
        // Handle PayPal payment result
        else if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                tvMessage.setText("Payment Successful with PayPal!");
            } else if (resultCode == RESULT_CANCELED) {
                tvMessage.setText("PayPal Payment Canceled");
            } else {
                tvMessage.setText("PayPal Payment Failed");
            }
        } else {
            tvMessage.setText("Payment information could not be retrieved");
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class)); // Stop PayPal service on destroy
        super.onDestroy();
    }
}
