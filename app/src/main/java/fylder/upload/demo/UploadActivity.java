package fylder.upload.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.widget.Button;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fylder.upload.demo.adapter.ImageAdapter;
import fylder.upload.demo.event.UploadResponse;
import fylder.upload.demo.event.UploadStatus;
import fylder.upload.demo.model.ImageModel;
import fylder.upload.demo.service.UploadService;
import fylder.upload.demo.service.bean.FileQueue;
import fylder.upload.demo.service.bean.ServiceData;
import xiaofei.library.hermeseventbus.HermesEventBus;

public class UploadActivity extends AppCompatActivity {

    @BindView(R.id.upload_send)
    Button uploadBtn;
    @BindView(R.id.upload_recycler)
    RecyclerView recyclerView;

    ImageAdapter adapter;
    SparseArray<ImageModel> datas = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        HermesEventBus.getDefault().register(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UploadStatus response = new UploadStatus();
        response.setStatus(100);
        HermesEventBus.getDefault().post(response);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            datas.put(i, imageModel);
        }
        adapter.setDatas(datas);


    }

    @OnClick(R.id.upload_send)
    void start() {
        for (int i = 0; i < datas.size(); i++) {
            datas.valueAt(i).setPro(0);
        }
        adapter.setDatas(datas);
        List<FileQueue> fileQueues = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            FileQueue fileQueue = new FileQueue();
            fileQueue.setMid(datas.valueAt(i).getId());
            fileQueue.setPath(datas.valueAt(i).getPath());
            fileQueues.add(fileQueue);
        }
        ServiceData datas = new ServiceData();
        datas.setFileQueues(fileQueues);
        Intent intent = new Intent(this, UploadService.class);
        intent.putExtra("ahh", datas);
        startService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    protected void response(UploadStatus response) {
        int s = response.getStatus();
        if (s == 1) {
            uploadBtn.setText("uploading");
            uploadBtn.setEnabled(false);
            uploadBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.upload_btn));
        } else if (s == 2) {
            uploadBtn.setText("finish");
            uploadBtn.setEnabled(true);
            uploadBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            finish();
        } else if (s == 200) {
            uploadBtn.setText("uploading");
            uploadBtn.setEnabled(false);
            uploadBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.upload_btn));
            List<FileQueue> fileQueues = response.getDatas();
            for (FileQueue f : fileQueues) {
                int key = f.getMid();
                datas.get(key).setPro(f.getPro());
            }
            adapter.setDatas(datas);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    protected void response(UploadResponse response) {
        int pro = response.getPro();
        int id = response.getFileQueue().getMid();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.valueAt(i).getId() == id) {
                datas.valueAt(i).setPro(pro);
                adapter.setDatas(datas);
                break;
            }
        }
    }

}
