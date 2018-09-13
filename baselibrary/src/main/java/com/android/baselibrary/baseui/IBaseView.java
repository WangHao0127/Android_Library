package com.android.baselibrary.baseui;

public interface IBaseView {

    void showToast(String msg);

    void showLoading(String msg);

    void showEmpty(String msg);

    void showError(String msg);
}
