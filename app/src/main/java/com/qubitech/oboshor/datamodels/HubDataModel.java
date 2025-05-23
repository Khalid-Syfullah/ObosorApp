package com.qubitech.oboshor.datamodels;

import android.content.Intent;

public class HubDataModel {

    String name, address;
    Intent intent;

    public HubDataModel(String name, String address, Intent intent) {
        this.name = name;
        this.address = address;
        this.intent = intent;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
