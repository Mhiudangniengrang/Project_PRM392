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
import com.example.project_prm.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;

import com.paypal.android.sdk.payments.PayPalService;

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

    private TextView tvTotalAmount, tvMessage;
    private EditText edAddress, edPhoneNumber;
    private Button btnPay, btnPayWithPayPal;
    private double totalAmount;
    private ManagmentCart managementCart;
 //   private static final String PAYPAL_CLIENT_ID = "AekmRj4s_nGVcEsrZxVJWTE7DGUQY7inpPAHZMxQTmOIcK6MszC9h7X2RU_lGYdmYk44nOOUr828hrev";
    private static final int PAYMENT_REQUEST_CODE = 123;
    private static final int PAYPAL_REQUEST_CODE = 123;
   // private String secrect = "EKTGt8aIoTACQ9Sq2CXnX7VUUVojaggBddFLYb7qh5v0W2YUTkew9jZx2NVMFR7YvrMjhAFpqgh69wJ7";
    private static final String PAYPAL_CLIENT_ID = "AQWhkHlsvSeXXjc_7u_vQnlhB_oHyJL1wZJEEAcYCvcAaX2z99FilLA60PY2v7WA30UdW29ee1OX_GbF";
    private static final String PAYPAL_CLIENT_SECRET = "EKTGt8aIoTACQ9Sq2CXnX7VUUVojaggBddFLYb7qh5v0W2YUTkew9jZx2NVMFR7YvrMjhAFpqgh69wJ7";

    // PayPal Configuration
    public static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIENT_ID);

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

    private void initiateCheckout() {
        double total = totalAmount;
        Log.d("PayPal", "Initiating checkout for amount: " + total);
        new Thread(() -> {
            try {
                JSONObject orderResponse = createPayPalOrder(total);
                Log.d("PayPal", "Order response: " + orderResponse);
                if (orderResponse != null) {
                    String approvalUrl = getApprovalUrl(orderResponse);
                    Log.d("PayPal", "Approval URL: " + approvalUrl);
                    if (approvalUrl != null) {
                        runOnUiThread(() -> openBrowser(approvalUrl));
                    } else {
                        runOnUiThread(() -> Toast.makeText(PaymentActivity.this, "Failed to get approval URL", Toast.LENGTH_LONG).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(PaymentActivity.this, "Failed to create order", Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                Log.e("PayPal", "Error in checkout process", e);
                runOnUiThread(() -> Toast.makeText(PaymentActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
    private JSONObject createPayPalOrder(double amount) throws Exception {
        URL url = new URL("https://api-m.sandbox.paypal.com/v2/checkout/orders");
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
                + "  \"return_url\": \"myapp://payment/success\","
                + "  \"cancel_url\": \"myapp://payment/cancel\""
                + "}"
                + "}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return new JSONObject(response.toString());
        }
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
                Toast.makeText(this, "Order ID not found in return URL", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Payment cancelled or failed", Toast.LENGTH_LONG).show();
        }
    }

    private void capturePayment(String orderId) {
        new Thread(() -> {
            try {
                URL url = new URL("https://api-m.sandbox.paypal.com/v2/checkout/orders/" + orderId + "/capture");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Basic " + getBasicAuthHeader());
                conn.setDoOutput(true);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    Log.d("PayPal", "Capture response: " + response.toString());

                    // Add new record to History after successful payment capture
                } else {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    Log.e("PayPal", "Capture error response: " + response.toString());

                    handlePaymentError("Failed to capture payment. Response code: " + responseCode + ", Response: " + response.toString());
                }
            } catch (Exception e) {
                handlePaymentError("Error capturing payment: " + e.getMessage());
            }
        }).start();
    }    private void handlePaymentError(String errorMessage) {
        Log.e("PayPal", "Payment error: " + errorMessage);
        runOnUiThread(() -> {
            String userMessage = "Payment error: " + errorMessage;
            // Truncate the message if it's too long
            if (userMessage.length() > 100) {
                userMessage = userMessage.substring(0, 97) + "...";
            }
            Toast.makeText(this, userMessage, Toast.LENGTH_LONG).show();
        });
    }
    private String getApprovalUrl(JSONObject orderResponse) throws Exception {
        JSONObject links = orderResponse.getJSONArray("links").getJSONObject(1);
        if ("approve".equals(links.getString("rel"))) {
            return links.getString("href");
        }
        return null;
    }
    private void openBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class)); // Stop PayPal service on destroy
        super.onDestroy();
    }
}
