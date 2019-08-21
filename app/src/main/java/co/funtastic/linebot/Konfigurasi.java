package co.funtastic.linebot;

public class Konfigurasi {
    public static final String URL_ADD =
            "https://asengsaragih.000webhostapp.com/LineBotAndroid/insertData.php";
    public static final String URL_GET_ALL =
            "https://asengsaragih.000webhostapp.com/LineBotAndroid/readAll.php";
    public static final String URL_GET_EMP =
            "https://asengsaragih.000webhostapp.com/LineBotAndroid/readOnce.php?id=";
    public static final String URL_UPDATE_EMP =
            "https://asengsaragih.000webhostapp.com/LineBotAndroid/updateData.php";
    public static final String URL_DELETE_EMP =
            "https://asengsaragih.000webhostapp.com/LineBotAndroid/deleteData.php?id=";
    public static final String URL_LOGIN_EMP =
            "https://asengsaragih.000webhostapp.com/LineBotAndroid/login.php?password=";

    //Dibawah ini merupakan Kunci yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    public static final String KEY_JADWAL_ID = "id";
    public static final String KEY_JADWAL_TANGGAL = "tanggal";
    public static final String KEY_JADWAL_KETERANGAN = "keterangan"; //desg itu variabel untuk posisi

    //JSON Tags
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_ID = "id";
    public static final String TAG_TANGGAL = "tanggal";
    public static final String TAG_KETERANGAN = "keterangan";

    //ID karyawan
    //emp itu singkatan dari Employee
    public static final String JADWAL_ID = "jadwal_id";
}
