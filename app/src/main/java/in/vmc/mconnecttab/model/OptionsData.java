package in.vmc.mconnecttab.model;


import android.os.Parcel;
import android.os.Parcelable;

public class OptionsData implements Parcelable {

    public static final Creator<OptionsData> CREATOR = new Creator<OptionsData>() {
        @Override
        public OptionsData createFromParcel(Parcel in) {
            return new OptionsData(in);
        }

        @Override
        public OptionsData[] newArray(int size) {
            return new OptionsData[size];
        }
    };
    private String OptionId;
    private String OptionName;
    private boolean IsChecked;

    public OptionsData() {

    }

    protected OptionsData(Parcel in) {
        OptionId = in.readString();
        OptionName = in.readString();
        IsChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OptionId);
        dest.writeString(OptionName);
        dest.writeByte((byte) (IsChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getOptionId() {
        return OptionId;
    }

    public void setOptionId(String optionId) {
        OptionId = optionId;
    }

    public String getOptionName() {
        return OptionName;
    }

    public void setOptionName(String optionName) {
        OptionName = optionName;
    }

    public boolean isChecked() {
        return IsChecked;
    }

    public void setChecked(boolean isChecked) {
        IsChecked = isChecked;
    }


}

