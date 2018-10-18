/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.vncthumbnailviewer;

import com.epic.vncthumbnailviewer.discovery.ClientThread;
import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Epic
 */
public class VncViewerDaemon {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

    }

    public VncViewerDaemon() {
        Thread client = new Thread(ClientThread.getInstance());
        client.start();

        try {
            if (SystemTray.isSupported()) {
                Image image;
                image = ImageIO.read(VncThumbnailViewer.class.getResource("/vnc_icon.png"));
                PopupMenu popup = new PopupMenu();
                TrayIcon trayIcon = new TrayIcon(image);
                trayIcon.setImageAutoSize(true);

                // Create a pop-up menu components
                MenuItem aboutItem = new MenuItem("About");
                MenuItem config = new MenuItem("Config");
                MenuItem exitItem = new MenuItem("Exit");

                //Add components to pop-up menu
                popup.add(config);
                popup.addSeparator();
                popup.add(aboutItem);
                popup.add(exitItem);

                trayIcon.setPopupMenu(popup);

                aboutItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //display software informations
                        System.out.println("Display about panel");
                    }
                });

                config.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //if root, open config
                        //else do nothing
                        System.out.println("Open config dialog");
                    }
                });

                exitItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //if root, exit
                        //else do nothing
                        System.out.println("Hiding tray icon");
                        SystemTray.getSystemTray().remove(trayIcon);
                    }
                });

                try {
                    SystemTray.getSystemTray().add(trayIcon);
                } catch (AWTException ex) {
                    System.err.println("Error while creating tray icon.");
                }
            } else
                System.err.println("Tray icons are not supported on this System.");
        } catch (IOException ex) {
            Logger.getLogger(VncThumbnailViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
