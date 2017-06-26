package fylder.upload.demo.service;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fylder on 2017/4/27.
 */

public class FileQueue implements Parcelable {

    private String path;
    private int mid;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeInt(this.mid);
    }

    public FileQueue() {
    }

    protected FileQueue(Parcel in) {
        this.path = in.readString();
        this.mid = in.readInt();
    }

    public static final Parcelable.Creator<FileQueue> CREATOR = new Parcelable.Creator<FileQueue>() {
        @Override
        public FileQueue createFromParcel(Parcel source) {
            return new FileQueue(source);
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
}
