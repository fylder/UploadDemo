package fylder.upload.demo.service.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ym-005 on 2017/6/28.
 */

public class ServiceData implements Parcelable {

    int type;
    List<FileQueue> fileQueues;

    public ServiceData() {
    }


    protected ServiceData(Parcel in) {
        type = in.readInt();
        fileQueues = in.createTypedArrayList(FileQueue.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeTypedList(fileQueues);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceData> CREATOR = new Creator<ServiceData>() {
        @Override
        public ServiceData createFromParcel(Parcel in) {
            return new ServiceData(in);
        }

        @Override
        public ServiceData[] newArray(int size) {
            return new ServiceData[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<FileQueue> getFileQueues() {
        return fileQueues;
    }

    public void setFileQueues(List<FileQueue> fileQueues) {
        this.fileQueues = fileQueues;
    }
}
