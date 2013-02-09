package org.elasticsearch.module.graphite.test;

import org.elasticsearch.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class GraphiteMockServer extends Thread {

    private int port;
    public List<String> content = Lists.newArrayList();
    private ServerSocket server;
    private boolean isClosed = false;

    public GraphiteMockServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            Socket client;

            while (!isClosed) {
                if (server.isClosed()) return;

                client = server.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                String msg;
                while ((msg = in.readLine()) != null) {
                    content.add(msg.trim());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws Exception {
        isClosed = true;
        server.close();
    }
}
