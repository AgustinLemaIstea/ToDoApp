package com.istea.agustinlema.todoapp.async;


public interface Callback<T> {
    public void onFinish(T result);
}
