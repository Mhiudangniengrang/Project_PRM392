package com.example.project_prm.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_prm.Model.CategoryModel;
import com.example.project_prm.Model.ItemsModel;
import com.example.project_prm.Model.SliderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private MutableLiveData<List<SliderModel>> _banner = new MutableLiveData<>();
    private MutableLiveData<List<CategoryModel>> _category = new MutableLiveData<>();
    private MutableLiveData<List<ItemsModel>> _recommended = new MutableLiveData<>();

    // Getters for LiveData
    public LiveData<List<SliderModel>> getBanners() {
        return _banner;
    }

    public LiveData<List<CategoryModel>> getCategories() {
        return _category;
    }

    public LiveData<List<ItemsModel>> getRecommended() {
        return _recommended;
    }

    // Load recommended items
    public void loadRecommended() {
        DatabaseReference ref = firebaseDatabase.getReference("Items");
        Query query = ref.orderByChild("showRecommended").equalTo(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ItemsModel> lists = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    ItemsModel item = childSnapshot.getValue(ItemsModel.class);
                    if (item != null) {
                        lists.add(item);
                    }
                }
                _recommended.setValue(lists);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    // Load categories
    public void loadCategory() {
        DatabaseReference ref = firebaseDatabase.getReference("Category");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<CategoryModel> lists = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    CategoryModel list = childSnapshot.getValue(CategoryModel.class);
                    if (list != null) {
                        lists.add(list);
                    }
                }
                _category.setValue(lists);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    // Load banners
    public void loadBanners() {
        DatabaseReference ref = firebaseDatabase.getReference("Banner");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<SliderModel> lists = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    SliderModel list = childSnapshot.getValue(SliderModel.class);
                    if (list != null) {
                        lists.add(list);
                    }
                }
                _banner.postValue(lists);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error
            }
        });
    }
}
