package com.example.project_prm.Helper;

import android.content.Context;
import android.widget.Toast;
import com.example.project_prm.Model.ItemsModel;

import java.util.ArrayList;
import java.util.List;

public class ManagmentCart {

    private final TinyDB tinyDB;
    private final Context context;
    private final ArrayList<ItemsModel> items;

    public ManagmentCart(Context context) {
        this.tinyDB = new TinyDB(context);
        this.context = context;
        ArrayList<ItemsModel> listFood = tinyDB.getListObject("CartList");
        items = listFood != null ? listFood : new ArrayList<>();
    }

    public void insertFood(ItemsModel item) {
        ArrayList<ItemsModel> listFood = getListCart();
        boolean existAlready = false;
        int index = -1;

        for (int i = 0; i < listFood.size(); i++) {
            if (listFood.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                index = i;
                break;
            }
        }

        if (existAlready) {
            listFood.get(index).setNumberInCart(item.getNumberInCart());
        } else {
            listFood.add(item);
        }

        tinyDB.putListObject("CartList", listFood);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ItemsModel> getListCart() {
        return items;
    }

    public void minusItem(ArrayList<ItemsModel> listFood, int position, ChangeNumberItemsListener listener) {
        if (listFood.get(position).getNumberInCart() == 1) {
            listFood.remove(position);
        } else {
            listFood.get(position).setNumberInCart(listFood.get(position).getNumberInCart() - 1);
        }

        tinyDB.putListObject("CartList", listFood);
        listener.onChanged();
    }

    public void updateTinyDB () {
        tinyDB.putListObject("CartList", items);
    }

    public void plusItem(ArrayList<ItemsModel> listFood, int position, ChangeNumberItemsListener listener) {
        listFood.get(position).setNumberInCart(listFood.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", listFood);
        listener.onChanged();
    }

    public double getTotalFee() {
        ArrayList<ItemsModel> listFood = getListCart();
        double fee = 0.0;
        for (ItemsModel item : listFood) {
            fee += item.getPrice() * item.getNumberInCart();
        }
        return fee;
    }
    public void clearCart() {
        items.clear();  // Clear the in-memory list
        tinyDB.putListObject("CartList", items);  // Save the empty list to storage
        Toast.makeText(context, "Cart has been cleared.", Toast.LENGTH_SHORT).show();
    }
}