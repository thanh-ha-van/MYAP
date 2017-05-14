package com.example.havan.mytrafficmap.bus;

import com.example.havan.mytrafficmap.model.busDirection.Busdirection;

import rx.Observable;


public interface RxJavaInterface {

    interface View {


        void showInfor(Busdirection info);
        void showError(String error);
    }

    interface Presenter {

        void getDirection(Observable<Busdirection> data);
    }


}
