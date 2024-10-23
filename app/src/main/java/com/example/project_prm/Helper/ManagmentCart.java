package com.example.project_prm.Helper;

import android.content.Context;
import android.widget.Toast;
import com.example.project_prm.Model.ItemsModel;

import java.util.ArrayList;

public class ManagmentCart {

    private final TinyDB tinyDB;
    private final Context context;

    public ManagmentCart(Context context) {
        this.tinyDB = new TinyDB(context);
        this.context = context;
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
        ArrayList<ItemsModel> listFood = tinyDB.getListObject("CartList");
        return listFood != null ? listFood : new ArrayList<>();
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
}