package com.bl.dmdelivery.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;


import com.bl.dmdelivery.adapter.OrderAdapter;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.model.OrderTemp;
import com.bl.dmdelivery.model.OrdersChangeList;
import com.bl.dmdelivery.model.Reason;
import com.bl.dmdelivery.model.Unpack;
import com.bl.dmdelivery.utility.TagUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by sd-m003 on 8/23/2017 AD.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DMDelivery.db";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase sqLiteDatabase;
    private final String TAG = getClass().getSimpleName();
    private static final String PATH = "/Android/data/";

    public static final String TableOrder = "Orders";
    public static final String TableUnpack = "Unpacks";
    public static final String TableOrderReturn = "OrderReturns";
    public static final String TableReason = "Reasons";
    public static final String TableOrderTemp = "OrdersTemp";

    public static String getAppPackagePath(Context _context) {
        final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        final String PACKAGE_NAME = _context.getPackageName();
        final String PACKAGE_PATH = PATH + PACKAGE_NAME;
        final String APP_PATH = SDCARD_PATH + PACKAGE_PATH;

        File directory = new File(APP_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return APP_PATH;
    }

    public DBHelper(Context context) {
        super(context, getAppPackagePath(context) + "/" + DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        String CREATE_ORDERTEMP_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY  AUTOINCREMENT,%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT)",
                TableOrderTemp,
                Order.Column.ID,
                Order.Column.Oucode,
                Order.Column.Trans_Year,
                Order.Column.Trans_Group,
                Order.Column.TransNo,
                Order.Column.Transdate,
                Order.Column.rep_seq,
                Order.Column.rep_code,
                Order.Column.rep_name,
                Order.Column.rep_nickname,
                Order.Column.address1,
                Order.Column.address2,
                Order.Column.tumbon,
                Order.Column.amphur,
                Order.Column.province,
                Order.Column.postal,
                Order.Column.rep_telno,
                Order.Column.dsm_name,
                Order.Column.dsm_telno,
                Order.Column.loc_code,
                Order.Column.trans_campaign,
                Order.Column.ord_campaign,
                Order.Column.ord_type,
                Order.Column.del_type,
                Order.Column.ord_flag_status,
                Order.Column.return_flag,
                Order.Column.unpack_items,
                Order.Column.order_flag_desc,
                Order.Column.delivery_desc,
                Order.Column.ordertype_desc,
                Order.Column.cont_desc,
                Order.Column.Itemno,
                Order.Column.delivery_status,
                Order.Column.isselect,
                Order.Column.cre_date,
                Order.Column.fullpathimage,
                Order.Column.lat,
                Order.Column.lon,
                Order.Column.signature_timestamp,
                Order.Column.reason_code,
                Order.Column.reason_note,
                Order.Column.send_status,
                Order.Column.mobile_serial,
                Order.Column.mobile_emei,
                Order.Column.mobile_battery,
                Order.Column.user_define1,
                Order.Column.user_define2,
                Order.Column.user_define3,
                Order.Column.user_define4,
                Order.Column.user_define5,
                Order.Column.return_order,
                Order.Column.return_status,
                Order.Column.delivery_date,
                Order.Column.truckNo,
                Order.Column.sendtoserver_timestamp
        );

        Log.i(TAG, CREATE_ORDERTEMP_TABLE);
        // create friend table
        db.execSQL(CREATE_ORDERTEMP_TABLE);

        //Order Return
        String CREATE_RETURN_TABLE = String.format("CREATE TABLE %s " +
                        "(%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT)",
                TableOrderReturn,
                OrderReturn.Column.ou_code,
                OrderReturn.Column.return_no,
                OrderReturn.Column.return_code,
                OrderReturn.Column.return_type,
                OrderReturn.Column.reftrans_no,
                OrderReturn.Column.reftrans_year,
                OrderReturn.Column.rep_code,
                OrderReturn.Column.rep_seq,
                OrderReturn.Column.rep_name,
                OrderReturn.Column.return_seq,
                OrderReturn.Column.fs_code,
                OrderReturn.Column.fs_desc,
                OrderReturn.Column.return_unit_real,
                OrderReturn.Column.return_unit,
                OrderReturn.Column.return_remark,
                OrderReturn.Column.return_status,
                OrderReturn.Column.reason_code,
                OrderReturn.Column.return_note,
                OrderReturn.Column.fullpathimage,
                OrderReturn.Column.track_no,
                OrderReturn.Column.delivery_date,
                OrderReturn.Column.lat,
                OrderReturn.Column.lon,
                OrderReturn.Column.signature_timestamp,
                OrderReturn.Column.sendtoserver_timestamp
        );

        Log.i(TAG, CREATE_RETURN_TABLE);
        // create friend table
        db.execSQL(CREATE_RETURN_TABLE);

        //Unpack
        String CREATE_UNPACK_TABLE = String.format("CREATE TABLE %s " +
                        "(%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TableUnpack,
                Unpack.Column.transno,
                Unpack.Column.unpack_code,
                Unpack.Column.unpack_desc,
                Unpack.Column.unpack_qty,
                Unpack.Column.unpack_image,
                Unpack.Column.rep_name
        );

        Log.i(TAG, CREATE_UNPACK_TABLE);

        // create friend table
        db.execSQL(CREATE_UNPACK_TABLE);


        //Reason
        String CREATE_REASON_TABLE = String.format("CREATE TABLE %s " +
                        "(%s TEXT, %s TEXT, %s TEXT)",
                TableReason,
                Reason.Column.reason_code,
                Reason.Column.reason_desc,
                Reason.Column.reason_type
        );

        Log.i(TAG, CREATE_REASON_TABLE);

        // create friend table
        db.execSQL(CREATE_REASON_TABLE);


        db.execSQL("CREATE TABLE IF NOT EXISTS " + TableOrder + " (" +
                Order.Column.ID + " INTEGER PRIMARY KEY  AUTOINCREMENT, " +
                Order.Column.Oucode + " TEXT, " +
                Order.Column.Trans_Year  + " TEXT, " +
                Order.Column.Trans_Group + " TEXT, " +
                Order.Column.TransNo  + " TEXT, " +
                Order.Column.Transdate  + " TEXT, " +
                Order.Column.rep_seq  + " TEXT, " +
                Order.Column.rep_code  + " TEXT, " +
                Order.Column.rep_name  + " TEXT, " +
                Order.Column.rep_nickname  + " TEXT, " +
                Order.Column.address1  + " TEXT, " +
                Order.Column.address2  + " TEXT, " +
                Order.Column.tumbon  + " TEXT, " +
                Order.Column.amphur  + " TEXT, " +
                Order.Column.province  + " TEXT, " +
                Order.Column.postal  + " TEXT, " +
                Order.Column.rep_telno  + " TEXT, " +
                Order.Column.dsm_name  + " TEXT, " +
                Order.Column.dsm_telno  + " TEXT, " +
                Order.Column.loc_code  + " TEXT, " +
                Order.Column.trans_campaign  + " TEXT, " +
                Order.Column.ord_campaign  + " TEXT, " +
                Order.Column.ord_type  + " TEXT, " +
                Order.Column.del_type  + " TEXT, " +
                Order.Column.ord_flag_status  + " TEXT, " +
                Order.Column.return_flag  + " TEXT, " +
                Order.Column.unpack_items  + " TEXT, " +
                Order.Column.order_flag_desc  + " TEXT, " +
                Order.Column.delivery_desc  + " TEXT, " +
                Order.Column.ordertype_desc  + " TEXT, " +
                Order.Column.cont_desc  + " TEXT, " +
                Order.Column.Itemno  + " TEXT, " +
                Order.Column.delivery_status  + " TEXT, " +
                Order.Column.isselect  + " TEXT, " +
                Order.Column.cre_date  + " TEXT, " +
                Order.Column.fullpathimage + " TEXT, " +
                Order.Column.lat + " TEXT, " +
                Order.Column.lon + " TEXT, " +
                Order.Column.signature_timestamp + " TEXT, " +
                Order.Column.reason_code + " TEXT, " +
                Order.Column.reason_note + " TEXT, " +
                Order.Column.send_status + " TEXT, " +
                Order.Column.mobile_serial + " TEXT, " +
                Order.Column.mobile_emei + " TEXT, " +
                Order.Column.mobile_battery + " TEXT, " +
                Order.Column.user_define1 + " TEXT, " +
                Order.Column.user_define2 + " TEXT, " +
                Order.Column.user_define3 + " TEXT, " +
                Order.Column.user_define4 + " TEXT, " +
                Order.Column.user_define5 + " TEXT, " +
                Order.Column.return_order + " TEXT, " +
                Order.Column.return_status + " TEXT, " +
                Order.Column.delivery_date + " TEXT, " +
                Order.Column.truckNo + " TEXT, " +
                Order.Column.sendtoserver_timestamp + " TEXT);");


        try
        {
            String ALTER_ORDER_TABLE = "ALTER TABLE " + TableOrder + " ADD COLUMN msllat TEXT DEFAULT '' ";
            db.execSQL(ALTER_ORDER_TABLE);
        }
        catch (Exception ex)
        {
            Log.d("DBHelper",ex.getMessage());
        }

        try
        {

            String ALTER_ORDER_TABLE = "ALTER TABLE " + TableOrder + " ADD COLUMN msllng TEXT DEFAULT '' ";
            db.execSQL(ALTER_ORDER_TABLE);
        }
        catch (Exception ex)
        {
            Log.d("DBHelper",ex.getMessage());
        }



    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_ORDER_TABLE = "DROP TABLE IF EXISTS " + TableOrderTemp;
        db.execSQL(DROP_ORDER_TABLE);

        String DROP_ORDERRETURN_TABLE = "DROP TABLE IF EXISTS " + TableOrderReturn;
        db.execSQL(DROP_ORDERRETURN_TABLE);

        String DROP_UNPACK_TABLE = "DROP TABLE IF EXISTS " + TableUnpack;
        db.execSQL(DROP_UNPACK_TABLE);

        String DROP_REASON_TABLE = "DROP TABLE IF EXISTS " + TableReason;
        db.execSQL(DROP_REASON_TABLE);

        String DELETE_TABLE = "";
        db.delete(TableOrder,"cre_date != ? ",new String[]{TagUtils.PREF_DELIVERY_DATE});




        Log.i(TAG, "Upgrade Database from " + oldVersion + " to " + newVersion);

        onCreate(db);
    }

    public void onUpgradeUnpack(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_UNPACK_TABLE = "DROP TABLE IF EXISTS " + TableUnpack;
        db.execSQL(DROP_UNPACK_TABLE);

        Log.i(TAG, "Upgrade Database unpack from " + oldVersion + " to " + newVersion);

        onCreateUnpack(db);
    }

    public void onCreateUnpack(SQLiteDatabase db) {


        //Unpack
        String CREATE_UNPACK_TABLE = String.format("CREATE TABLE %s " +
                        "(%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TableUnpack,
                Unpack.Column.transno,
                Unpack.Column.unpack_code,
                Unpack.Column.unpack_desc,
                Unpack.Column.unpack_qty,
                Unpack.Column.unpack_image,
                Unpack.Column.rep_name
        );

        Log.i(TAG, CREATE_UNPACK_TABLE);

        // create friend table
        db.execSQL(CREATE_UNPACK_TABLE);


    }



    public void addOrders(Order order) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Order.Column.Oucode, order.getOucode());
        values.put(Order.Column.Trans_Year, order.getYear());
        values.put(Order.Column.Trans_Group, order.getGroup());
        values.put(Order.Column.TransNo, order.getTransNo());
        values.put(Order.Column.Transdate, order.getTransdate());
        values.put(Order.Column.rep_seq, order.getRep_seq());
        values.put(Order.Column.rep_code, order.getRep_code());
        values.put(Order.Column.rep_name, order.getRep_name());
        values.put(Order.Column.rep_nickname, order.getRep_nickname());
        values.put(Order.Column.address1, order.getAddress1());
        values.put(Order.Column.address2, order.getAddress2());
        values.put(Order.Column.tumbon, order.getTumbon());
        values.put(Order.Column.amphur, order.getAmphur());
        values.put(Order.Column.province, order.getProvince());
        values.put(Order.Column.postal, order.getPostal());
        values.put(Order.Column.rep_telno, order.getRep_telno());
        values.put(Order.Column.dsm_name, order.getDsm_name());
        values.put(Order.Column.dsm_telno, order.getDsm_telno());
        values.put(Order.Column.loc_code, order.getLoc_code());
        values.put(Order.Column.trans_campaign, order.getTrans_campaign());
        values.put(Order.Column.ord_campaign, order.getOrd_campaign());
        values.put(Order.Column.ord_type, order.getOrd_type());
        values.put(Order.Column.del_type, order.getDel_type());
        values.put(Order.Column.ord_flag_status, order.getOrd_flag_status());
        values.put(Order.Column.return_flag, order.getReturn_flag());
        values.put(Order.Column.unpack_items, order.getUnpack_items());
        values.put(Order.Column.order_flag_desc, order.getOrder_flag_desc());
        values.put(Order.Column.delivery_desc, order.getDelivery_desc());
        values.put(Order.Column.ordertype_desc, order.getOrdertype_desc());
        values.put(Order.Column.cont_desc, order.getCont_desc());
        values.put(Order.Column.Itemno, order.getItemno());
        values.put(Order.Column.delivery_status,order.getDelivery_status());
        values.put(Order.Column.isselect,order.getIsselect());
        values.put(Order.Column.cre_date,order.getCre_date());
        values.put(Order.Column.fullpathimage,order.getFullpathimage());
        values.put(Order.Column.lat,order.getLat());
        values.put(Order.Column.lon,order.getLon());
        values.put(Order.Column.signature_timestamp,order.getSignature_timestamp());
        values.put(Order.Column.reason_code,order.getReason_code());
        values.put(Order.Column.reason_note,order.getReason_note());
        values.put(Order.Column.send_status,order.getSend_status());
        values.put(Order.Column.mobile_serial,order.getMobile_serial());
        values.put(Order.Column.mobile_emei,order.getMobile_emei());
        values.put(Order.Column.mobile_battery,order.getMobile_battery());
        values.put(Order.Column.user_define1,order.getUser_define1());
        values.put(Order.Column.user_define2,order.getUser_define2());
        values.put(Order.Column.user_define3,order.getUser_define3());
        values.put(Order.Column.user_define4,order.getUser_define4());
        values.put(Order.Column.user_define5,order.getUser_define5());
        values.put(Order.Column.return_order,order.getReturn_order());
        values.put(Order.Column.return_status,order.getReturn_status());
        values.put(Order.Column.delivery_date,order.getDelivery_date());
        values.put(Order.Column.truckNo,order.getTruckNo());
        values.put(Order.Column.sendtoserver_timestamp,order.getSendtoserver_timestamp());

        sqLiteDatabase.insert(TableOrder, null, values);

        sqLiteDatabase.close();
    }

    public void addOrdersTemp(Order order) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Order.Column.Oucode, order.getOucode());
        values.put(Order.Column.Trans_Year, order.getYear());
        values.put(Order.Column.Trans_Group, order.getGroup());
        values.put(Order.Column.TransNo, order.getTransNo());
        values.put(Order.Column.Transdate, order.getTransdate());
        values.put(Order.Column.rep_seq, order.getRep_seq());
        values.put(Order.Column.rep_code, order.getRep_code());
        values.put(Order.Column.rep_name, order.getRep_name());
        values.put(Order.Column.rep_nickname, order.getRep_nickname());
        values.put(Order.Column.address1, order.getAddress1());
        values.put(Order.Column.address2, order.getAddress2());
        values.put(Order.Column.tumbon, order.getTumbon());
        values.put(Order.Column.amphur, order.getAmphur());
        values.put(Order.Column.province, order.getProvince());
        values.put(Order.Column.postal, order.getPostal());
        values.put(Order.Column.rep_telno, order.getRep_telno());
        values.put(Order.Column.dsm_name, order.getDsm_name());
        values.put(Order.Column.dsm_telno, order.getDsm_telno());
        values.put(Order.Column.loc_code, order.getLoc_code());
        values.put(Order.Column.trans_campaign, order.getTrans_campaign());
        values.put(Order.Column.ord_campaign, order.getOrd_campaign());
        values.put(Order.Column.ord_type, order.getOrd_type());
        values.put(Order.Column.del_type, order.getDel_type());
        values.put(Order.Column.ord_flag_status, order.getOrd_flag_status());
        values.put(Order.Column.return_flag, order.getReturn_flag());
        values.put(Order.Column.unpack_items, order.getUnpack_items());
        values.put(Order.Column.order_flag_desc, order.getOrder_flag_desc());
        values.put(Order.Column.delivery_desc, order.getDelivery_desc());
        values.put(Order.Column.ordertype_desc, order.getOrdertype_desc());
        values.put(Order.Column.cont_desc, order.getCont_desc());
        values.put(Order.Column.Itemno, order.getItemno());
        values.put(Order.Column.delivery_status,order.getDelivery_status());
        values.put(Order.Column.isselect,order.getIsselect());
        values.put(Order.Column.cre_date,order.getCre_date());
        values.put(Order.Column.fullpathimage,order.getFullpathimage());

        sqLiteDatabase.insert(TableOrderTemp, null, values);

        sqLiteDatabase.close();
    }


    public Order getOrders(String id) {


        Cursor cursor = null;
        Order order = new Order();
        sqLiteDatabase = this.getReadableDatabase();

        /*Cursor cursor = sqLiteDatabase.query(TableOrder,
                null,
                Order.Column.Oucode + " = ? ",
                new String[] { id },
                null,
                null,
                null,
                null);*/

        try{

            cursor = sqLiteDatabase.query(TableOrder,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);


            if (cursor != null  && cursor.getCount()>0) {
                cursor.moveToFirst();
            }

            order = new Order();
            order.setId(cursor.getInt(0));
            order.setOucode(cursor.getString(1));
            order.setYear(cursor.getString(2));
            order.setGroup(cursor.getString(3));
            order.setTransNo(cursor.getString(4));
            order.setTransdate(cursor.getString(5));
            order.setRep_seq(cursor.getString(6));
            order.setRep_code(cursor.getString(7));
            order.setRep_name(cursor.getString(8));
            order.setRep_nickname(cursor.getString(9));
            order.setAddress1(cursor.getString(10));
            order.setAddress2(cursor.getString(11));
            order.setTumbon(cursor.getString(12));
            order.setAmphur(cursor.getString(13));
            order.setProvince(cursor.getString(14));
            order.setPostal(cursor.getString(15));
            order.setRep_telno(cursor.getString(16));
            order.setDsm_name(cursor.getString(17));
            order.setDsm_telno(cursor.getString(18));
            order.setLoc_code(cursor.getString(19));
            order.setTrans_campaign(cursor.getString(20));
            order.setOrd_campaign(cursor.getString(21));
            order.setOrd_type(cursor.getString(22));
            order.setDel_type(cursor.getString(23));
            order.setOrd_flag_status(cursor.getString(24));
            order.setReturn_flag(cursor.getString(25));
            order.setUnpack_items(cursor.getString(26));
            order.setOrder_flag_desc(cursor.getString(27));
            order.setDelivery_desc(cursor.getString(28));
            order.setOrdertype_desc(cursor.getString(29));
            order.setCont_desc(cursor.getString(30));
            order.setItemno(cursor.getInt(31));
            order.setDelivery_status(cursor.getString(32));

        }catch (Exception ex){

        }finally {
            if(cursor != null){
                cursor.close();
                sqLiteDatabase.close();
            }
        }



        return order;
    }

    public void addOrdersReturn(OrderReturn order) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrderReturn.Column.ou_code, order.getOu_code());
        values.put(OrderReturn.Column.return_no, order.getReturn_no());
        values.put(OrderReturn.Column.return_code, order.getReturn_code());
        values.put(OrderReturn.Column.return_type, order.getReturn_type());
        values.put(OrderReturn.Column.reftrans_no, order.getReftrans_no());
        values.put(OrderReturn.Column.reftrans_year, order.getReftrans_year());
        values.put(OrderReturn.Column.rep_code, order.getRep_code());
        values.put(OrderReturn.Column.rep_seq, order.getRep_seq());
        values.put(OrderReturn.Column.rep_name, order.getRep_name());
        values.put(OrderReturn.Column.return_seq, order.getReturn_seq());
        values.put(OrderReturn.Column.fs_code, order.getFs_code());
        values.put(OrderReturn.Column.fs_desc, order.getFs_desc());
        values.put(OrderReturn.Column.return_unit_real, order.getReturn_unit_real());
        values.put(OrderReturn.Column.return_unit, order.getReturn_unit());
        values.put(OrderReturn.Column.return_remark, order.getReturn_remark());
        values.put(OrderReturn.Column.return_status, order.getReturn_status());
        values.put(OrderReturn.Column.fullpathimage, order.getFullpathimage());
        values.put(OrderReturn.Column.track_no, order.getTrack_no());
        values.put(OrderReturn.Column.delivery_date, order.getDelivery_date());
        values.put(OrderReturn.Column.lat, order.getLat());
        values.put(OrderReturn.Column.lon, order.getLon());
        values.put(OrderReturn.Column.signature_timestamp, order.getSignature_timestamp());
        values.put(OrderReturn.Column.sendtoserver_timestamp, order.getSendtoserver_timestamp());
        values.put(OrderReturn.Column.return_order_status,order.getReturn_order_status());


        sqLiteDatabase.insert(TableOrderReturn, null, values);


        ContentValues cv = new ContentValues();
        cv.put(Order.Column.return_flag,"R"); //These Fields should be your String values of actual column names
