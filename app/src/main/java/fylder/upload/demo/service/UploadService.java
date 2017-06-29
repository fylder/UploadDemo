package fylder.upload.demo.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import fylder.upload.demo.event.UploadResponse;
import fylder.upload.demo.event.UploadStatus;
import fylder.upload.demo.service.bean.FileQueue;
import fylder.upload.demo.service.bean.ServiceData;
import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * Created by fylder on 2017/4/27.
 */

@SuppressLint("Registered")
public class UploadService extends Service {

    private static final int RUNNING_MAX = 5;
    static int running = 0;
    Queue<FileQueue> fileQueues = new LinkedList<>();
    static SparseArray<FileQueue> datas = new SparseArray<>();
    boolean startStat = true;
    boolean hasRun = false;

    Executor executor = new ThreadPoolExecutor(5, 5, 10,
            TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(5));

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HermesEventBus.getDefault().register(this);
        runThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ServiceData data = intent.getParcelableExtra("ahh");
        List<FileQueue> fileQueues = data.getFileQueues();
        for (FileQueue f : fileQueues) {
            datas.put(f.getMid(), f);
            Log.i("test", "onStartCommand key:" + f.getMid());
        }
        for (int i = 0; i < datas.size(); i++) {
            sendFile(datas.valueAt(i));//上传文件放入队列
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HermesEventBus.getDefault().unregister(this);
        Log.w("test", "onDestroy");
    }

    /**
     * 发送文件
     *
     * @param fileQueue
     */
    public void sendFile(FileQueue fileQueue) {
        if (!fileQueues.contains(fileQueue)) {
            fileQueues.offer(fileQueue);
        }
    }

    Thread runThread = new Thread(() -> {
        while (startStat) {
            try {
                if (fileQueues.size() > 0) {
                    Thread.sleep(100);
                } else {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runUpload();
        }

        stopSelf();
        Log.w("test", "stopSelf");

        UploadStatus response = new UploadStatus();
        response.setStatus(2);
        HermesEventBus.getDefault().post(response);
    });

    /**
     * 控制上传
     */
    private void runUpload() {
        if (fileQueues.size() > 0) {
            if (running < RUNNING_MAX) {
                FileQueue fileQueue = fileQueues.poll();
                if (fileQueue != null) {
                    upload(fileQueue);
                }
                hasRun = true;
            }
        } else if (hasRun && running == 0) {
            startStat = false;
            Log.w("test", "startStat:" + false);
        }
    }

    /**
     * 上传文件
     */

    private void upload(FileQueue fileQueue) {
        UploadStatus response = new UploadStatus();
        response.setStatus(1);
        HermesEventBus.getDefault().post(response);
        new UploadTask(fileQueue).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//异步多线程
//        new UploadTask(fileQueue).executeOnExecutor(executor);//异步多线程
    }

    static class UploadTask extends AsyncTask<Void, Void, Integer> {

        FileQueue fileQueue;

        UploadTask(FileQueue fileQueue) {
            this.fileQueue = fileQueue;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int key = 0;
            for (int i = 0; i < datas.size(); i++) {
                if (datas.valueAt(i).getMid() == fileQueue.getMid()) {
                    key = datas.keyAt(i);
                    break;
                }
            }
            running++;
            int pro = 1;
            while (pro <= 100) {
                UploadResponse response = new UploadResponse();
                response.setState(1);
                response.setPro(pro);
                response.setFileQueue(fileQueue);
                HermesEventBus.getDefault().post(response);
                datas.get(key).setPro(pro);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (fileQueue.getMid() % 3 == 2) {
                    pro += 7;
                } else {
                    pro += 2;
                }
            }
            return fileQueue.getMid();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            UploadResponse response = new UploadResponse();
            response.setState(2);
            response.setPro(100);
            response.setFileQueue(fileQueue);

            HermesEventBus.getDefault().post(response);
            running--;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    protected void getServiceData(UploadStatus data) {
        if (data.getStatus() == 100) {

            List<FileQueue> fileQueues = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                fileQueues.add(datas.valueAt(i));
            }

            UploadStatus response = new UploadStatus();
            response.setStatus(200);
            response.setDatas(fileQueues);
            HermesEventBus.getDefault().post(response);
        }
    }
}
