package fylder.upload.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import fylder.upload.demo.event.RunThread;
import fylder.upload.demo.event.UploadResponse;
import fylder.upload.demo.service.FileQueue;
import fylder.upload.demo.service.UploadService;

public class DemoActivity extends AppCompatActivity {

    TextView showText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    int count = 0;

    void init() {
        showText = (TextView) findViewById(R.id.demo_text);
        findViewById(R.id.demo_send).setOnClickListener(v -> {

            count++;
            FileQueue fileQueue = new FileQueue();
            fileQueue.setMid(count);
            fileQueue.setPath("ahh");

            Intent intent = new Intent(this, UploadService.class);
            intent.putExtra("ahh", fileQueue);
            startService(intent);
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void response(UploadResponse response) {
        String s;
        if (response.getState() == 1) {
            s = new Date().getTime() + "--start:";
        } else {
            s = "finish:";
        }
        s += response.getFileQueue().getMid() + "\n";
        showText.append(s);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void runThread(RunThread runThread) {
        String s = "go:" + runThread.getIndex() + "\tmsg:" + runThread.getMsg() + "\n";
        showText.append(s);
    }

}
