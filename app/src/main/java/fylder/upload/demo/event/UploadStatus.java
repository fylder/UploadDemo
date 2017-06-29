package fylder.upload.demo.event;

import android.util.SparseArray;

import java.util.List;

import fylder.upload.demo.service.bean.FileQueue;

/**
 * Created by fylder on 2017/6/28.
 */

public class UploadStatus {

    public int status;//1:开始   2:结束    100:通知获取数据  200:返回数据
    public List<FileQueue> datas;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<FileQueue> getDatas() {
        return datas;
    }

    public void setDatas(List<FileQueue> datas) {
        this.datas = datas;
    }
}
