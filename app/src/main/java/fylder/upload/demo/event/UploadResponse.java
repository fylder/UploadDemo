package fylder.upload.demo.event;

import fylder.upload.demo.service.bean.FileQueue;

/**
 * Created by fylder on 2017/4/27.
 */

public class UploadResponse {

    private int state;
    private int pro;
    private FileQueue fileQueue;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPro() {
        return pro;
    }

    public void setPro(int pro) {
        this.pro = pro;
    }

    public FileQueue getFileQueue() {
        return fileQueue;
    }

    public void setFileQueue(FileQueue fileQueue) {
        this.fileQueue = fileQueue;
    }
}
