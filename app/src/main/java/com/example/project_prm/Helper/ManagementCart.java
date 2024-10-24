package com.example.project_prm.Helper;

import android.content.Context;

import com.example.project_prm.Model.ListItemModel;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private ArrayList<ListItemModel> cartItems; // Giả sử đây là danh sách sản phẩm trong giỏ hàng

    // Constructor để khởi tạo ManagementCart
    public ManagementCart(Context context) {
        this.context = context;
        this.cartItems = new ArrayList<>(); // Khởi tạo danh sách giỏ hàng
    }

    // Phương thức thêm sản phẩm vào giỏ hàng
    public void plusItem(ArrayList<ListItemModel> listItem, int position, ChangeNumberItemsListener listener) {
        ListItemModel item = listItem.get(position);
        item.setNumberInCart(item.getNumberInCart() + 1);
        listener.onChanged();
    }

    // Phương thức giảm số lượng sản phẩm trong giỏ hàng
    public void minusItem(ArrayList<ListItemModel> listItem, int position, ChangeNumberItemsListener listener) {
        ListItemModel item = listItem.get(position);
        if (item.getNumberInCart() > 0) {
            item.setNumberInCart(item.getNumberInCart() - 1);
        }
        listener.onChanged();
    }

    // Phương thức để tính tổng số tiền trong giỏ hàng
    public double getTotalPrice() {
        double total = 0;
        for (ListItemModel item : cartItems) {
            total += item.getPrice() * item.getNumberInCart();
        }
        return total;
    }
}
