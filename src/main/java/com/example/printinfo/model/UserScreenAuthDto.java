package com.example.printinfo.model;

public class UserScreenAuthDto {

    private String userId;
    private String screenId;
    private String canCreate;
    private String canUpdate;
    private String canDelete;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getCanCreate() {
        return canCreate;
    }

    public void setCanCreate(String canCreate) {
        this.canCreate = canCreate;
    }

    public String getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(String canUpdate) {
        this.canUpdate = canUpdate;
    }

    public String getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(String canDelete) {
        this.canDelete = canDelete;
    }
}