//        cv.put(Order.Column.return_status,"N");
        sqLiteDatabase.update(TableOrder,cv,"rep_seq='" + order.getRep_seq().toString() + "'",null);

        sqLiteDatabase.close();


//        //ถ้ามี return ต้องเข้าไป update flag
//        if(isReturnFlag(sigReftrans_no)==true)
//        {
//            //update flag
//            updateReturnFlag(sigReftrans_no);
//        }





    }


//    public  boolean isReturnFlag(String sigTranNo)
//    {
//        if (sigTranNo == null || sigTranNo.isEmpty() || sigTranNo.equals("null")){return false;}
//        try{
//
//            //update return_flag
//            sqLiteDatabase = this.getReadableDatabase();
//
//            Cursor cursor = sqLiteDatabase.query(TableOrder,
//                    null,
//                    "TransNo='" + sigTranNo + "'",
//                    null,
//                    null,
//                    null,
//                    null,
//                    null);
//
//            if (cursor != null  && cursor.getCount()>0) {
//                return true;
//            }
//        }
//        catch (Exception e)
//        {
//
//        }
//        return false;
//    }


    public OrderReturn getOrdersReturn(String id) {

        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TableOrderReturn,
                null,
                null,
                null,
                null,
                null,
                null,
                null);


        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        OrderReturn order = new OrderReturn();
        order.setOu_code(cursor.getString(0));
        order.setReturn_no(cursor.getString(1));
        order.setReturn_code(cursor.getString(2));
        order.setReturn_type(cursor.getString(3));
        order.setReftrans_no(cursor.getString(4));
        order.setReftrans_year(cursor.getString(5));
        order.setRep_code(cursor.getString(6));
        order.setRep_seq(cursor.getString(7));
        order.setRep_name(cursor.getString(8));
        order.setReturn_seq(cursor.getString(9));
        order.setFs_code(cursor.getString(10));
        order.setFs_desc(cursor.getString(11));
        order.setReturn_unit(cursor.getString(12));
        order.setReturn_remark(cursor.getString(13));

        return order;
    }


    //เพิ่มเวันที่ 18-03-2018 15:54
    public ArrayList<OrderReturn> getOrdersReturnList(ArrayList<Order> mOrderCriteria) {

        //add data
        ArrayList<OrderReturn>  mOrderReturnlist = new ArrayList<OrderReturn>();
        if(mOrderCriteria==null){return null ;}
        if(mOrderCriteria.size()==0){return null ;}

        String sigGetData="";
        for(int i=0;i<mOrderCriteria.size();i++)
        {
            if(i==0)
            {
                sigGetData = "'" + mOrderCriteria.get(i).getRep_code() + "'";
            }
            else
            {
                sigGetData = sigGetData + ",'" + mOrderCriteria.get(i).getRep_code() + "'";
            }
        }

        if(sigGetData.isEmpty() || sigGetData==null || sigGetData.equals("null") || sigGetData.equals("")){return null;}

        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TableOrderReturn,
                null,
                "rep_code IN (" + sigGetData + ")",
                null,
                "return_no",
                null,
                null,
                null);


        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {
            OrderReturn mOrderReturn = new OrderReturn();

            mOrderReturn.setOu_code(cursor.getString(0));
            mOrderReturn.setReturn_no(cursor.getString(1));
            mOrderReturn.setReturn_code(cursor.getString(2));
            mOrderReturn.setReturn_type(cursor.getString(3));
            mOrderReturn.setReftrans_no(cursor.getString(4));
            mOrderReturn.setReftrans_year(cursor.getString(5));
            mOrderReturn.setRep_code(cursor.getString(6));
            mOrderReturn.setRep_seq(cursor.getString(7));
            mOrderReturn.setRep_name(cursor.getString(8));
            mOrderReturn.setReturn_seq(cursor.getString(9));
            mOrderReturn.setFs_code(cursor.getString(10));
            mOrderReturn.setFs_desc(cursor.getString(11));
            mOrderReturn.setReturn_unit_real(cursor.getString(12));
            mOrderReturn.setReturn_unit(cursor.getString(13));
            mOrderReturn.setReturn_remark(cursor.getString(14));
            mOrderReturn.setReturn_status(cursor.getString(15));
            mOrderReturnlist.add(mOrderReturn);

            cursor.moveToNext();
        }

        return mOrderReturnlist;
    }

    public ArrayList<OrderReturn> getOrdersReturnListByRepCode(Order mOrder) {

        //add data
        ArrayList<OrderReturn>  mOrderReturnlist = new ArrayList<OrderReturn>();
        if(mOrder==null){return null ;}


        String sigGetData="";
        sigGetData = "'" + mOrder.getRep_code()+ "'";

        if(sigGetData.isEmpty() || sigGetData==null || sigGetData.equals("null") || sigGetData.equals("")){return null;}
        sqLiteDatabase = this.getReadableDatabase();


        Cursor cursor = sqLiteDatabase.query(TableOrderReturn,
                null,
                "rep_code =" + sigGetData + " AND return_status IN ('1','2')",
                null,
                "return_no",
                null,
                null,
                null);


        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {
            OrderReturn mOrderReturn = new OrderReturn();

            mOrderReturn.setOu_code(cursor.getString(0));
            mOrderReturn.setReturn_no(cursor.getString(1));
            mOrderReturn.setReturn_code(cursor.getString(2));
            mOrderReturn.setReturn_type(cursor.getString(3));
            mOrderReturn.setReftrans_no(cursor.getString(4));
            mOrderReturn.setReftrans_year(cursor.getString(5));
            mOrderReturn.setRep_code(cursor.getString(6));
            mOrderReturn.setRep_seq(cursor.getString(7));
            mOrderReturn.setRep_name(cursor.getString(8));
            mOrderReturn.setReturn_seq(cursor.getString(9));
            mOrderReturn.setFs_code(cursor.getString(10));
            mOrderReturn.setFs_desc(cursor.getString(11));
            mOrderReturn.setReturn_unit_real(cursor.getString(12));
            mOrderReturn.setReturn_unit(cursor.getString(13));
            mOrderReturn.setReturn_remark(cursor.getString(14));
            mOrderReturn.setReturn_status(cursor.getString(15));
            mOrderReturn.setReason_code(cursor.getString(16));
            mOrderReturn.setReturn_note(cursor.getString(17));
            mOrderReturn.setFullpathimage(cursor.getString(18));
            mOrderReturn.setTrack_no(cursor.getString(19));
            mOrderReturn.setDelivery_date(cursor.getString(20));
            mOrderReturn.setLat(cursor.getString(21));
            mOrderReturn.setLon(cursor.getString(22));
            mOrderReturn.setSignature_timestamp(cursor.getString(23));
            mOrderReturn.setSendtoserver_timestamp(cursor.getString(24));
            mOrderReturnlist.add(mOrderReturn);

            cursor.moveToNext();
        }

        return mOrderReturnlist;
    }


    // return_status 0= ยังไม่รับคืน,1=รับคืน,2=ไม่รับคืน
    public ArrayList<OrderReturn> getOrdersReturnListSummary(String sigCriteria) {

        if(sigCriteria.isEmpty() || sigCriteria.equals("") || sigCriteria==null){ return null;}
        ArrayList<OrderReturn>  mOrderReturnlist = new ArrayList<OrderReturn>();

        String sigCriteriaSql="";
        switch(sigCriteria.toUpperCase().toString()){
            case "N":
                sigCriteriaSql="return_status='0'";
                break;
            case "Y":
                sigCriteriaSql="return_status='1'";
                break;
            case "C":
                sigCriteriaSql="return_status='2'";
                break;
            case "YC":
                sigCriteriaSql="return_status IN ('1','2')";
                break;
            case "ALL":
                sigCriteriaSql="return_status IN ('0','1','2')";
                break;
        }

        if(sigCriteriaSql.isEmpty() || sigCriteriaSql==null || sigCriteriaSql.equals("null") || sigCriteriaSql.equals("")){return null;}

        Cursor cursor=null;
        sqLiteDatabase = this.getReadableDatabase();

        cursor = sqLiteDatabase.rawQuery(" SELECT return_no,rep_code,rep_name,return_status, SUM(return_unit_real) AS return_unit_real,SUM(return_unit) AS return_unit,reftrans_no,fullpathimage,rep_seq FROM " + TableOrderReturn + " WHERE " + sigCriteriaSql + " GROUP BY return_no" ,null);
        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {
            OrderReturn mOrderReturn = new OrderReturn();

            mOrderReturn.setReturn_no(cursor.getString(0));
            mOrderReturn.setRep_code(cursor.getString(1));
            mOrderReturn.setRep_name(cursor.getString(2));

            if(cursor.getString(3).equals("")){
                mOrderReturn.setReturn_status("ยังไม่รับคืน");
            }
            else if(cursor.getString(3).equals("0"))
            {
                mOrderReturn.setReturn_status("ยังไม่รับคืน");
            }
            else if(cursor.getString(3).equals("1"))
            {
                mOrderReturn.setReturn_status("รับคืนได้");
            }
            else if(cursor.getString(3).equals("2"))
            {
                mOrderReturn.setReturn_status("รับคืนไม่ได้");
            }

            mOrderReturn.setReturn_unit_real(cursor.getString(4));
            mOrderReturn.setReturn_unit(cursor.getString(5));
            mOrderReturn.setReftrans_no(cursor.getString(6));
            mOrderReturn.setFullpathimage(cursor.getString(7));
            mOrderReturn.setRep_seq(cursor.getString(8));
            mOrderReturnlist.add(mOrderReturn);

            cursor.moveToNext();
        }

        return mOrderReturnlist;
    }


    public ArrayList<OrderReturn> getReturnSummary(String sigCriteria) {

        if(sigCriteria.isEmpty() || sigCriteria.equals("") || sigCriteria==null){ return null;}
        ArrayList<OrderReturn>  mOrderReturnlist = new ArrayList<OrderReturn>();

        String sigCriteriaSql="";
        switch(sigCriteria.toUpperCase().toString()){
            case "N":
                sigCriteriaSql="return_status='0'";
                break;
            case "Y":
                sigCriteriaSql="return_status='1'";
                break;
            case "C":
                sigCriteriaSql="return_status='2'";
                break;
            case "YC":
                sigCriteriaSql="return_status IN ('1','2')";
                break;
            case "ALL":
                sigCriteriaSql="return_status IN ('0','1','2')";
                break;
        }

        if(sigCriteriaSql.isEmpty() || sigCriteriaSql==null || sigCriteriaSql.equals("null") || sigCriteriaSql.equals("")){return null;}

        Cursor cursor=null;
        sqLiteDatabase = this.getReadableDatabase();

        cursor = sqLiteDatabase.rawQuery(" SELECT rep_code,rep_name,fs_code,fs_desc,return_status,SUM(return_unit_real) AS return_unit_real FROM " + TableOrderReturn + " WHERE " + sigCriteriaSql + " GROUP BY fs_code" ,null);
        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {
            OrderReturn mOrderReturn = new OrderReturn();

            mOrderReturn.setRep_code(cursor.getString(0));
            mOrderReturn.setRep_name(cursor.getString(1));
            mOrderReturn.setFs_code(cursor.getString(2));
            mOrderReturn.setFs_desc(cursor.getString(3));

            if(cursor.getString(4).equals("")){
                mOrderReturn.setReturn_status("ยังไม่รับคืน");
            }
            else if(cursor.getString(4).equals("0"))
            {
                mOrderReturn.setReturn_status("ยังไม่รับคืน");
            }
            else if(cursor.getString(4).equals("1"))
            {
                mOrderReturn.setReturn_status("รับคืนได้");
            }
            else if(cursor.getString(4).equals("2"))
            {
                mOrderReturn.setReturn_status("รับคืนไม่ได้");
            }

            mOrderReturn.setReturn_unit_real(cursor.getString(5));
            mOrderReturnlist.add(mOrderReturn);

            cursor.moveToNext();
        }

        return mOrderReturnlist;
    }



    public void addUnpack(Unpack order) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Unpack.Column.transno, order.getTransno());
        values.put(Unpack.Column.unpack_code, order.getUnpack_code());
        values.put(Unpack.Column.unpack_desc, order.getUnpack_desc());
        values.put(Unpack.Column.unpack_qty, order.getUnpack_qty());
        values.put(Unpack.Column.unpack_image, order.getUnpack_image());
        values.put(Unpack.Column.rep_name, order.getRep_name());

        sqLiteDatabase.insert(TableUnpack, null, values);

        sqLiteDatabase.close();
    }

    public Unpack getUnpack(String id) {

        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TableUnpack,
                null,
                null,
                null,
                null,
                null,
                null,
                null);


        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        Unpack order = new Unpack();
        order.setTransno(cursor.getString(0));
        order.setUnpack_code(cursor.getString(1));
        order.setUnpack_desc(cursor.getString(2));
        order.setUnpack_qty(cursor.getString(3));
        order.setUnpack_image(cursor.getString(4));

        return order;
    }


    //delivery_status N=ยังไม่ส่ง, Y=ส่งแล้ว, ALL=แสดงทั้งหมด
    public ArrayList<Order> getOrderWaitListToServer() {
        ArrayList<Order> orders = new ArrayList<Order>();

        sqLiteDatabase = this.getReadableDatabase();

        String sigCriteriaSql= Order.Column.delivery_status + "='S'";
        Cursor cursor = sqLiteDatabase.query(TableOrder,
                null,
                sigCriteriaSql,
                null,
                null,
                null,
                Order.Column.Itemno,
                null);


        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            Order order = new Order();
            order.setId(cursor.getInt(0));
            order.setOucode(cursor.getString(1));
            order.setYear(cursor.getString(2));
            order.setGroup(cursor.getString(3));
            order.setTransNo(cursor.getString(4));
            order.setTransdate(cursor.getString(5));
            order.setRep_seq(cursor.getString(6));
            order.setRep_code(cursor.getString(7));
            order.setRep_name(cursor.getString(8));
            order.setRep_nickname(cursor.getString(9));
            order.setAddress1(cursor.getString(10));
            order.setAddress2(cursor.getString(11));
            order.setTumbon(cursor.getString(12));
            order.setAmphur(cursor.getString(13));
            order.setProvince(cursor.getString(14));
            order.setPostal(cursor.getString(15));
            order.setRep_telno(cursor.getString(16));
            order.setDsm_name(cursor.getString(17));
            order.setDsm_telno(cursor.getString(18));
            order.setLoc_code(cursor.getString(19));
            order.setTrans_campaign(cursor.getString(20));
            order.setOrd_campaign(cursor.getString(21));
            order.setOrd_type(cursor.getString(22));
            order.setDel_type(cursor.getString(23));
            order.setOrd_flag_status(cursor.getString(24));
            order.setReturn_flag(cursor.getString(25));
            order.setUnpack_items(cursor.getString(26));
            order.setOrder_flag_desc(cursor.getString(27));
            order.setDelivery_desc(cursor.getString(28));
            order.setOrdertype_desc(cursor.getString(29));
            order.setCont_desc(cursor.getString(30));
            order.setItemno(cursor.getInt(31));
            order.setDelivery_status(cursor.getString(32));
            order.setIsselect(cursor.getString(33));
            order.setCre_date(cursor.getString(34));
            order.setFullpathimage(cursor.getString(35));
            order.setLat(cursor.getString(36));
            order.setLon(cursor.getString(37));
            order.setSignature_timestamp(cursor.getString(38));
            order.setReason_code(cursor.getString(39));
            order.setReason_note(cursor.getString(40));
            order.setSend_status(cursor.getString(41));
            order.setMobile_serial(cursor.getString(42));
            order.setMobile_emei(cursor.getString(43));
            order.setMobile_battery(cursor.getString(44));
            order.setUser_define1(cursor.getString(45));
            order.setUser_define2(cursor.getString(46));
            order.setUser_define3(cursor.getString(47));
            order.setUser_define4(cursor.getString(48));
            order.setUser_define5(cursor.getString(49));
            order.setReturn_order(cursor.getString(50));
            order.setReturn_status(cursor.getString(51));
            order.setDelivery_date(cursor.getString(52));
            order.setTruckNo(cursor.getString(53));
            order.setSendtoserver_timestamp(cursor.getString(54));
            orders.add(order);

            cursor.moveToNext();
        }


        return orders;
    }


