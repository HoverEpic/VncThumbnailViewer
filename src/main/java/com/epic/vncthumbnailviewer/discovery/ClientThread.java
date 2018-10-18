/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.vncthumbnailviewer.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Epic
 */
public class ClientThread implements Runnable {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Thread client = new Thread(ClientThread.getInstance());
        client.start();
    }

    public static ClientThread getInstance() {
        return ClientDiscoveryThreadHolder.INSTANCE;
    }

    private static class ClientDiscoveryThreadHolder {

        private static final ClientThread INSTANCE = new ClientThread();
    }

    private DatagramSocket socket;
    private boolean serverFound = false;
    private boolean neverStop = true;
    private int broadcastDelay = 3000;

    @Override
    public void run() {
        // Find the server using UDP broadcast
        while (!serverFound || neverStop) {
            try {
                System.out.println("Trying to find a server !");
                //Open a random port to send the package
                socket = new DatagramSocket();
                socket.setBroadcast(true);
                socket.setSoTimeout(2000);

                // Broadcast the message over all the network interfaces
                Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                    if (networkInterface.isLoopback() || !networkInterface.isUp())
                        continue; // Don't want to broadcast to the loopback interface

                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                        InetAddress broadcast = interfaceAddress.getBroadcast();
                        if (broadcast == null)
                            continue;

                        //retrive mac of nic (later usage)
                        byte[] mac = networkInterface.getHardwareAddress();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < mac.length; i++)
                            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));

                        byte[] sendData = (Constants.DISCOVER_REQUEST + "__" + InetAddress.getLocalHost().getHostName() + "__" + sb.toString()).getBytes();

                        // Send the broadcast package!
                        try {
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, Constants.PORT);
                            socket.send(sendPacket);
                        } catch (Exception e) {
                        }
                    }
                }

                //Wait for a response
                byte[] recvBuf = new byte[15000];
                DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(receivePacket);

                //We have a response
                //Check if the message is correct
                String message = new String(receivePacket.getData()).trim();
                if (message.equals(Constants.DISCOVER_RESPONSE)) {
                    //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                    serverFound = false;
                    System.out.println("Server IP : " + receivePacket.getAddress().getHostAddress());
                }
                //Close the port!
                socket.close();

            } catch (Exception ex) {
                System.out.println("Server not found, retrying...");
            }
            try {
                Thread.sleep(broadcastDelay);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
