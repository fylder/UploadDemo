package fylder.upload.demo.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import fylder.upload.demo.event.RunThread;
import fylder.upload.demo.event.UploadResponse;

/**
 * Created by fylder on 2017/4/27.
 */

@SuppressLint("Registered")
public class UploadService extends Service {

    private static final int RUNNING_MAX = 5;
    static int running = 0;
    Queue<FileQueue> fileQueues = new LinkedList<>();
    boolean startStat = true;
    boolean hasRun = false;

    Executor executor = new ThreadPoolExecutor(3, 5, 10,
            TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(5));

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        runThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FileQueue fileQueue = intent.getParcelableExtra("ahh");
        sendFile(fileQueue);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

        AsyncTask<Void, Void, Integer> uploadTask = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                Log.i("test", "start:" + fileQueue.getMid());
                UploadResponse response = new UploadResponse();
                response.setState(1);
                response.setFileQueue(fileQueue);
                EventBus.getDefault().post(response);
                running++;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return fileQueue.getMid();
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.w("test", "finish:" + integer);
                UploadResponse response = new UploadResponse();
                response.setState(2);
                response.setFileQueue(fileQueue);

                EventBus.getDefault().post(response);
                running--;
            }
        };
        uploadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//异步多线程

    }

    void msg(int index, String msg) {
        RunThread runThread = new RunThread();
        runThread.setIndex(index);
        runThread.setMsg(msg);
        EventBus.getDefault().post(runThread);
    }


}
