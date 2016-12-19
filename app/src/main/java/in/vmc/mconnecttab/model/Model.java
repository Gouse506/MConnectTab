package in.vmc.mconnecttab.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mukesh on 30/12/15.
 */
public class Model implements Parcelable {

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };
    private String Code;
    private String Logo;
    private String Description;
    private ArrayList<String> Images;
    private String Name;
    private String VedioUrl;
    private String SiteId;
    private String bid;
    private boolean like;
    private ArrayList<OptionsData> optionsData = new ArrayList<OptionsData>();
    private String beacinId = "";
    private String Phone;
    private String Message;

    public Model() {
    }

    protected Model(Parcel in) {
        Code = in.readString();
        Logo = in.readString();
        Description = in.readString();
        Images = in.createStringArrayList();
        Name = in.readString();
        VedioUrl = in.readString();
        SiteId = in.readString();
        bid = in.readString();
        like = in.readByte() != 0;
        optionsData = in.createTypedArrayList(OptionsData.CREATOR);
        beacinId = in.readString();
        Phone = in.readString();
        Message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Code);
        dest.writeString(Logo);
        dest.writeString(Description);
        dest.writeStringList(Images);
        dest.writeString(Name);
        dest.writeString(VedioUrl);
        dest.writeString(SiteId);
        dest.writeString(bid);
        dest.writeByte((byte) (like ? 1 : 0));
        dest.writeTypedList(optionsData);
        dest.writeString(beacinId);
        dest.writeString(Phone);
        dest.writeString(Message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<OptionsData> getOptionsData() {
        return optionsData;
    }

    public void setOptionsData(ArrayList<OptionsData> optionsData) {
        this.optionsData = optionsData;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBeacinId() {
        return beacinId;
    }

    public void setBeacinId(String beacinId) {
        this.beacinId = beacinId;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getSiteId() {
        return SiteId;
    }

    public void setSiteId(String siteId) {
        SiteId = siteId;
    }

    public String getVedioUrl() {
        return VedioUrl;
    }

    public void setVedioUrl(String vedioUrl) {
        VedioUrl = vedioUrl;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<String> getImages() {
        return Images;
    }

    public void setImages(ArrayList<String> images) {
        Images = images;
    }


}
