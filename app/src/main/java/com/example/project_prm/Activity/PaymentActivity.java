package com.example.project_prm.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.Helper.ManagmentCart;
import com.example.project_prm.Helper.OrderManager;
import com.example.project_prm.HomeActivity;
import com.example.project_prm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;

public class PaymentActivity extends AppCompatActivity {

    private static final String PAYPAL_CLIENT_ID = "AQWhkHlsvSeXXjc_7u_vQnlhB_oHyJL1wZJEEAcYCvcAaX2z99FilLA60PY2v7WA30UdW29ee1OX_GbF";
    private static final int PAYPAL_REQUEST_CODE = 123;
    private static final String PAYPAL_BASE_URL = "https://api-m.sandbox.paypal.com";
    private static final String PAYPAL_CLIENT_SECRET = "EKTGt8aIoTACQ9Sq2CXnX7VUUVojaggBddFLYb7qh5v0W2YUTkew9jZx2NVMFR7YvrMjhAFpqgh69wJ7";
    private TextView tvTotalAmount, tvMessage;
    private EditText edAddress, edPhoneNumber;
    private Button btnPay, btnPayWithPayPal;
    private double totalAmount;
    private ManagmentCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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
        btnPayWithPayPal.setOnClickListener(v -> initiateCheckout());
        handleIntent(getIntent());
    }

    private void initiateMoMoPayment() {
        String address = edAddress.getText().toString().trim();
        String phoneNumber = edPhoneNumber.getText().toString().trim();

        if (address.isEmpty() || phoneNumber.isEmpty()) {
            tvMessage.setText("Please enter both address and phone number.");
            return;
        }

        // Prepare payment details for MoMo
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

        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    }

    private void initiateCheckout() {
        Log.d("PayPal", "Initiating checkout for amount: " + totalAmount);
        new Thread(() -> {
            try {
                JSONObject orderResponse = createPayPalOrder(totalAmount);
                if (orderResponse != null) {
                    String approvalUrl = getApprovalUrl(orderResponse);
                    if (approvalUrl != null) {
                        runOnUiThread(() -> openBrowser(approvalUrl));
                    } else {
                        showToast("Failed to get approval URL");
                    }
                } else {
                    showToast("Failed to create order");
                }
            } catch (Exception e) {
                Log.e("PayPal", "Error in checkout process", e);
                showToast("Error: " + e.getMessage());
            }
        }).start();
    }

    private JSONObject createPayPalOrder(double amount) throws Exception {
        URL url = new URL(PAYPAL_BASE_URL + "/v2/checkout/orders");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Basic " + getBasicAuthHeader());
        conn.setDoOutput(true);

        String jsonInputString = "{"
                + "\"intent\": \"CAPTURE\","
                + "\"purchase_units\": [{"
                + "  \"amount\": {"
                + "    \"currency_code\": \"USD\","
                + "    \"value\": \"" + amount + "\""
                + "  }"
                + "}],"
                + "\"application_context\": {"
                + "  \"return_url\": \"prm://payment/success\","
                + "  \"cancel_url\": \"prm://payment/cancel\""
                + "}"
                + "}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
        }

        return getResponseAsJson(conn);
    }

    private String getBasicAuthHeader() {
        String auth = PAYPAL_CLIENT_ID + ":" + PAYPAL_CLIENT_SECRET;
        return android.util.Base64.encodeToString(auth.getBytes(StandardCharsets.UTF_8), android.util.Base64.NO_WRAP);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        Uri data = intent.getData();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            handlePayPalResult(data.toString());
        }
    }

    private void handlePayPalResult(String url) {
        if (url.contains("success")) {
            Uri uri = Uri.parse(url);
            String orderId = uri.getQueryParameter("token");
            if (orderId != null) {
                capturePayment(orderId);
            } else {
                showToast("Order ID not found in return URL");
            }
        } else {
            showToast("Payment cancelled or failed");
        }
    }

    private void capturePayment(String orderId) {
        new Thread(() -> {
            try {
                URL url = new URL(PAYPAL_BASE_URL + "/v2/checkout/orders/" + orderId + "/capture");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Basic " + getBasicAuthHeader());
                conn.setRequestProperty("Content-Type", "application/json"); // Ensure JSON content type
                conn.setDoOutput(true);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    // Capture was successful
                    Log.d("PayPal", "Capture response: " + getResponseAsString(conn));
                    runOnUiThread(() -> {
                        showToast("Payment captured successfully!");
                        // Save the current cart as an order
                        OrderManager orderManager = new OrderManager(PaymentActivity.this);
                        orderManager.saveOrder(managementCart.getListCart(), managementCart.getTotalFee());

                        // Clear the cart after saving the order
                        managementCart.clearCart();
                        // After showing the success message, navigate back to HomeActivity
                        Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear activity stack
                        startActivity(intent);
                        finish(); // Finish PaymentActivity to prevent going back to it
                    });
                } else {
                    handlePaymentError("Failed to capture payment. Response code: " + responseCode);
                }
            } catch (Exception e) {
                handlePaymentError("Error capturing payment: " + e.getMessage());
            }
        }).start();
    }



    private void handlePaymentError(String errorMessage) {
        Log.e("PayPal", "Payment error: " + errorMessage);
        showToast("Payment error: " + errorMessage);
    }

    private JSONObject getResponseAsJson(HttpURLConnection conn) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return new JSONObject(response.toString());
        }
    }

    private String getResponseAsString(HttpURLConnection conn) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    private String getApprovalUrl(JSONObject orderResponse) throws Exception {
        for (int i = 0; i < orderResponse.getJSONArray("links").length(); i++) {
            JSONObject link = orderResponse.getJSONArray("links").getJSONObject(i);
            if ("approve".equals(link.getString("rel"))) {
                return link.getString("href");
            }
        }
        return null;
    }

    private void openBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
