package com.berksozcu.controller.base;

import java.util.List;

import static com.berksozcu.controller.base.RootEntity.listRootEntity;

public class RestBaseController {

    public <T>RootEntity<T> ok(T data) {
        return RootEntity.ok(data);
    }

    public <T>RootEntity<T> error(String message) {
        return RootEntity.error(message);
    }

    public <T> RootEntity<List<T>> list(List<T> data) {
        return listRootEntity(data);
    }
}
