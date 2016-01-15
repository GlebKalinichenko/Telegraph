package com.example.gleb.telegraph;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gleb on 29.12.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Telegraph.db";

    /** Table MailBoxes*/
    public static final String TABLE_MAIL_BOXES = "MailBoxes";
    public static final String ID_MAIL_BOX = "IdMailBox";
    public static final String EMAIL_ACCOUNT = "Email";
    public static final String PASSWORD_ACCOUNT = "Password";
    public static final String USE_PROTOCOL = "UseProtocol";

    /**Table MailSettings*/
    public static final String TABLE_MAIL_SETTINGS = "MailSettings";
    public static final String ID_MAIL_SETTING = "IdMailSetting";
    public static final String NAME_POST_SERVER = "NamePostServer";
    public static final String ADDRESS_IMAP = "AddressImap";
    public static final String PORT_IMAP = "PortImap";
    public static final String ADDRESS_POP3 = "AddressPop3";
    public static final String PORT_POP3 = "PortPop3";
    public static final String ADDRESS_SMTP = "AddressSmtp";
    public static final String PORT_SMTP = "PortSmtp";
    public static final String BOX_CODE = "BoxCode";

    /**Table Folders*/
    public static final String TABLE_FOLDERS = "Folders";
    public static final String ID_FOLDER = "IdFolder";
    public static final String NAME_FOLDER = "NameFolder";
    public static final String MAIL_BOX_CODE = "MailBoxCode";

    /**Table Mails*/
    public static final String TABLE_MAILS = "Mails";
    public static final String ID_MAIL = "IdMail";
    public static final String SENDER_USER_CODE = "SenderUserCode";
    public static final String NAME_SENDER = "NameSender";
    public static final String RECEIVER = "Receiver";
    public static final String SUBJECT = "Subject";
    public static final String CONTENT = "Content";
    public static final String FOLDER_CODE = "FolderCode";
    public static final String DATE = "Date";
    public static final String HAS_ATTACH = "HasAttach";
    public static final String STRAIGHT_INDEX = "StraightIndex";

    /**Table Attachs*/
    public static final String TABLE_ATTACHS = "Attachs";
    public static final String ID_ATTACH = "IdAttach";
    public static final String NAME_ATTACH = "NameAttach";
    public static final String NUM_POSITION = "NumPositionOfAttach";
    public static final String MAIL_CODE = "MailCode";

    /**Table Users*/
    public static final String TABLE_USERS = "Users";
    public static final String ID_USER = "IdUser";
    public static final String EMAIL_USER = "Email";

    /**Table User keys*/
    public static final String TABLE_USER_KEYS = "UserKeys";
    public static final String ID_USER_KEY = "IdUserKey";
    public static final String LINK_TO_USER_KEY_FOLDER = "LinkToFolder";
    public static final String USER_CODE = "UserCode";

    /**Table User signature*/
    public static final String TABLE_USER_SIGNATURES = "UserSignatures";
    public static final String ID_USER_SIGNATURE = "IdUserSignature";
    public static final String LINK_TO_USER_SIGNATURE_FOLDER = "LinkToFolder";

    /**Table My users*/
    public static final String TABLE_MY_USERS = "MyUsers";
    public static final String ID_MY_USER = "IdUser";
    public static final String EMAIL_MY_USER = "Email";
    public static final String BOX_CODE_MY_USER = "BoxCode";

    /**Table MailBox signatures*/
    public static final String TABLE_MAILBOX_SIGNATURES = "MailBoxSignatures";
    public static final String ID_MAILBOX_SIGNATURE = "IdSignature";
    public static final String LINK_TO_MAILBOX_SIGNATURE_FOLDER = "LinkToFolder";
    public static final String MY_SIGNATURE_USER_CODE = "UserCode";

    /**Table MailBox keys*/
    public static final String TABLE_MAILBOX_KEYS = "MailBoxKeys";
    public static final String ID_MAILBOX_KEY = "IdKey";
    public static final String LINK_TO_MAILBOX_KEY_FOLDER = "LinkToFolder";
    public static final String MY_KEY_USER_CODE = "UserCode";
    public static final String TYPE_KEY_CODE = "TypeKeyCode";

    /**Table Type keys*/
    public static final String TABLE_TYPE_KEYS = "TypeKeys";
    public static final String ID_TYPE_KEY = "IdTypeKey";
    public static final String TYPE_KEY = "TypeKey";

    public static final String CREATE_MAIL_BOXES = "CREATE TABLE " + TABLE_MAIL_BOXES + " (" +
            ID_MAIL_BOX + " INTEGER PRIMARY KEY AUTOINCREMENT," + EMAIL_ACCOUNT + " TEXT," + PASSWORD_ACCOUNT +
            " TEXT," + USE_PROTOCOL + " TEXT" +");";

    public static final String CREATE_MAIL_SETTINGS = "CREATE TABLE " + TABLE_MAIL_SETTINGS + " (" +
            ID_MAIL_SETTING + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME_POST_SERVER + " TEXT," +
            ADDRESS_IMAP + " TEXT," + PORT_IMAP + " TEXT," + ADDRESS_POP3 + " TEXT," +
            PORT_POP3 + " TEXT," + ADDRESS_SMTP + " TEXT," + PORT_SMTP + " TEXT, " + BOX_CODE + " INTEGER, "
            + "FOREIGN KEY (" + BOX_CODE + ") REFERENCES " + TABLE_MAIL_BOXES + "(" + ID_MAIL_BOX
            + ") ON DELETE CASCADE ON UPDATE CASCADE" + ");";

    public static final String CREATE_FOLDERS = "CREATE TABLE " + TABLE_FOLDERS + " (" +
            ID_FOLDER + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_FOLDER + " TEXT," + MAIL_BOX_CODE +
            " INTEGER, " + "FOREIGN KEY (" + MAIL_BOX_CODE + ") REFERENCES " + TABLE_MAIL_BOXES +
            " (" + ID_MAIL_BOX + ") ON DELETE CASCADE ON UPDATE CASCADE" + ");";

    public static final String CREATE_MAILS = "CREATE TABLE " + TABLE_MAILS + " (" +
            ID_MAIL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SENDER_USER_CODE + " INTEGER," + NAME_SENDER +
            " TEXT, " + RECEIVER + " TEXT, " + SUBJECT + " TEXT, " + CONTENT + " TEXT, "
            + FOLDER_CODE + " INTEGER, " + DATE + " TEXT, " +  HAS_ATTACH + " INTEGER, " + STRAIGHT_INDEX + " INTEGER, " + " FOREIGN KEY (" + FOLDER_CODE
            + ") REFERENCES " + TABLE_FOLDERS + " (" + ID_FOLDER + ") ON DELETE CASCADE ON UPDATE CASCADE, "
            + "FOREIGN KEY (" + SENDER_USER_CODE + ") REFERENCES " + TABLE_USERS + " (" + ID_USER
            + ") ON DELETE CASCADE ON UPDATE CASCADE" + ");";

    public static final String CREATE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
            ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMAIL_USER + " TEXT" + ");";

    public static final String CREATE_ATTACHS = "CREATE TABLE " + TABLE_ATTACHS + " (" +
            ID_ATTACH + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_ATTACH + " TEXT, " + NUM_POSITION +
            " INTEGER, " + MAIL_CODE + " INTEGER, " + "FOREIGN KEY (" + MAIL_CODE +
            ") REFERENCES " + TABLE_MAILS + " (" + ID_MAIL + ") ON DELETE CASCADE ON UPDATE CASCADE" + ");";

    public static final String CREATE_USER_KEYS = "CREATE TABLE " + TABLE_USER_KEYS + " (" +
            ID_USER_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LINK_TO_USER_KEY_FOLDER + " TEXT, " + USER_CODE +
            " INTEGER, " + "FOREIGN KEY (" + USER_CODE + ") REFERENCES " + TABLE_USERS
            + " (" + ID_USER + ") ON DELETE CASCADE ON UPDATE CASCADE" + ");";

    public static final String CREATE_USERS_SIGNATURES = "CREATE TABLE " + TABLE_USER_SIGNATURES + " (" +
            ID_USER_SIGNATURE + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LINK_TO_USER_SIGNATURE_FOLDER + " TEXT,"
            + USER_CODE + " INTEGER, " + "FOREIGN KEY (" + USER_CODE + ") REFERENCES " + TABLE_USERS
            + " (" + ID_USER + ") ON DELETE CASCADE ON UPDATE CASCADE" + ");";

    public static final String CREATE_MY_USERS = "CREATE TABLE " + TABLE_MY_USERS + " (" +
            ID_MY_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMAIL_MY_USER + " TEXT, " + BOX_CODE_MY_USER +
            " INTEGER, " + "FOREIGN KEY (" + BOX_CODE_MY_USER  + ") REFERENCES " + TABLE_MAIL_BOXES +
            " (" + ID_MAIL_BOX  + ") ON DELETE CASCADE ON UPDATE CASCADE" +");";

    public static final String CREATE_MAILBOX_SIGNATURES = "CREATE TABLE " + TABLE_MAILBOX_SIGNATURES + " (" +
            ID_MAILBOX_SIGNATURE + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LINK_TO_MAILBOX_SIGNATURE_FOLDER
            + " TEXT," + MY_SIGNATURE_USER_CODE + " INTEGER, " + "FOREIGN KEY ("
            + MY_SIGNATURE_USER_CODE + ") REFERENCES " + TABLE_MY_USERS
            + " (" + ID_MY_USER + ") ON DELETE CASCADE ON UPDATE CASCADE" + ");";

    public static final String CREATE_TYPE_KEYS = "CREATE TABLE " + TABLE_TYPE_KEYS + " (" +
            ID_TYPE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +TYPE_KEY + " TEXT" + ");";

    public static final String CREATE_MAILBOX_KEYS = "CREATE TABLE " + TABLE_MAILBOX_KEYS + " (" +
            ID_MAILBOX_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LINK_TO_MAILBOX_KEY_FOLDER + " TEXT, "
            + MY_KEY_USER_CODE + " INTEGER, " + TYPE_KEY_CODE + " INTEGER, "
            + "FOREIGN KEY (" + MY_KEY_USER_CODE + ") REFERENCES " + TABLE_MY_USERS
            + " (" + ID_MY_USER + ") ON DELETE CASCADE ON UPDATE CASCADE,"
            + "FOREIGN KEY (" + TYPE_KEY_CODE + ") REFERENCES " + TABLE_TYPE_KEYS
            + " (" + ID_TYPE_KEY + ") ON DELETE CASCADE ON UPDATE CASCADE" + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL(CREATE_MAIL_BOXES);
        db.execSQL(CREATE_MAIL_SETTINGS);
        db.execSQL(CREATE_FOLDERS);
        db.execSQL(CREATE_MAILS);
        db.execSQL(CREATE_USERS);
        db.execSQL(CREATE_ATTACHS);
        db.execSQL(CREATE_USER_KEYS);
        db.execSQL(CREATE_USERS_SIGNATURES);
        db.execSQL(CREATE_MY_USERS);
        db.execSQL(CREATE_MAILBOX_KEYS);
        db.execSQL(CREATE_MAILBOX_SIGNATURES);
        db.execSQL(CREATE_TYPE_KEYS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
