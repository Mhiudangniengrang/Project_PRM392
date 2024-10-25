package com.example.project_prm.Helper;

import android.app.Application;
//import com.paypal.checkout.PayPalCheckout;
//import com.paypal.checkout.config.CheckoutConfig;
//import com.paypal.checkout.config.Environment;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//
//        CheckoutConfig config = new CheckoutConfig(
//                this,
//                "YOUR_CLIENT_ID", // Replace with your PayPal client ID
//                Environment.SANDBOX, // Change to Environment.LIVE in production
//                "YOUR_PACKAGE_NAME://paypalpay" // Redirect URI scheme for PayPal
//        );
//
//        PayPalCheckout.setConfig(config);
    }
}