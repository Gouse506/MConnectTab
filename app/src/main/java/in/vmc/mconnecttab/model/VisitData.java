package in.vmc.mconnecttab.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mukesh on 8/1/16.
 */
public class VisitData implements Parcelable {
    public static final Creator<VisitData> CREATOR = new Creator<VisitData>() {
        @Override
        public VisitData createFromParcel(Parcel in) {
            return new VisitData(in);
        }

        @Override
        public VisitData[] newArray(int size) {
            return new VisitData[size];
        }
    };
    private String message, code;
    private String sitename;
    private String siteid;
    private String siteicon;
    private String sitedesc;
    private String number;
    private String offer;
    private String bid;
    private String propertyname;
    //  private boolean delete;
    private ArrayList<OptionsData> optionsData = new ArrayList<OptionsData>();
    private String offer_desc;
    private boolean Like;
    public VisitData() {
    }

    protected VisitData(Parcel in) {
        sitename = in.readString();
        siteid = in.readString();
        siteicon = in.readString();
        sitedesc = in.readString();
        number = in.readString();
        offer = in.readString();
        bid = in.readString();
        // delete = in.readByte() != 0;
        optionsData = in.createTypedArrayList(OptionsData.CREATOR);
        offer_desc = in.readString();
        Like = in.readByte() != 0;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPropertyname() {
        return propertyname;
    }

    public void setPropertyname(String propertyname) {
        this.propertyname = propertyname;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sitename);
        dest.writeString(siteid);
        dest.writeString(siteicon);
        dest.writeString(sitedesc);
        dest.writeString(number);
        dest.writeString(offer);
        dest.writeString(bid);
        dest.writeString(propertyname);
        //   dest.writeByte((byte) (delete ? 1 : 0));
        dest.writeTypedList(optionsData);
        dest.writeString(offer_desc);
        dest.writeByte((byte) (Like ? 1 : 0));
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

//    public boolean isDelete() {
//        return delete;
//    }
//
////    public void setDelete(boolean delete) {
////        this.delete = delete;
////    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getOffer_desc() {
        return offer_desc;
    }

    public void setOffer_desc(String offer_desc) {
        this.offer_desc = offer_desc;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public boolean isLike() {
        return Like;
    }

    public void setLike(boolean like) {
        Like = like;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public String getSiteicon() {
        return siteicon;
    }

    public void setSiteicon(String siteicon) {
        this.siteicon = siteicon;
    }

    public String getSitedesc() {
        return sitedesc;
    }

    public void setSitedesc(String sitedesc) {
        this.sitedesc = sitedesc;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    @Override
    public String toString() {
        return "VisitData{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", sitename='" + sitename + '\'' +
                ", siteid='" + siteid + '\'' +
                ", siteicon='" + siteicon + '\'' +
                ", sitedesc='" + sitedesc + '\'' +
                ", number='" + number + '\'' +
                ", offer='" + offer + '\'' +
                ", bid='" + bid + '\'' +
                ", propertyname='" + propertyname + '\'' +
                ", optionsData=" + optionsData +
                ", offer_desc='" + offer_desc + '\'' +
                ", Like=" + Like +
                '}';
    }
}
