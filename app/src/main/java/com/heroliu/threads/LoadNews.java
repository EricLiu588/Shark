package com.heroliu.threads;

import android.os.Bundle;
import android.os.Message;

import java.util.ArrayList;

/**
 * Created by Eric on 6/17/16.
 */
public class LoadNews extends Thread implements Runnable{
    @Override
    public void run() {
        ArrayList<CharSequence> names = new ArrayList<>();
        names.add(0,"user1");
        names.add(1,"user2");
        names.add(2,"user3");
        names.add(3,"user4");
        names.add(4,"user5");
        names.add(5,"user6");
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putCharSequenceArrayList("names",names);
        msg.setData(bundle);
        msg.sendToTarget();
    }
}
