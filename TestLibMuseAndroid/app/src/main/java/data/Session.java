package data;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by panzer on 2/26/15.
 */
public class Session {
    //TODO: Use a correct Date data
    private long startTime;
    private long endTime;
    private String subjectName;



    private List<MuseData> ELEMENTS_LIST = new ArrayList<MuseData>();
    private List<MuseData> EEG_LIST = new ArrayList<MuseData>();

    private List<MuseData> ACC_LIST = new ArrayList<MuseData>();


    String SESSION_PATH;
    File SESSIONS_FOLDER;

    File FILE_ELEMENTS;
    File FILE_EGG;

    File FILE_ACC;
    private File FILE_SESSION;


    public Session(long startTime, String subjectName) {
        this.startTime = startTime;
        this.subjectName = subjectName;
    }


    public void startSession() throws IOException {
        SESSION_PATH = Environment.getExternalStorageDirectory()+"/sessions/"+subjectName+"/"+doubleToFullString(startTime)+"/";
         SESSIONS_FOLDER = new File(SESSION_PATH);
        if (!SESSIONS_FOLDER.exists()){
            SESSIONS_FOLDER.mkdirs();
        }
        FILE_EGG = new File(SESSION_PATH +subjectName+"_MUSE_EEG_"+startTime+".csv");
        FILE_ACC = new File(SESSION_PATH +subjectName+"_MUSE_ACC_"+startTime+".csv");
        FILE_ELEMENTS = new File(SESSION_PATH +subjectName+"_MUSE_ELEMENTS_"+startTime+".csv");
        FILE_SESSION = new File(SESSION_PATH +subjectName+"_MUSE_SESSION_"+startTime+".csv");


       //save files
        FILE_ELEMENTS.createNewFile();
        FILE_ACC.createNewFile();
        FILE_EGG.createNewFile();

        FILE_SESSION.createNewFile();
    }

    public long getEndTime() {
        return endTime;
    }
        private static int MAX_MUSE_ELEMENTS_LIST_SIZE = 75;
    private static int MAX_EEG_LIST_SIZE = 50;
    private static int MAX_ACC_LIST_SIZE = 100;


    public void addMuseData(MuseData museData) throws IOException {
        MuseData.MuseDataType e_type = museData.getmType();
        switch(e_type){
            case ACC:
            if(ACC_LIST.size() == MAX_ACC_LIST_SIZE) {
                //save to disk,
                saveACC();
                ACC_LIST.clear();
                ACC_LIST.add(museData);
            }else{
                ACC_LIST.add(museData);

            }
            break;
            case EEG:
                if(EEG_LIST.size() == MAX_EEG_LIST_SIZE) {
                    //save to disk,
                    saveEGG();
                    EEG_LIST.clear();
                    EEG_LIST.add(museData);
                }else{
                    EEG_LIST.add(museData);

                }
            break;
            case MUSELEMENT:
                if(ELEMENTS_LIST.size() == MAX_MUSE_ELEMENTS_LIST_SIZE) {
                    //save to disk,
                    saveElements();
                    ELEMENTS_LIST.clear();
                    ELEMENTS_LIST.add(museData);
                }else{
                    ELEMENTS_LIST.add(museData);

                }
                break;
        }
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }


    private void saveACC() throws IOException {
        if (FILE_ACC.canWrite()) {
            FileWriter fw = new FileWriter(FILE_ACC,true);
            for (MuseData e : ACC_LIST) {
                fw.write(doubleToFullString(e.timestamp) + "," + String.valueOf(e.valueX)
                        + "," + String.valueOf(e.valueY) + "," + String.valueOf(e.valueZ)
                        +"\n");
            }
            fw.close();
        }
    }
    private void saveEGG() throws IOException {
        if (FILE_EGG.canWrite()) {
            FileWriter fw = new FileWriter(FILE_EGG,true);
            for (MuseData e : EEG_LIST) {
                fw.write(doubleToFullString(e.timestamp) + "," + String.valueOf(e.valueX)
                        + "," + String.valueOf(e.valueY) + "," + String.valueOf(e.valueZ)
                        + "," + String.valueOf(e.valueXX) + "\n" );
            }
            fw.close();
        }
    }
    private void saveElements() throws IOException {
        if (FILE_ELEMENTS.canWrite()) {
            FileWriter fw = new FileWriter(FILE_ELEMENTS,true);
            for (MuseData e : ELEMENTS_LIST) {
                fw.write(doubleToFullString(e.timestamp) + "," + String.valueOf(e.valueX)
                        + "," + String.valueOf(e.valueY) + "," + String.valueOf(e.valueZ)
                        + "," + String.valueOf(e.valueXX) + "\n" );
            }
            fw.close();
        }
    }
    /*
        1) Generate all the files (start Session)
        2) Start storing
        3) Save data periodically
        4) When finishing session,

     */
    public void saveSession() throws IOException {
        //save  the rest
        saveACC();
        saveEGG();
        saveElements();
        endTime = System.currentTimeMillis();
        saveSessionData();
    }

    private void saveSessionData() throws IOException {

        if (FILE_SESSION.canWrite()) {
            FileWriter fw = new FileWriter(FILE_SESSION,true);
            fw.write("SUBJECT,"+subjectName +"\n");
            fw.write("START_TIME,"+doubleToFullString(startTime) +"\n");
            fw.write("END_TIME,"+doubleToFullString(endTime) +"\n");
            long duration = endTime -startTime;
            fw.write("DURATION,"+doubleToFullString(duration) +"\n");


            fw.close();
        }
    }
    private String doubleToFullString(double value) {
        NumberFormat f = NumberFormat.getInstance();
        f.setGroupingUsed(false);
        String refinedNumber = f.format(value);
        return refinedNumber;
    }
}