//    delivery_status N=ยังไม่ส่ง, Y=ส่งแล้ว, ALL=แสดงทั้งหมด
    public ArrayList<Order> getOrderWaitList(String sigCriteria) {

        if(sigCriteria.isEmpty() || sigCriteria.equals("") || sigCriteria==null){ return null;}

        ArrayList<Order> orders = new ArrayList<Order>();

        sqLiteDatabase = this.getReadableDatabase();

        String sigCriteriaSql="";
        switch(sigCriteria.toUpperCase().toString()){
            case "N":
                sigCriteriaSql="delivery_status='N'";
                break;
            case "Y":
                sigCriteriaSql="delivery_status='Y'";
                break;
            case "W":
                sigCriteriaSql="delivery_status='W'";
                break;
            case "NY":
                sigCriteriaSql="delivery_status IN ('N','Y')";
                break;
            case "YN":
                sigCriteriaSql="delivery_status IN ('Y','N')";
                break;
            case "NW":
                sigCriteriaSql="delivery_status IN ('N','W')";
                break;
            case "WN":
                sigCriteriaSql="delivery_status IN ('W','N')";
                break;
            case "YW":
                sigCriteriaSql="delivery_status IN ('Y','W')";
                break;
            case "WY":
                sigCriteriaSql="delivery_status IN ('W','Y')";
                break;
            case "SWY":
                sigCriteriaSql="delivery_status IN ('S','W','Y')";
                break;
            case "SYW":
                sigCriteriaSql="delivery_status IN ('S','W','Y')";
                break;
            case "WSY":
                sigCriteriaSql="delivery_status IN ('S','W','Y')";
                break;
            case "YSW":
                sigCriteriaSql="delivery_status IN ('S','W','Y')";
                break;
            case "YS":
                sigCriteriaSql="delivery_status IN ('Y','S')";
                break;
            case "ALL":
                sigCriteriaSql="TransNo IS NOT NULL";
                break;
        }

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TableOrder + " WHERE " + sigCriteriaSql + " ORDER BY Itemno ASC" ,null);



