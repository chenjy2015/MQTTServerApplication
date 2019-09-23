package com.example.mqttserviceapplication;

import android.util.Log;

import com.example.baselibrary.DoubleClickObservableTransformer;
import com.example.baselibrary.ui.BaseUIActivity;
import com.example.mqtt.MQTTContacts;
import com.example.mqtt.MQTTProxy;
import com.example.mqtt.MQTTServerManager;
import com.example.mqtt.PushCallback;
import com.example.mqttserviceapplication.databinding.ActivityServiceBinding;
import com.jakewharton.rxbinding3.view.RxView;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public class ServiceActivity extends BaseUIActivity<ActivityServiceBinding, ServiceViewModel> implements IMqttActionListener {

    PushCallback callback;
    MQTTProxy mqttProxy;

    @Override
    public int getLayoutId() {
        return R.layout.activity_service;
    }

    @Override
    public void init() {

        callback = new PushCallback(MQTTServerManager.getInstance());
        callback.setCallBack((topicName, topicMsg) -> {
            if (topicMsg != null) {
                dataBinding.msgInput.setText(topicMsg);
            }
        });
        mqttProxy = com.example.mqtt.MQTTProxy.getInstance()
                .init(this, com.example.mqtt.MQTTServerManager.CLIENT_ID, "admin", "password");
        mqttProxy.connect(this);
    }

    @Override
    public void initEvent() {
        addDisposable(
                RxView.clicks(dataBinding.send)
                        .compose(new DoubleClickObservableTransformer())
                        .subscribe(o -> {
                            viewModel.send(com.example.mqtt.MQTTServerManager.TOPIC,
                                    "给客户端124推送的信息 --" + dataBinding.msgInput.getText().toString());
                            viewModel.send(com.example.mqtt.MQTTServerManager.TOPIC125,
                                    "给客户端125推送的信息 --" + dataBinding.msgInput.getText().toString());
                        })
        );
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        //注册接收者 接收发给当前服务Id的消息
        mqttProxy.subscribeMsg(MQTTServerManager.CLIENT_ID, MQTTContacts.QoS.QoSAtLeastOnce.type);
        Log.d(com.example.mqtt.MQTTServerManager.TAG, "connect success");
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

    }
}
