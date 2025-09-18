package com.example.printinfo.model;

public class UserScreenAuthDto {

    private String id;
    private String screenId;
    private String canCreate;
    private String canUpdate;
    private String canDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
