package com.aidan.basetools.models.network;

/**
 * Created by Aidan on 2018/3/10.
 */

public interface ServiceCallback<T> {
    void onResult(boolean success, T list, ServiceConstants.ErrorHandler errorHandler);
}