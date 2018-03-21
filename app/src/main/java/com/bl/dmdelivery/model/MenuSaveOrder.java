package com.bl.dmdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tHundorn_j on 21/2/2561.
 */

public class MenuSaveOrder implements Parcelable{

    private String menuname;
    private String menuname_image;
    private String menuname_type;
    private String menuname_mode;

    public class Column{
        public static final String menuname = "menuname";
        public static final String menuname_image = "image";
        public static final String menuname_type = "type";
        public static final String menuname_mode = "mode";
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }


    public String getMenuname_image() {
        return menuname_image;
    }

    public void setMenuname_image(String menuname_image) {
        this.menuname_image = menuname_image;
    }

    public String getMenuname_type() {
        return menuname_type;
    }

    public void setMenuname_type(String menuname_type) {
        this.menuname_type = menuname_type;
    }


    public String getMenuname_mode() {
        return menuname_mode;
    }

    public void setMenuname_mode(String menuname_mode) {
        this.menuname_mode = menuname_mode;
    }



    public MenuSaveOrder() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.menuname);
        dest.writeString(this.menuname_image);
        dest.writeString(this.menuname_mode);
    }

    protected MenuSaveOrder(Parcel in) {
        this.menuname = in.readString();
        this.menuname_image = in.readString();
        this.menuname_mode = in.readString();
    }

    public static final Creator<MenuSaveOrder> CREATOR = new Creator<MenuSaveOrder>() {
        @Override
        public MenuSaveOrder createFromParcel(Parcel source) {
            return new MenuSaveOrder(source);
        }

        @Override
        public MenuSaveOrder[] newArray(int size) {
            return new MenuSaveOrder[size];
        }
    };
}
