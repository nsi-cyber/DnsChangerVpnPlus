package com.example.dnschangervpnt;

import android.content.Intent;
import android.net.VpnService;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.Type;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Date;

public class LookUpService extends VpnService {
    String deviceIp;
    Thread mThread;
    int localPort;
    String[] dns1 = {"8.8.8.8"};
    String[] dns2 = {"1.1.1.1"};
    String[] dns3 = {"208.67.222.222"};
    String[] dns=new String[1];
    //,,,"127.0.0.1"
    byte[] addr= new byte[]{8,8,8,8};
    String dnsblDomain = ".in-addr.arpa";
    String[] dnsEs = {"8.8.8.8"+dnsblDomain,"1.1.1.1"+dnsblDomain,"208.67.222.222"+dnsblDomain};
    LocalBroadcastManager broadcaster;
    Record[] records;
    Lookup lookup;
    long latency = 0;
    Name asd;
    Date now = new Date();
    long[] latencyArr=new long[3];
    public int onStartCommand(Intent intent, int flags, int startId){
        broadcaster = LocalBroadcastManager.getInstance(this);
        deviceIp = intent.getStringExtra("deviceips");

        try {
             localPort = new ServerSocket(0).getLocalPort();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mThread = new Thread(() -> {
            try {
                lokup();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Test");
    //start the service
        mThread.start();
        return START_STICKY;
    }

















public void lokup() throws IOException {

  

        for (int i = 0; i <dnsEs.length ; i++) {
            if(i==0){dns=dns1;}

                else if(i==1){  dns=dns2;}

                    else if(i==2){  dns=dns3;
                }

            asd = Name.fromString(dnsEs[i]);

         Resolver res = new ExtendedResolver(dns);

            lookup = new Lookup(asd, Type.PTR);
            lookup.setResolver(res);
            long msSend = System.currentTimeMillis();
            lookup.run();
            latencyArr[i] = System.currentTimeMillis() - msSend;
            int result = lookup.getResult();
            if (result==Lookup.TRY_AGAIN) {
                throw new IOException("Network error when trying to look up "+ Arrays.toString(addr) +", try again.");
            }
            System.out.println(latencyArr[i]);







}
    Intent local = new Intent();

    local.setAction("com.hello.action");
local.putExtra("data",latencyArr);
    this.sendBroadcast(local);

}

}
