package com.t3leem_live.interfaces;


public interface Listeners {

    interface LoginListener {
        void validate();
        void showCountryDialog();
        void forgetPassword();
        void navigateToSignUpActivity();
    }


}