//        Cursor cursor = sqLiteDatabase.query(TableOrder,
//                null,
//                sigCriteriaSql,
//                null,
//                null,
//                null,
//                Order.Column.Itemno,
//                null);


        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            Order order = new Order();
            order.setId(cursor.getInt(0));
            order.setOucode(cursor.getString(1));
            order.setYear(cursor.getString(2));
            order.setGroup(cursor.getString(3));
            order.setTransNo(cursor.getString(4));
            order.setTransdate(cursor.getString(5));
            order.setRep_seq(cursor.getString(6));
            order.setRep_code(cursor.getString(7));
            order.setRep_name(cursor.getString(8));
            order.setRep_nickname(cursor.getString(9));
            order.setAddress1(cursor.getString(10));
            order.setAddress2(cursor.getString(11));
            order.setTumbon(cursor.getString(12));
            order.setAmphur(cursor.getString(13));
            order.setProvince(cursor.getString(14));
            order.setPostal(cursor.getString(15));
            order.setRep_telno(cursor.getString(16));
            order.setDsm_name(cursor.getString(17));
            order.setDsm_telno(cursor.getString(18));
            order.setLoc_code(cursor.getString(19));
            order.setTrans_campaign(cursor.getString(20));
            order.setOrd_campaign(cursor.getString(21));
            order.setOrd_type(cursor.getString(22));
            order.setDel_type(cursor.getString(23));
            order.setOrd_flag_status(cursor.getString(24));
            order.setReturn_flag(cursor.getString(25));
            order.setUnpack_items(cursor.getString(26));
            order.setOrder_flag_desc(cursor.getString(27));
            order.setDelivery_desc(cursor.getString(28));
            order.setOrdertype_desc(cursor.getString(29));
            order.setCont_desc(cursor.getString(30));
            order.setItemno(cursor.getInt(31));
            order.setDelivery_status(cursor.getString(32));
            order.setIsselect(cursor.getString(33));
            order.setCre_date(cursor.getString(34));
            order.setFullpathimage(cursor.getString(35));
            order.setLat(cursor.getString(36));
            order.setLon(cursor.getString(37));
            order.setSignature_timestamp(cursor.getString(38));
            order.setReason_code(cursor.getString(39));
            order.setReason_note(cursor.getString(40));
            order.setSend_status(cursor.getString(41));
            order.setMobile_serial(cursor.getString(42));
            order.setMobile_emei(cursor.getString(43));
            order.setMobile_battery(cursor.getString(44));
            order.setUser_define1(cursor.getString(45));
            order.setUser_define2(cursor.getString(46));
            order.setUser_define3(cursor.getString(47));
            order.setUser_define4(cursor.getString(48));
            order.setUser_define5(cursor.getString(49));
            order.setReturn_order(cursor.getString(50));
            order.setReturn_status(cursor.getString(51));
            order.setDelivery_date(cursor.getString(52));
            order.setTruckNo(cursor.getString(53));
            order.setSendtoserver_timestamp(cursor.getString(54));
            order.setMsllat(cursor.getString(55));
            order.setMsllng(cursor.getString(56));
            orders.add(order);

            cursor.moveToNext();
        }



        return orders;
    }


    public ArrayList<OrdersChangeList> getOrderChangeList(String sigInvlist) {

        ArrayList<OrdersChangeList> mOrdersChangeLists = new ArrayList<OrdersChangeList>();


        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TableOrder,
                null,
                "TransNo IN (" + sigInvlist + ") ",
                null,
                null,
                null,
                null,
                null);

