/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.vncthumbnailviewer.discovery;

import com.epic.vncthumbnailviewer.VncThumbnailViewer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Epic
 */
public class DiscoveryThread extends Thread {

    private final VncThumbnailViewer viewer;
    private final int globalPort;
    private final String globalPassword;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DiscoveryThread discoveryThread = new DiscoveryThread();
        discoveryThread.start();
    }

    public DiscoveryThread() {
        viewer = null;
        this.globalPort = 5900;
        this.globalPassword = "";
    }

    public DiscoveryThread(VncThumbnailViewer viewer, int port, String password) {
        this.viewer = viewer;
        this.globalPort = port;
        this.globalPassword = password;
    }

    DatagramSocket socket;
    public Map<String, InetAddress> clients;

    @Override
    public void run() {
        try {
            //Keep a socket open to listen to all the UDP trafic that is destined for this port
            socket = new DatagramSocket(Constants.PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);

            clients = new HashMap();
            System.out.println("Server ready to handle packets");

            while (true) {
//                System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");

                //Receive a packet
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                //Packet received
//                System.out.println(getClass().getName() + ">>>Discovery packet received from: " + packet.getAddress().getHostAddress());
//                System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));
                //See if the packet holds the right command (message)
                String message = new String(packet.getData()).trim();
                if (message.startsWith(Constants.DISCOVER_REQUEST)) {
                    //get other data in message
                    if (message.contains("__")) {
                        String[] split = message.split("__");
                        if (split.length > 2) {
                            String hostname = split[1];
                            String mac = split[2];
                            if (!clients.containsKey(hostname)) {
//                                System.out.println("Client detected : " + hostname + " " + packet.getAddress().getHostAddress() + " " + mac);
//                                clients.put(hostname, packet.getAddress());
                                if (viewer != null) {
                                    viewer.launchViewer(hostname, packet.getAddress().getHostAddress(), this.globalPort, this.globalPassword, null, null);
                                }
                            }
                        }
                    }
                    byte[] sendData = Constants.DISCOVER_RESPONSE.getBytes();

                    //Send a response
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);

//                    System.out.println(getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
