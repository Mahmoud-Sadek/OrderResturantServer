package com.example.mahmoudsadek.orderresturantserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mahmoudsadek.orderresturantserver.common.Common;
import com.example.mahmoudsadek.orderresturantserver.model.DataMessage;
import com.example.mahmoudsadek.orderresturantserver.model.MyResponse;
import com.example.mahmoudsadek.orderresturantserver.remote.APIService;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMessageActivity extends AppCompatActivity {

    MaterialEditText edtMessage, edtTitle;
    Button btnSendMessage;

    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        mService = Common.getFCMClient();

        edtMessage = findViewById(R.id.edtMessage);
        edtTitle = findViewById(R.id.edtTitle);
        btnSendMessage = findViewById(R.id.bnSend);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.AlertDialog waitingDialog = new SpotsDialog(SendMessageActivity.this);
                waitingDialog.show();

                /*Notification notification = new Notification(edtMessage.getText().toString() ,edtTitle.getText().toString());

                Sender toTopic = new Sender();
                toTopic.to = new StringBuilder("/topics/").append(Common.TopicName).toString();
                toTopic.notification = notification;*/
                Map<String, String> dataSender = new HashMap<>();
                dataSender.put("title", edtTitle.getText().toString());
                dataSender.put("message", edtMessage.getText().toString());
                String to =  new StringBuilder("/topics/").append(Common.TopicName).toString();
                DataMessage dataMessage = new DataMessage(to, dataSender);


                mService.sendNotification(dataMessage).enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        if (response.isSuccessful()){
                            waitingDialog.dismiss();
                            Toast.makeText(SendMessageActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        waitingDialog.dismiss();
                        Toast.makeText(SendMessageActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}