//
//        Cursor cursor = sqLiteDatabase.query(TableOrder,
//                null,
//                "TransNo IN (?) ",
//                new String[] { Invlist },
//                null,
//                null,
//                null,
//                null);
//        String selectQuery = "SELECT * FROM " + TableOrder + " WHERE TransNo IN (" + Invlist + ")";
//        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);


        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            OrdersChangeList mOrdersChangeList=new OrdersChangeList();
            mOrdersChangeList.setTransNo(cursor.getString(4));
            mOrdersChangeList.setRep_code(cursor.getString(7) + ' ' + cursor.getString(8));
            mOrdersChangeList.setQty("สินค้า: 0, นอกกล่อง: 0, รวมทั้งหมด: 0");
            mOrdersChangeList.setUnpack_items(cursor.getString(26));
            mOrdersChangeLists.add(mOrdersChangeList);

            cursor.moveToNext();
        }

        return mOrdersChangeLists;
    }

    public ArrayList<Unpack> getUnpackListForInvCustom(String sigInvlist) {

        ArrayList<Unpack> unpacks = new ArrayList<Unpack>();


        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TableUnpack,
                null,
                "transno IN (" + sigInvlist + ") ",
                null,
                null,
                null,
                null,
                null);


        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            Unpack order = new Unpack();
            order.setTransno(cursor.getString(0));
            order.setUnpack_code(cursor.getString(1));
            order.setUnpack_desc(cursor.getString(2));
            order.setUnpack_qty(cursor.getString(3));
            order.setUnpack_image(cursor.getString(4));
            unpacks.add(order);

            cursor.moveToNext();
        }
        return unpacks;
    }


    public ArrayList<Unpack> getUnpackList() {

        ArrayList<Unpack> unpacks = new ArrayList<Unpack>();


        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TableUnpack,
                null,
                null,
                null,
                null,
                null,
                null,
                null);


        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            Unpack order = new Unpack();
            order.setTransno(cursor.getString(0));
            order.setUnpack_code(cursor.getString(1));
            order.setUnpack_desc(cursor.getString(2));
            order.setUnpack_qty(cursor.getString(3));
            order.setUnpack_image(cursor.getString(4));
            order.setRep_name(cursor.getString(5));
            unpacks.add(order);

            cursor.moveToNext();
        }



        return unpacks;
    }

    public ArrayList<Unpack> getUnpackListItem(String fscode) {

        ArrayList<Unpack> unpacks = new ArrayList<Unpack>();
        Cursor cursor = null;


        sqLiteDatabase = this.getReadableDatabase();

        try{


            cursor = sqLiteDatabase.rawQuery("select transno,rep_name,sum(unpack_qty) from Unpacks where unpack_code = '" + fscode + "' group by transno,rep_name ",null);



            if (cursor != null  && cursor.getCount()>0) {
                cursor.moveToFirst();
            }

            while(!cursor.isAfterLast()) {

                Unpack order = new Unpack();
                order.setTransno(cursor.getString(0));
                order.setUnpack_code("");
                order.setUnpack_desc("");
                order.setUnpack_qty(cursor.getString(2));
                order.setUnpack_image("");
                order.setRep_name(cursor.getString(1));
                unpacks.add(order);

                cursor.moveToNext();
            }

        }catch (Exception ex){

        }finally {
            if(cursor != null){
                cursor.close();
                sqLiteDatabase.close();
            }
        }

        return unpacks;
    }

    public ArrayList<Unpack> getUnpackListWithMultiInv(ArrayList<Order> mOrder) {

        ArrayList<Unpack> unpacks = new ArrayList<Unpack>();
        if(mOrder==null){return null ;}
        if(mOrder.size()==0){return null ;}

        Cursor cursor = null;

        String sigGetData="";
        for(int i=0;i<mOrder.size();i++)
        {
//            sigGetData = mOrder.get(i).getTransNo();
            if(i==0)
            {
                sigGetData = "'" + mOrder.get(i).getTransNo() + "'";
            }
            else
            {
                sigGetData = sigGetData + ",'" + mOrder.get(i).getTransNo() + "'";
            }
        }

        if(sigGetData.isEmpty() || sigGetData==null || sigGetData.equals("null") || sigGetData.equals("")){return null;}


        sqLiteDatabase = this.getReadableDatabase();

        try{

            cursor = sqLiteDatabase.rawQuery(" SELECT unpack_code,unpack_desc, SUM(unpack_qty) AS unpack_qty,transno,unpack_image FROM " + TableUnpack + " WHERE transno IN (" + sigGetData + ") GROUP BY unpack_code,unpack_desc,transno ORDER BY unpack_code" ,null);
            if (cursor != null  && cursor.getCount()>0) {
                cursor.moveToFirst();
            }

            while(!cursor.isAfterLast()) {
                Unpack order = new Unpack();
                order.setUnpack_code(cursor.getString(0));
                order.setUnpack_desc(cursor.getString(1));
                order.setUnpack_qty(cursor.getString(2));
                order.setTransno(cursor.getString(3));
                order.setUnpack_image(cursor.getString(4));
                unpacks.add(order);
                cursor.moveToNext();
            }

        }catch (Exception ex){

        }finally {
            if(cursor != null){
                cursor.close();
                sqLiteDatabase.close();
            }
        }

        return unpacks;
    }

    public ArrayList<Unpack> getUnpackGroup() {

        ArrayList<Unpack> unpacks = new ArrayList<Unpack>();
        Cursor cursor = null;

        sqLiteDatabase = this.getReadableDatabase();
        try{
             cursor = sqLiteDatabase.rawQuery("select unpack_code,unpack_desc,Unpack_image,sum(unpack_qty) from Unpacks group by unpack_code,unpack_desc,Unpack_image order by unpack_code",null);


            if (cursor != null  && cursor.getCount()>0) {
                cursor.moveToFirst();
            }

            while(!cursor.isAfterLast()) {

                Unpack order = new Unpack();
                //order.setTransno(cursor.getString(0));
                order.setUnpack_code(cursor.getString(0));
                order.setUnpack_desc(cursor.getString(1));
                order.setUnpack_qty(cursor.getString(3));
                order.setUnpack_image(cursor.getString(2));
                unpacks.add(order);

                cursor.moveToNext();
            }

        }catch (Exception ex){

        }finally {
            if(cursor != null){
                cursor.close();
                sqLiteDatabase.close();
            }
        }

        return unpacks;
    }

    //เพิ่มเวันที่ 15-03-2018 14:17
    public ArrayList<OrderReturn> getOrderReturnCriteria(String sigReftrans_no) {

        ArrayList<OrderReturn> orderReturns = new ArrayList<OrderReturn>();
        Cursor cursor = null;

        sqLiteDatabase = this.getReadableDatabase();

        try{

            cursor = sqLiteDatabase.query(TableOrderReturn,
                    null,
                    "reftrans_no IN (" + sigReftrans_no + ") ",
                    null,
                    null,
                    null,
                    null,
                    null);


            if (cursor != null  && cursor.getCount()>0) {
                cursor.moveToFirst();
            }

            while(!cursor.isAfterLast()) {

                OrderReturn orderReturn = new OrderReturn();
                orderReturn.setOu_code(cursor.getString(0));
                orderReturn.setReturn_no(cursor.getString(1));
                orderReturn.setReturn_code(cursor.getString(2));
                orderReturn.setReturn_type(cursor.getString(3));
                orderReturn.setReftrans_no(cursor.getString(4));
                orderReturn.setReftrans_year(cursor.getString(5));
                orderReturn.setRep_code(cursor.getString(6));
                orderReturn.setRep_seq(cursor.getString(7));
                orderReturn.setRep_name(cursor.getString(8));
                orderReturn.setReturn_seq(cursor.getString(9));
                orderReturn.setFs_code(cursor.getString(10));
                orderReturn.setFs_desc(cursor.getString(11));
                orderReturn.setReturn_unit(cursor.getString(12));
                orderReturn.setReturn_remark(cursor.getString(13));
                orderReturn.setReturn_status(cursor.getString(14));
                orderReturns.add(orderReturn);

                cursor.moveToNext();
            }

        }catch (Exception ex){

        }finally {
            if(cursor != null){
                cursor.close();
                sqLiteDatabase.close();
            }
        }

        return orderReturns;
    }

    public boolean updateOrderDeliveryStatus(ArrayList<Order> mListOrder) {

        try{
            int intResult=0;
            for(int i=0;i<mListOrder.size();i++)
            {

                sqLiteDatabase = this.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(Order.Column.lat,mListOrder.get(i).getLat());
                cv.put(Order.Column.lon,mListOrder.get(i).getLon());
                cv.put(Order.Column.signature_timestamp,mListOrder.get(i).getSignature_timestamp());
                cv.put(Order.Column.reason_code,mListOrder.get(i).getReason_code());
                cv.put(Order.Column.reason_note,mListOrder.get(i).getReason_note());
                cv.put(Order.Column.send_status,mListOrder.get(i).getSend_status());
                cv.put(Order.Column.mobile_serial,mListOrder.get(i).getMobile_serial());
                cv.put(Order.Column.mobile_emei,mListOrder.get(i).getMobile_emei());
                cv.put(Order.Column.mobile_battery,mListOrder.get(i).getMobile_battery());
                cv.put(Order.Column.return_order,mListOrder.get(i).getReturn_order());
                cv.put(Order.Column.delivery_status,mListOrder.get(i).getDelivery_status());

                String sigTransNo = mListOrder.get(i).getTransNo();
                intResult = sqLiteDatabase.update(TableOrder,cv,Order.Column.TransNo + " ='" + sigTransNo + "'",null);

                if(sqLiteDatabase != null){
                    sqLiteDatabase.close();
                }

                if (intResult == 0 ){
                    return  false;
                }
            }


            if(intResult > 0){
                return  true;
            }
        }
        catch (Exception ex)
        {

        }
        finally
        {
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }

        return false;
    }


    public boolean updateOrderDeliveryStatusToServer(ArrayList<Order> mListOrder) {

        try{
            int intResult=0;
            for(int i=0;i<mListOrder.size();i++)
            {
                sqLiteDatabase = this.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(Order.Column.sendtoserver_timestamp,mListOrder.get(i).getSendtoserver_timestamp());
                cv.put(Order.Column.delivery_status,mListOrder.get(i).getDelivery_status());

                String sigTransNo = mListOrder.get(i).getTransNo();
                intResult = sqLiteDatabase.update(TableOrder,cv,Order.Column.TransNo + " ='" + sigTransNo + "'",null);

                if(sqLiteDatabase != null){
                    sqLiteDatabase.close();
                }

                if (intResult == 0 ){
                    return  false;
                }
            }


            if(intResult > 0){
                return  true;
            }
        }
        catch (Exception ex)
        {

        }
        finally
        {
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }

        return false;
    }




    public boolean updateOrderFullpathimage(ArrayList<Order> mOrders) {
        sqLiteDatabase = this.getWritableDatabase();
        try{
            int intResult = 0;

            for(int i=0;i<mOrders.size();i++) {
                ContentValues cv = new ContentValues();
                cv.put("fullpathimage", mOrders.get(i).getFullpathimage());

                intResult = sqLiteDatabase.update(TableOrder, cv,
                        "TransNo='" + mOrders.get(i).getTransNo() + "'",
                        null);

                if (intResult ==0 ){
                    return  false;
                }
            }

            if(intResult > 0){
                return  true;
            }
        }
        catch (Exception ex)
        {

        }
        finally
        {
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }

        return false;
    }

    public boolean updateOrderReturnFullpathimage(OrderReturn mOrderReturn) {
        sqLiteDatabase = this.getWritableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put("fullpathimage",mOrderReturn.getFullpathimage());

            int intResult = sqLiteDatabase.update("OrderReturns", cv,
                    "return_no='" + mOrderReturn.getReturn_no()
                            + "' AND rep_code='" + mOrderReturn.getRep_code() + "'",
                    null);

            if(intResult > 0){
                return  true;
            }
        }
        catch (Exception ex)
        {

        }
        finally
        {
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }

        return false;
    }



    public boolean updateOrderReturn(OrderReturn mOrderReturn) {
        sqLiteDatabase = this.getWritableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put("return_status",mOrderReturn.getReturn_status());

            int intResult = sqLiteDatabase.update("OrderReturns", cv,
                    "return_no='" + mOrderReturn.getReturn_no()
                            + "' AND rep_code='" + mOrderReturn.getRep_code() + "'",
                    null);

            if(intResult > 0){
                return  true;
            }
        }
        catch (Exception ex)
        {

        }
        finally
        {
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }

        return false;
    }

    public boolean updateOrderReturnSlip(OrderReturn mOrderReturn) {
        sqLiteDatabase = this.getWritableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put(OrderReturn.Column.reason_code,mOrderReturn.getReason_code());
            cv.put(OrderReturn.Column.return_status,mOrderReturn.getReturn_status());
            cv.put(OrderReturn.Column.return_note,mOrderReturn.getReturn_note());
            cv.put(OrderReturn.Column.return_unit_real,mOrderReturn.getReturn_unit_real());
            cv.put(OrderReturn.Column.fullpathimage,mOrderReturn.getFullpathimage());
            cv.put(OrderReturn.Column.lat,mOrderReturn.getLat());
            cv.put(OrderReturn.Column.lon,mOrderReturn.getLon());
            cv.put(OrderReturn.Column.signature_timestamp,mOrderReturn.getSignature_timestamp());


            int intResult = sqLiteDatabase.update("OrderReturns", cv,
                    "return_no='" + mOrderReturn.getReturn_no()
                            + "' AND rep_code='" + mOrderReturn.getRep_code() + "'",
                    null);

            if(intResult > 0){
                return  true;
            }
        }
        catch (Exception ex)
        {

        }
        finally
        {
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }

        return false;
    }

    public boolean updateOrderReturnDetails(OrderReturn mOrderReturn) {
        sqLiteDatabase = this.getWritableDatabase();
        try{

            ContentValues cv = new ContentValues();
            cv.put(OrderReturn.Column.reason_code,mOrderReturn.getReason_code());
            cv.put(OrderReturn.Column.return_status,mOrderReturn.getReturn_status());
            cv.put(OrderReturn.Column.return_note,mOrderReturn.getReturn_note());
            cv.put(OrderReturn.Column.return_unit_real,mOrderReturn.getReturn_unit_real());
            cv.put(OrderReturn.Column.fullpathimage,mOrderReturn.getFullpathimage());
            cv.put(OrderReturn.Column.lat,mOrderReturn.getLat());
            cv.put(OrderReturn.Column.lon,mOrderReturn.getLon());
            cv.put(OrderReturn.Column.signature_timestamp,mOrderReturn.getSignature_timestamp());

            int intResult = sqLiteDatabase.update("OrderReturns", cv,
                    "return_no='" + mOrderReturn.getReturn_no()
                            + "' AND rep_code='" + mOrderReturn.getRep_code() + "'",
                    null);

            if(intResult > 0){
                return  true;
            }

        }
        catch (Exception ex)
        {

        }
        finally
        {
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }

        return false;
    }


    public boolean updateOrderReturnDtl(OrderReturn mOrderReturn) {
        sqLiteDatabase = this.getWritableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put("return_unit_real",mOrderReturn.getReturn_unit_real());

            int intResult = sqLiteDatabase.update("OrderReturns", cv,
                    "return_no='" + mOrderReturn.getReturn_no()
                            + "' AND rep_code='" + mOrderReturn.getRep_code()
                            + "' AND fs_code='" + mOrderReturn.getFs_code() + "'",
                    null);

            if(intResult > 0){
                return  true;
            }
        }
        catch (Exception ex)
        {

        }
        finally
        {
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }

        return false;
    }

    public ArrayList<OrderReturn> getOrderReturn() {

        ArrayList<OrderReturn> orderReturns = new ArrayList<OrderReturn>();
        Cursor cursor = null;

        sqLiteDatabase = this.getReadableDatabase();

        try{
        cursor = sqLiteDatabase.query(TableOrderReturn,
                null,
                null,
                null,
                null,
                null,
                null,
                null);


        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            OrderReturn orderReturn = new OrderReturn();
            orderReturn.setOu_code(cursor.getString(0));
            orderReturn.setReturn_no(cursor.getString(1));
            orderReturn.setReturn_code(cursor.getString(2));
            orderReturn.setReturn_type(cursor.getString(3));
            orderReturn.setReftrans_no(cursor.getString(4));
            orderReturn.setReftrans_year(cursor.getString(5));
            orderReturn.setRep_code(cursor.getString(6));
            orderReturn.setRep_seq(cursor.getString(7));
            orderReturn.setRep_name(cursor.getString(8));
            orderReturn.setReturn_seq(cursor.getString(9));
            orderReturn.setFs_code(cursor.getString(10));
            orderReturn.setFs_desc(cursor.getString(11));
            orderReturn.setReturn_unit(cursor.getString(12));
            orderReturn.setReturn_remark(cursor.getString(13));
            orderReturn.setReturn_status(cursor.getString(14));
            orderReturns.add(orderReturn);

            cursor.moveToNext();
        }

        }catch (Exception ex){

        }finally {
            if(cursor != null){
                cursor.close();
                sqLiteDatabase.close();
            }
        }

        return orderReturns;
    }

    public ArrayList<OrderReturn> getOrderReturnDtl(String refno) {

        ArrayList<OrderReturn> orderReturns = new ArrayList<OrderReturn>();


        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TableOrderReturn,
                null,
                OrderReturn.Column.return_no + " = ? ",
                new String[] { refno },
                null,
                null,
                null,
                null);


        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            OrderReturn orderReturn = new OrderReturn();
            orderReturn.setOu_code(cursor.getString(0));
            orderReturn.setReturn_no(cursor.getString(1));
            orderReturn.setReturn_code(cursor.getString(2));
            orderReturn.setReturn_type(cursor.getString(3));
            orderReturn.setReftrans_no(cursor.getString(4));
            orderReturn.setReftrans_year(cursor.getString(5));
            orderReturn.setRep_code(cursor.getString(6));
            orderReturn.setRep_seq(cursor.getString(7));
            orderReturn.setRep_name(cursor.getString(8));
            orderReturn.setReturn_seq(cursor.getString(9));
            orderReturn.setFs_code(cursor.getString(10));
            orderReturn.setFs_desc(cursor.getString(11));
            orderReturn.setReturn_unit_real(cursor.getString(12));
            orderReturn.setReturn_unit(cursor.getString(13));
            orderReturn.setReturn_remark(cursor.getString(14));
            orderReturns.add(orderReturn);

            cursor.moveToNext();
        }



        return orderReturns;
    }

    public void addReason(Reason order) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Reason.Column.reason_code, order.getReason_code());
        values.put(Reason.Column.reason_desc, order.getReason_desc());
        values.put(Reason.Column.reason_type, order.getReason_type());

