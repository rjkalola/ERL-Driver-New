package com.app.erldriver.network;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mine-pc on 03-10-2017.
 */


public class FileDownloadResponse implements Parcelable{

    public FileDownloadResponse() {

    }

    public String message;
    public int status;

    private String fileName;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeString(message);
        dest.writeString(fileName);

    }

    FileDownloadResponse(Parcel in) {
        status = in.readInt();
        message = in.readString();
        fileName = in.readString();
    }

    public static final Creator<FileDownloadResponse> CREATOR = new Creator<FileDownloadResponse>() {
        public FileDownloadResponse createFromParcel(Parcel in) {
            return new FileDownloadResponse(in);
        }

        public FileDownloadResponse[] newArray(int size) {
            return new FileDownloadResponse[size];
        }
    };

}
