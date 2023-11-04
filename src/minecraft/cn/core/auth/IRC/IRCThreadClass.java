package cn.core.auth.IRC;

import cn.foodtower.ui.GuiLogin;
import cn.foodtower.manager.ModuleManager;
import cn.foodtower.util.misc.Helper;

import javax.swing.*;


public class IRCThreadClass extends Thread{

    @Override
    public void run(){
        if(!GuiLogin.logined){
            while(true){
                System.out.println( "破解你老妈子?" );
                javax.swing.JOptionPane.showMessageDialog( null,"破解你老妈子?","操你妈你个傻逼", JOptionPane.ERROR_MESSAGE );
            }
        }
        if(!GuiLogin.Passed){
            System.out.println( "破解你老妈子?" );
            while(true){
                javax.swing.JOptionPane.showMessageDialog( null,"破解你老妈子?","操你妈你个傻逼", JOptionPane.ERROR_MESSAGE );
            }
        }
        IRC.IRCverify();
        while(true){
            IRC.handleInput();
            if(!ModuleManager.getModByClass(IRC.class).isEnabled()){
                Helper.sendMessageWithoutPrefix("[IRC]Disconnect From IRC Server Due To IRC Disabled");
                break;
            }
        }
    }
}
