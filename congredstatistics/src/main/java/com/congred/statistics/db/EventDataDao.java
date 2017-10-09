package com.congred.statistics.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.congred.statistics.bean.EventData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EVENT_DATA".
*/
public class EventDataDao extends AbstractDao<EventData, Long> {

    public static final String TABLENAME = "EVENT_DATA";

    /**
     * Properties of entity EventData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Deviceid = new Property(1, String.class, "deviceid", false, "DEVICEID");
        public final static Property Event = new Property(2, String.class, "event", false, "EVENT");
        public final static Property Label = new Property(3, String.class, "label", false, "LABEL");
        public final static Property Clientdate = new Property(4, String.class, "clientdate", false, "CLIENTDATE");
        public final static Property Productkey = new Property(5, String.class, "productkey", false, "PRODUCTKEY");
        public final static Property Num = new Property(6, int.class, "num", false, "NUM");
        public final static Property Version = new Property(7, String.class, "version", false, "VERSION");
        public final static Property Useridentifier = new Property(8, String.class, "useridentifier", false, "USERIDENTIFIER");
        public final static Property Session_id = new Property(9, String.class, "session_id", false, "SESSION_ID");
        public final static Property Lib_version = new Property(10, String.class, "lib_version", false, "LIB_VERSION");
        public final static Property Insertdate = new Property(11, String.class, "insertdate", false, "INSERTDATE");
    }


    public EventDataDao(DaoConfig config) {
        super(config);
    }
    
    public EventDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EVENT_DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"DEVICEID\" TEXT," + // 1: deviceid
                "\"EVENT\" TEXT," + // 2: event
                "\"LABEL\" TEXT," + // 3: label
                "\"CLIENTDATE\" TEXT," + // 4: clientdate
                "\"PRODUCTKEY\" TEXT," + // 5: productkey
                "\"NUM\" INTEGER NOT NULL ," + // 6: num
                "\"VERSION\" TEXT," + // 7: version
                "\"USERIDENTIFIER\" TEXT," + // 8: useridentifier
                "\"SESSION_ID\" TEXT," + // 9: session_id
                "\"LIB_VERSION\" TEXT," + // 10: lib_version
                "\"INSERTDATE\" TEXT);"); // 11: insertdate
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EVENT_DATA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, EventData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String deviceid = entity.getDeviceid();
        if (deviceid != null) {
            stmt.bindString(2, deviceid);
        }
 
        String event = entity.getEvent();
        if (event != null) {
            stmt.bindString(3, event);
        }
 
        String label = entity.getLabel();
        if (label != null) {
            stmt.bindString(4, label);
        }
 
        String clientdate = entity.getClientdate();
        if (clientdate != null) {
            stmt.bindString(5, clientdate);
        }
 
        String productkey = entity.getProductkey();
        if (productkey != null) {
            stmt.bindString(6, productkey);
        }
        stmt.bindLong(7, entity.getNum());
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(8, version);
        }
 
        String useridentifier = entity.getUseridentifier();
        if (useridentifier != null) {
            stmt.bindString(9, useridentifier);
        }
 
        String session_id = entity.getSession_id();
        if (session_id != null) {
            stmt.bindString(10, session_id);
        }
 
        String lib_version = entity.getLib_version();
        if (lib_version != null) {
            stmt.bindString(11, lib_version);
        }
 
        String insertdate = entity.getInsertdate();
        if (insertdate != null) {
            stmt.bindString(12, insertdate);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, EventData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String deviceid = entity.getDeviceid();
        if (deviceid != null) {
            stmt.bindString(2, deviceid);
        }
 
        String event = entity.getEvent();
        if (event != null) {
            stmt.bindString(3, event);
        }
 
        String label = entity.getLabel();
        if (label != null) {
            stmt.bindString(4, label);
        }
 
        String clientdate = entity.getClientdate();
        if (clientdate != null) {
            stmt.bindString(5, clientdate);
        }
 
        String productkey = entity.getProductkey();
        if (productkey != null) {
            stmt.bindString(6, productkey);
        }
        stmt.bindLong(7, entity.getNum());
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(8, version);
        }
 
        String useridentifier = entity.getUseridentifier();
        if (useridentifier != null) {
            stmt.bindString(9, useridentifier);
        }
 
        String session_id = entity.getSession_id();
        if (session_id != null) {
            stmt.bindString(10, session_id);
        }
 
        String lib_version = entity.getLib_version();
        if (lib_version != null) {
            stmt.bindString(11, lib_version);
        }
 
        String insertdate = entity.getInsertdate();
        if (insertdate != null) {
            stmt.bindString(12, insertdate);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public EventData readEntity(Cursor cursor, int offset) {
        EventData entity = new EventData( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // deviceid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // event
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // label
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // clientdate
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // productkey
            cursor.getInt(offset + 6), // num
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // version
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // useridentifier
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // session_id
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // lib_version
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // insertdate
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, EventData entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDeviceid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setEvent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLabel(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setClientdate(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setProductkey(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setNum(cursor.getInt(offset + 6));
        entity.setVersion(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUseridentifier(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSession_id(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setLib_version(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setInsertdate(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(EventData entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(EventData entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(EventData entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}