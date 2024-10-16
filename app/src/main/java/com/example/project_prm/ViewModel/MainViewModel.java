package com.example.project_prm.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_prm.Model.SliderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private final MutableLiveData<List<SliderModel>> _banner = new MutableLiveData<>();
    public LiveData<List<SliderModel>> getBanners() {
        return _banner;
    }

    public void loadBanners() {
        firebaseDatabase.getReference("Banner").addValueEventListener(new ValueEventListener() {
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
