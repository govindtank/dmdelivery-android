package com.bl.dmdelivery.helper;


import android.database.Cursor;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by nitisak_p on 9/12/2558.
 */
public class GlobalObject {

    private static GlobalObject instance;

    private String mTruck,mDate,mSerialNumber,mFragile,mDelCount,mSign;
    private Integer screenWidth,screenHeight;
    private Boolean mUpdGpsOK=true;
    public static Cursor mSaveCursor;

    private static File mGoldClub_dir =  null;

    public static ArrayList listOrderData,listUnpackData;

    private GlobalObject(){}


    public String getTruck(){
        return mTruck;
    }
    public void setTruck(String truck){
        mTruck = truck;
    }

    public String getDate(){
        return mDate;
    }
    public void setDate(String Date){
        mDate = Date;
    }

    public String getSerialNumber(){
        return mSerialNumber;
    }
    public void setSerialNumber(String SerialNumber){
        mSerialNumber = SerialNumber;
    }

    public String getFragile(){
        return mFragile;
    }
    public void setFragile(String fragile){
        mFragile = fragile;
    }

    public Integer getScreenWidth(){
        return screenWidth;
    }
    public void setScreenWidth(Integer screenwidth){
        screenWidth = screenwidth;
    }

    public Integer getScreenHeight(){
        return screenHeight;
    }
    public void setScreenHeight(Integer screenheight){
        screenHeight = screenheight;
    }

    public String getDelCount(){
        return mDelCount;
    }
    public void setDelCount(String delCount){
        mDelCount = delCount;
    }

    public String getSign(){
        return mSign;
    }
    public void setSign(String sign){
        mSign = sign;
    }

    public Boolean getUpdGpsOK(){
        return mUpdGpsOK;
    }
    public void setUpdGpsOK(Boolean updGpsOK){
        mUpdGpsOK = updGpsOK;
    }

    public File getGoldClub_dir(){
        return mGoldClub_dir;
    }
    public void setGoldClub_dir(File goldClub_dir){
        mGoldClub_dir = goldClub_dir;
    }


    public ArrayList getListCheckOrderData() {
        return listOrderData;
    }
    public void setListCheckOrderData(ArrayList listOrderData) {
        this.listOrderData = listOrderData;
    }

    public ArrayList getListUnpackData() {
        return listUnpackData;
    }
    public void setListCheckUnpackData(ArrayList listUnpackData) {
        this.listUnpackData = listUnpackData;
    }


    public Cursor getmSaveCursor(){
        return mSaveCursor;
    }
    public void setmSaveCursor(Cursor saveCursor){
        mSaveCursor = saveCursor;
    }


    public static synchronized GlobalObject getInstance(){
        if(instance==null){
            instance=new GlobalObject();
        }
        return instance;
    }

}
