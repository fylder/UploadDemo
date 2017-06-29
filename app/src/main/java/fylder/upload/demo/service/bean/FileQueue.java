package fylder.upload.demo.service.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fylder on 2017/4/27.
 */

public class FileQueue implements Parcelable {

    private String path;
    private int mid;
    private int pro;

    public FileQueue() {
    }

    protected FileQueue(Parcel in) {
        path = in.readString();
        mid = in.readInt();
        pro = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeInt(mid);
        dest.writeInt(pro);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FileQueue> CREATOR = new Creator<FileQueue>() {
        @Override
        public FileQueue createFromParcel(Parcel in) {
            return new FileQueue(in);
        }

        @Override
        public FileQueue[] newArray(int size) {
            return new FileQueue[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getPro() {
        return pro;
    }

    public void setPro(int pro) {
        this.pro = pro;
    }
}
