package Controller;

import Model.User;
import View.LoginView;

public class LoginViewController {

    private LoginView loginView;

    public LoginViewController( ){
        this.loginView = new LoginView(this);
        loginView.setVisible(true);
    }

    public boolean verifyLogin(String name, String password){
        User user = ServerCommunication.verifyLogin(name,password);
        if(user == null){
            return false;
        }
        return true;
    }

    public void endView(){
        loginView.setVisible(false);
    }

}
