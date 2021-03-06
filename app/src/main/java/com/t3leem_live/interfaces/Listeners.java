package com.t3leem_live.interfaces;


public interface Listeners {

    interface LoginListener {
        void validate();
        void showCountryDialog();
        void forgetPassword();
        void navigateToSignUpChooserActivity();
    }

    interface StudentSignUpListener{
        void validate();
        void showCountryDialog(int type);
        void navigateToStudentHomeActivity();
        void showImageChooserDialog();

    }

    interface ParentSignUpListener{
        void validate();
        void showCountryDialog(int type);
        void navigateToParentHomeActivity();
        void showImageChooserDialog();

    }

    interface TeacherSignUpListener{
        void validate();
        void showCountryDialog();
        void navigateToTeacherHomeActivity();
        void showImageChooserDialog();
        void showVideoChooserDialog();

    }
    interface CenterSignUpListener{
        void validate();
        void showCountryDialog();
        void navigateToCenterHomeActivity();
        void showImageChooserDialog();
        void showVideoChooserDialog();

    }

}
