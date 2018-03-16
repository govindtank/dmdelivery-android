package com.bl.dmdelivery.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.OrderReturn;
import com.bl.dmdelivery.model.OrdersChangeList;
import com.bl.dmdelivery.model.Reason;
import com.bl.dmdelivery.model.Unpack;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by sd-m003 on 8/23/2017 AD.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DMDelivery";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase sqLiteDatabase;
    private final String TAG = getClass().getSimpleName();

    public static final String TableOrder = "Orders";
    public static final String TableUnpack = "Unpacks";
    public static final String TableOrderReturn = "OrderReturns";
    public static final String TableReason = "Reasons";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        String CREATE_ORDER_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY  AUTOINCREMENT,%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                        "%s TEXT)",
                TableOrder,
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
                Order.Column.Itemno
        );

        Log.i(TAG, CREATE_ORDER_TABLE);
        // create friend table
        db.execSQL(CREATE_ORDER_TABLE);

        //Order Return
        String CREATE_RETURN_TABLE = String.format("CREATE TABLE %s " +
                        "(%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT," +
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
                OrderReturn.Column.return_unit,
                OrderReturn.Column.return_remark,
                OrderReturn.Column.return_status
        );

        Log.i(TAG, CREATE_RETURN_TABLE);
        // create friend table
        db.execSQL(CREATE_RETURN_TABLE);

        //Unpack
        String CREATE_UNPACK_TABLE = String.format("CREATE TABLE %s " +
                        "(%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TableUnpack,
                Unpack.Column.transno,
                Unpack.Column.unpack_code,
                Unpack.Column.unpack_desc,
                Unpack.Column.unpack_qty,
                Unpack.Column.unpack_image
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

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_ORDER_TABLE = "DROP TABLE IF EXISTS " + TableOrder;
        db.execSQL(DROP_ORDER_TABLE);

        String DROP_ORDERRETURN_TABLE = "DROP TABLE IF EXISTS " + TableOrderReturn;
        db.execSQL(DROP_ORDERRETURN_TABLE);

        String DROP_UNPACK_TABLE = "DROP TABLE IF EXISTS " + TableUnpack;
        db.execSQL(DROP_UNPACK_TABLE);

        String DROP_REASON_TABLE = "DROP TABLE IF EXISTS " + TableReason;
        db.execSQL(DROP_REASON_TABLE);

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
                        "(%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TableUnpack,
                Unpack.Column.transno,
                Unpack.Column.unpack_code,
                Unpack.Column.unpack_desc,
                Unpack.Column.unpack_qty,
                Unpack.Column.unpack_image
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


        sqLiteDatabase.insert(TableOrder, null, values);

        sqLiteDatabase.close();
    }

    public Order getOrders(String id) {

        sqLiteDatabase = this.getReadableDatabase();

        /*Cursor cursor = sqLiteDatabase.query(TableOrder,
                null,
                Order.Column.Oucode + " = ? ",
                new String[] { id },
                null,
                null,
                null,
                null);*/

        Cursor cursor = sqLiteDatabase.query(TableOrder,
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


        return order;
    }

    public void addOrdersReturn(OrderReturn order) {
        String sigReftrans_no="";

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
        values.put(OrderReturn.Column.return_unit, order.getReturn_unit());
        values.put(OrderReturn.Column.return_remark, order.getReturn_remark());

        sigReftrans_no=order.getReftrans_no();

        sqLiteDatabase.insert(TableOrderReturn, null, values);

        sqLiteDatabase.close();


        //ถ้ามี return ต้องเข้าไป update flag
        if(isReturnFlag(sigReftrans_no)==true)
        {
            //update flag
            updateReturnFlag(sigReftrans_no);
        }
    }


    public  boolean isReturnFlag(String sigTranNo)
    {
        if (sigTranNo == null || sigTranNo.isEmpty() || sigTranNo.equals("null")){return false;}
        try{

            //update return_flag
            sqLiteDatabase = this.getReadableDatabase();

            Cursor cursor = sqLiteDatabase.query(TableOrder,
                    null,
                    "TransNo='" + sigTranNo + "'",
                    null,
                    null,
                    null,
                    null,
                    null);

            if (cursor != null  && cursor.getCount()>0) {
                return true;
            }
        }
        catch (Exception e)
        {

        }
        return false;
    }


    public  void updateReturnFlag(String sigTranNo)
    {
        if (sigTranNo == null || sigTranNo.isEmpty() || sigTranNo.equals("null")){return;}
        try{
            sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("return_flag", "'R'");

            sqLiteDatabase.update(TableOrder,
                    values,
                    "TransNo='" + sigTranNo + "'",
                    null);
            sqLiteDatabase.close();

        }
        catch (Exception e)
        {

        }
    }

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

    public void addUnpack(Unpack order) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Unpack.Column.transno, order.getTransno());
        values.put(Unpack.Column.unpack_code, order.getUnpack_code());
        values.put(Unpack.Column.unpack_desc, order.getUnpack_desc());
        values.put(Unpack.Column.unpack_qty, order.getUnpack_qty());
        values.put(Unpack.Column.unpack_image, order.getUnpack_image());

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

    public ArrayList<Order> getOrderWaitList() {

        ArrayList<Order> orders = new ArrayList<Order>();


        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TableOrder,
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
            unpacks.add(order);

            cursor.moveToNext();
        }



        return unpacks;
    }

    //เพิ่มเวันที่ 15-03-2018 14:17
    public ArrayList<OrderReturn> getOrderReturnCriteria(String sigReftrans_no) {

        ArrayList<OrderReturn> orderReturns = new ArrayList<OrderReturn>();

        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TableOrderReturn,
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
        return orderReturns;
    }

    public ArrayList<OrderReturn> getOrderReturn() {

        ArrayList<OrderReturn> orderReturns = new ArrayList<OrderReturn>();

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
            orderReturn.setReturn_unit(cursor.getString(12));
            orderReturn.setReturn_remark(cursor.getString(13));
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

        Cursor cursor = sqLiteDatabase.query(TableReason,
                null,
                "reason_type = " + sigReason_type,
                null,
                null,
                null,
                null,
                null);

        if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {
            Reason mReason = new Reason();
            mReason.setReason_code(cursor.getString(0));
            mReason.setReason_desc(cursor.getString(1));
            mReason.setReason_type(cursor.getString(2));
            Reasons.add(mReason);
            cursor.moveToNext();
        }
        return Reasons;
    }

}
