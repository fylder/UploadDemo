package fylder.upload.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fylder.upload.demo.adapter.ImageAdapter;
import fylder.upload.demo.event.UploadResponse;
import fylder.upload.demo.event.UploadStatus;
import fylder.upload.demo.model.ImageModel;
import fylder.upload.demo.service.FileQueue;
import fylder.upload.demo.service.UploadService;
import xiaofei.library.hermeseventbus.HermesEventBus;

public class DemoActivity extends AppCompatActivity {

    @BindView(R.id.demo_send)
    Button uploadBtn;
    @BindView(R.id.demo_recycler)
    RecyclerView recyclerView;

    ImageAdapter adapter;
    List<ImageModel> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        HermesEventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        HermesEventBus.getDefault().unregister(this);
    }


    void init() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ImageAdapter(this);
        recyclerView.setAdapter(adapter);
        for (int i = 1; i <= 7; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setId(i);
            imageModel.setPro(1);
            imageModel.setPath("fylder:" + i);
            datas.add(imageModel);
        }
        adapter.setDatas(datas);
    }

    @OnClick(R.id.demo_send)
    void start() {
        for (ImageModel data : datas) {
            data.setPro(0);
        }
        adapter.setDatas(datas);

        for (ImageModel data : datas) {
            FileQueue fileQueue = new FileQueue();
            fileQueue.setMid(data.getId());
            fileQueue.setPath(data.getPath());
            Intent intent = new Intent(this, UploadService.class);
            intent.putExtra("ahh", fileQueue);
            startService(intent);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    protected void response(UploadStatus response) {
        int s = response.getStatus();
        if (s == 1) {
            uploadBtn.setText("upload");
            uploadBtn.setEnabled(false);
            uploadBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.upload_btn));
        } else if (s == 2) {
            uploadBtn.setText("finish");
            uploadBtn.setEnabled(true);
            uploadBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    protected void response(UploadResponse response) {
        int pro = response.getPro();
        int id = response.getFileQueue().getMid();
        for (ImageModel d : datas) {
            if (d.getId() == id) {
                d.setPro(pro);
                adapter.setDatas(datas);
                break;
            }
        }
    }

}
