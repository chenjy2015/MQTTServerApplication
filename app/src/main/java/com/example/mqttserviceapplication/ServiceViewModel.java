package com.example.mqttserviceapplication;

import android.content.Context;
import android.util.Log;

import com.example.baselibrary.BaseViewModel;
import com.example.mqtt.MQTTContacts;
import com.example.mqtt.MQTTProxy;
import com.example.mqtt.MQTTServerManager;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;

public class ServiceViewModel implements BaseViewModel {
    public void login() {
        Log.d("login", "login !!!");
    }

    public void register(Context context, String topic, String userName, String password){
        MQTTProxy.getInstance().init(context, topic, userName, password);
    }

    public void connect(IMqttActionListener actionListener) {
        MQTTProxy.getInstance().connect(actionListener);
    }

    public void send(String topic, String msg){
        MQTTProxy.getInstance().publish(topic, msg, true, MQTTContacts.QoS.QoSForOnce.type);
    }

    @Override
    public void destroy() {
        MQTTServerManager.getInstance().release();
    }
}
