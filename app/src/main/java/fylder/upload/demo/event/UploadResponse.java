package fylder.upload.demo.event;

import fylder.upload.demo.service.FileQueue;

/**
 * Created by fylder on 2017/4/27.
 */

public class UploadResponse {

    int state;
    FileQueue fileQueue;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public FileQueue getFileQueue() {
        return fileQueue;
    }

    public void setFileQueue(FileQueue fileQueue) {
        this.fileQueue = fileQueue;
    }
}
