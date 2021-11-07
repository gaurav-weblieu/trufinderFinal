package com.business.findtrue.app.connection;

import androidx.annotation.Nullable;

public class ConnectionModel {

    @Nullable
    private int type;
    private boolean isConnected;

    public ConnectionModel(int type, boolean isConnected) {
        this.type = type;
        this.isConnected = isConnected;
    }

    public int getType() {
        return type;
    }

    public boolean getIsConnected() {
        return isConnected;
    }
}
