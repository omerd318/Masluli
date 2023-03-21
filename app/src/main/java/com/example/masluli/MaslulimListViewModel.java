package com.example.masluli;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.masluli.Model.Maslul;
import com.example.masluli.Model.Model;

import java.util.List;

public class MaslulimListViewModel extends ViewModel {

    public enum ListMode {
        AllMaslulim,
        MyMaslulim
    }

    LiveData<List<Maslul>> data;
    ListMode mode;

    public MaslulimListViewModel(ListMode listMode) {
        mode = listMode;
        if (listMode == ListMode.AllMaslulim) {
            data = Model.instance().getAllMaslulim();
        } else {
            data = Model.instance().getMyMaslulim();  // TODO: add getMyMaslulim to model
        }
    }

    public LiveData<List<Maslul>> getData() {
        return data;
    }

    public ListMode getMode() {
        return this.mode;
    }
}