//        sqLiteDatabase.insert(TableUnpack, null, values);

        sqLiteDatabase.insert(TableReason, null, values);

        sqLiteDatabase.close();
    }

    public Reason getReason(String id) {

        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TableReason,
                null,
                null,
                null,
                null,
                null,
                null,
                null);


        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        Reason order = new Reason();
        order.setReason_code(cursor.getString(0));
        order.setReason_desc(cursor.getString(1));
        order.setReason_type(cursor.getString(2));

        return order;
    }


    public ArrayList<Reason> getReasonListForCondition(String sigReason_type) {
        ArrayList<Reason> Reasons = new ArrayList<Reason>();
        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT reason_code,reason_desc,reason_type FROM " + TableReason + " WHERE reason_type IN (" + sigReason_type + ")",null);

        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {
            Reason mReason = new Reason();
            mReason.setReason_code(cursor.getString(0));
            mReason.setReason_desc(cursor.getString(1));
            mReason.setReason_type(cursor.getString(2));
            mReason.setIsselect("0");
            Reasons.add(mReason);
            cursor.moveToNext();
        }
        return Reasons;
    }

    public boolean getCheckLoadOrder(String oucode,String year,String group,String Transno) {
        boolean result = false;
        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + TableOrder + " WHERE  Oucode ='" + oucode + "' and Trans_Year ='" + year + "' and Trans_Group ='" + group +"' and TransNo ='"+ Transno + "'",null);

        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
            result =  true;
        }else {
            result = false;
        }

        return result;
    }



    public String getInvOnOrder(String rep_seq)
    {
        try {
            sqLiteDatabase = this.getReadableDatabase();

            Cursor cursor = sqLiteDatabase.query(TableOrder,
                    null,
                    Order.Column.rep_seq + " = ? ",
                    new String[]{rep_seq},
                    null,
                    null,
                    null,
                    null);


            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
            }

            if (!cursor.isAfterLast()) {
                return cursor.getString(4);
            }

        }
        catch (Exception e)
        {
            Log.d(TAG,e.getMessage());

        }
        finally
        {


            sqLiteDatabase.close();

        }

        return "";
    }



    public  void updateItemno(String tranNo,String itemNo,String lat,String lng)
    {
        if (tranNo == null || tranNo.isEmpty() || tranNo.equals("null")){return;}
        try{
            sqLiteDatabase = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(Order.Column.Itemno,itemNo); //These Fields should be your String values of actual column names
            cv.put(Order.Column.msllat,lat);
            cv.put(Order.Column.msllng,lng);
            sqLiteDatabase.update(TableOrder,cv,"TransNo='" + tranNo.toString().trim() + "'",null);

        }
        catch (Exception e)
        {
            Log.d(TAG,e.getMessage());

        }finally {

                sqLiteDatabase.close();

        }
    }

    //ปรับปรุงสถานะการคืน '' หรือ 0=ยังไม่รับคืน,1=รับคืน,2=ไม่รับคืน
    public  void updateOrdersDeliveryStatus(ArrayList<Order> mListOrder , String sigStatus)
    {
        if (mListOrder == null || mListOrder.size()==0 || mListOrder.equals("null")){return;}

        try{
            for(int i=0; i < mListOrder.size();i++)
            {
                sqLiteDatabase = this.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(Order.Column.delivery_status,sigStatus);

                String sigTransNo = mListOrder.get(i).getTransNo();
                sqLiteDatabase.update(TableOrder,cv,Order.Column.TransNo + " ='" + sigTransNo + "'",null);

                if(sqLiteDatabase != null){
                    sqLiteDatabase.close();
                }
            }
        }
        catch (Exception e)
        {
            Log.d(TAG,e.getMessage());
        }
        finally
        {
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }
    }



    public  boolean IsCheckOrdersReturnBalance(ArrayList<Order> mListOrder)
    {
        if(mListOrder==null){return false;}
        if(mListOrder.size()==0){return false;}

        boolean IsCheckedStatus = false;

        try{
            String sigGetData="";
            for(int i=0;i<mListOrder.size();i++)
            {
                if(i==0)
                {
                    sigGetData = "'" + mListOrder.get(i).getRep_code() + "'";
                }
                else
                {
                    sigGetData = sigGetData + ",'" + mListOrder.get(i).getRep_code() + "'";
                }
            }

            if(sigGetData.isEmpty() || sigGetData==null || sigGetData.equals("null") || sigGetData.equals("")){return false;}

            sqLiteDatabase = this.getReadableDatabase();

            Cursor cursor = sqLiteDatabase.query(TableOrderReturn,
                    null,
                    "rep_code IN (" + sigGetData + ") AND return_status IN ('1','2')",
                    null,
                    "return_no",
                    null,
                    null,
                    null);


            if (cursor != null  && cursor.getCount()>0) {
                cursor.moveToFirst();
            }

            if(!cursor.isAfterLast()){
                //ถ้ามีการรับคืนแล้ว
                IsCheckedStatus = false;
            }
            else
            {
                //ไม่มีการรับคืน
                IsCheckedStatus = true;
            }
        }
        catch (Exception e)
        {
            Log.d(TAG,e.getMessage());
        }
        finally
        {
            //clean up
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }

        return IsCheckedStatus;
    }


    public boolean IsCheckOrdersShowMenuOnReturnList(String sigTransNo) {
        boolean IsCheckedStatus = false;
        try{
            sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TableOrder + " WHERE delivery_status ='W' AND return_flag='R' AND TransNo='" + sigTransNo + "' ORDER BY Itemno ASC" ,null);

            if (cursor != null  && cursor.getCount()>0) {
                cursor.moveToFirst();
            }


            if(!cursor.isAfterLast()){
                //ถ้ามีการรับคืนแล้ว
                IsCheckedStatus = true;
            }
        }
        catch (Exception e)
        {
            Log.d(TAG,e.getMessage());
        }
        finally
        {
            //clean up
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }

        return  IsCheckedStatus;
    }


    public  void update_DragandDrop_items(int xForm_ItemNo , int xTo_ItemNo)
    {
        try{

             ArrayList<Order> mOrders = new ArrayList<Order>();
             mOrders = getOrderWaitList("ALL");

             int getItemno = 0;

             for(int i=0;i < mOrders.size();i++)
             {

                 //ดึงข้อมูล itemno รายการล่าสุด
                 getItemno = mOrders.get(i).getItemno();


                 //ถ้า xForm_ItemNo มากกว่า xTo_ItemNo นั้นแสสดงว่า จาก ล่าง-->บน
                 if(xForm_ItemNo > xTo_ItemNo)
                 {
                     //ล่าง-->บน
                     if (getItemno == xForm_ItemNo)
                     {

                         //เรียงออร์เดอร์ ระหว่าง xForm_ItemNo --> xTo_ItemNo ใหม่อีกครั้ง
                         for(int j=0;j < mOrders.size();j++)
                         {
                             //ดึงข้อมูล itemno รายการล่าสุด
                             int iGET_ITEMNO = mOrders.get(j).getItemno();

                             //ถ้าดึงข้อมูล itemno รายการล่าสุด เท่ากับ xForm_ItemNo
                             if (iGET_ITEMNO == xForm_ItemNo)
                             {
                                //ปรับปรุงข้อมูลปกติ (itemno)
                                updateOrdersItemno(xTo_ItemNo,mOrders.get(j).getTransNo());
                             }
                             else
                             {
                                 //ถ้าดึงข้อมูล itemno รายการล่าสุด น้อยกว่า xForm_ItemNo
                                 if (iGET_ITEMNO < xForm_ItemNo )
                                 {
                                     //ถ้าดึงข้อมูล itemno รายการล่าสุด เท่ากับ xTo_ItemNo
                                     if (iGET_ITEMNO == xTo_ItemNo)
                                     {
                                         //ปรับปรุงข้อมูลปกติ (itemno)
                                         updateOrdersItemno(iGET_ITEMNO + 1,mOrders.get(j).getTransNo());
                                     }
                                     else  if (iGET_ITEMNO > xTo_ItemNo)
                                     {
                                         //ถ้าดึงข้อมูล itemno รายการล่าสุด มากกว่า toitemno
                                         //ปรับปรุงข้อมูลปกติ (itemno)
                                         updateOrdersItemno(iGET_ITEMNO + 1,mOrders.get(j).getTransNo());
                                     }
                                 }
                             }
                         }

                     }
                 }
                 else if(xForm_ItemNo < xTo_ItemNo)
                 {
                     //ถ้า xForm_ItemNo น้อยกว่า xTo_ItemNo นั้นแสสดงว่า จาก บน-->ล่าง
                     // บน-->ล่าง
                     if (getItemno == xTo_ItemNo)
                     {

                         //เรียงออร์เดอร์ ระหว่าง xForm_ItemNo --> xTo_ItemNo ใหม่อีกครั้ง
                         for(int j=0;j < mOrders.size();j++)
                         {

                             //ดึงข้อมูล itemno รายการล่าสุด
                             int iGET_ITEMNO = mOrders.get(j).getItemno();

                             //ถ้าดึงข้อมูล itemno รายการล่าสุด เท่ากับ xTo_ItemNo
                             if (iGET_ITEMNO == xTo_ItemNo)
                             {
                                 //ปรับปรุงข้อมูลปกติ (itemno)
                                 updateOrdersItemno(iGET_ITEMNO - 1,mOrders.get(j).getTransNo());
                             }
                             else
                             {
                                 //ถ้าดึงข้อมูล itemno รายการล่าสุด น้อยกว่า xTo_ItemNo
                                 if (iGET_ITEMNO < xTo_ItemNo )
                                 {
                                     //ถ้าดึงข้อมูบมาแล้ว getitemno มากกว่า toitemno
                                     if (iGET_ITEMNO == xForm_ItemNo)
                                     {
                                         //ปรับปรุงข้อมูลปกติ (itemno)
                                         updateOrdersItemno(xTo_ItemNo,mOrders.get(j).getTransNo());
                                     }
                                     else if (iGET_ITEMNO > xForm_ItemNo)
                                     {
                                         //ถ้าดึงข้อมูล itemno รายการล่าสุด มากกว่า xForm_ItemNo
                                         //ปรับปรุงข้อมูลปกติ (itemno)
                                         updateOrdersItemno(iGET_ITEMNO - 1,mOrders.get(j).getTransNo());
                                     }
                                 }

                             }

                         }

                     }
                 }
             }
        }
        catch (Exception e)
        {
            Log.d(TAG,e.getMessage());
        }
    }


    public  void update_DragandDrop_items_Multi(int xForm_ItemNo, int xTo_ItemNo, int xUserSelected, ArrayList<Order> mOrderSelectedList,ArrayList<Order> mOrderUpdateList)
    {
        try
        {
            if(mOrderSelectedList.size() == 0)
            {
                return;
            }

            if(mOrderUpdateList.size() == 0)
            {
                return;
            }

            //ตัวแปร

            ArrayList<Order> mOrders = new ArrayList<Order>();
            mOrders = getOrderWaitList("ALL");


            //บน-->ล่าง
            if(xUserSelected > xTo_ItemNo)
            {

                if(xUserSelected > xTo_ItemNo)
                {
                    //บน-->ล่าง
                    //ถ้ารายการที่เลือกมากกว่ารายการแรก
                    int iNextItemno = xForm_ItemNo;


                    //ปรับปรุงรายการที่ไม่เลือก
                    for(int j=0;j < mOrderUpdateList.size();j++)
                    {
                        //ดึงข้อมูล itemno รายการล่าสุด
                        int getItemnoUpdate = mOrderUpdateList.get(j).getItemno();


                        if ((getItemnoUpdate >= xForm_ItemNo) &&  (getItemnoUpdate <= xUserSelected))
                        {

                            if(mOrderUpdateList.get(j).getIsselect().equals("0"))
                            {
                                //ถ้าไม่ใช่รายการที่เลือกให้ +1 ไปเรื่อย
                                updateOrdersItemno(iNextItemno, mOrderUpdateList.get(j).getTransNo());
                                iNextItemno++;

                            }

                        }
                    }

                    //ปรับปรุงรายการที่เลือก
                    for (int j = 0; j < mOrderSelectedList.size(); j++) {
                        //ปรับปรุงข้อมูลปกติ (itemno)
                        updateOrdersItemno(iNextItemno, mOrderSelectedList.get(j).getTransNo());
                        iNextItemno++;
                    }
                }
                else
                {

                    //ถ้ารายการที่เลือกน้อยกว่ารายการแรก
                    int iNextItemno = xForm_ItemNo;


                    //ปรับปรุงรายการที่ไม่เลือก
                    for(int j=0;j < mOrderUpdateList.size();j++)
                    {
                        //ดึงข้อมูล itemno รายการล่าสุด
                        int getItemnoUpdate = mOrderUpdateList.get(j).getItemno();


                        if ((getItemnoUpdate >= xForm_ItemNo) &&  (getItemnoUpdate <= xTo_ItemNo))
                        {

                            if(mOrderUpdateList.get(j).getIsselect().equals("0"))
                            {

                                //ถ้าไม่ใช่รายการที่เลือกให้ +1 ไปเรื่อย
                                updateOrdersItemno(iNextItemno, mOrderUpdateList.get(j).getTransNo());
                                iNextItemno++;

                            }

                        }
                    }

                    //ปรับปรุงรายการที่เลือก
                    for (int j = 0; j < mOrderSelectedList.size(); j++) {
                        //ปรับปรุงข้อมูลปกติ (itemno)
                        updateOrdersItemno(iNextItemno, mOrderSelectedList.get(j).getTransNo());
                        iNextItemno++;
                    }

                }


            }
            else  if(xUserSelected < xTo_ItemNo)
            {

                //ล่าง-->บน
                if(xUserSelected > xForm_ItemNo)
                {
                    //ถ้ารายการที่เลือกมากกว่ารายการแรก
                    int iNextItemno = xForm_ItemNo;

                    //ปรับปรุงรายการที่เลือก
                    for (int j = 0; j < mOrderSelectedList.size(); j++) {
                        //ปรับปรุงข้อมูลปกติ (itemno)
                        updateOrdersItemno(iNextItemno, mOrderSelectedList.get(j).getTransNo());
                        iNextItemno++;
                    }

                    //ปรับปรุงรายการที่ไม่เลือก
                    for(int j=0;j < mOrderUpdateList.size();j++)
                    {
                        //ดึงข้อมูล itemno รายการล่าสุด
                        int getItemnoUpdate = mOrderUpdateList.get(j).getItemno();


                        if ((getItemnoUpdate >= xForm_ItemNo) &&  (getItemnoUpdate <= xTo_ItemNo))
                        {

                            if(mOrderUpdateList.get(j).getIsselect().equals("0"))
                            {
                                //ปรับปรุงข้อมูลปกติ (itemno)
                                updateOrdersItemno(iNextItemno, mOrderUpdateList.get(j).getTransNo());
                                iNextItemno++;
                            }

                        }
                    }

                }
                else
                {

                    //ถ้ารายการที่เลือกน้อยกว่ารายการแรก
                    int iNextItemno = xUserSelected;

                    //ปรับปรุงรายการที่เลือก
                    for (int j = 0; j < mOrderSelectedList.size(); j++) {
                        //ปรับปรุงข้อมูลปกติ (itemno)
                        iNextItemno++;
                        updateOrdersItemno(iNextItemno, mOrderSelectedList.get(j).getTransNo());
                    }

                    //ปรับปรุงรายการที่ไม่เลือก
                    for(int j=0;j < mOrderUpdateList.size();j++)
                    {
                        //ดึงข้อมูล itemno รายการล่าสุด
                        int getItemnoUpdate = mOrderUpdateList.get(j).getItemno();


                        if ((getItemnoUpdate > xUserSelected) &&  (getItemnoUpdate <= xTo_ItemNo))
                        {

                            if(mOrderUpdateList.get(j).getIsselect().equals("0"))
                            {
                                //ปรับปรุงข้อมูลปกติ (itemno)
                                iNextItemno++;
                                updateOrdersItemno(iNextItemno, mOrderUpdateList.get(j).getTransNo());
                            }

                        }
                    }
                }
            }

        }
        catch (Exception e)
        {
            Log.d(TAG,e.getMessage());
        }
    }




        public  void updateOrdersItemno(int sigItemno,String sigTransNo)
    {
        if (sigTransNo == null || sigTransNo.equals("null") ||  sigTransNo.equals("")){return;}

        try{

            sqLiteDatabase = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(Order.Column.Itemno,sigItemno);

            sqLiteDatabase.update(TableOrder,cv,Order.Column.TransNo + " ='" + sigTransNo + "'",null);

            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }
        catch (Exception e)
        {
            Log.d(TAG,e.getMessage());
        }
        finally
        {
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
            }
        }
    }


    public boolean getCheckSenddata() {
        boolean result = false;
        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + TableOrder + " WHERE  delivery_status ='S'",null);

        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
            result =  true;
        }else {
            result = false;
        }

        return result;
    }


    public  void UpdateOrderDelivery(String Transno,String status)
    {
        if (Transno == null || Transno.isEmpty() || Transno.equals("null")){return;}
        try{
            sqLiteDatabase = this.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(Order.Column.delivery_status,status); //These Fields should be your String values of actual column names
            sqLiteDatabase.update(TableOrder,cv,"TransNo='" + Transno.toString().trim() + "'",null);

        }
        catch (Exception e)
        {
            Log.d(TAG,e.getMessage());

        }finally {

            sqLiteDatabase.close();

        }
    }







}
