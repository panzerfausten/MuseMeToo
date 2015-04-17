package data;

/**
 * Created by panzer on 2/26/15.
 */
public class MuseData {
    public enum MuseDataType { EEG,ACC,MUSELEMENT};
    double timestamp;
    float value;
    float valueX;
    float valueY;
    float valueZ;
    float valueXX;

    public MuseDataType getmType() {
        return mType;
    }

    private MuseDataType mType;

    public MuseData(float value, double timestamp, MuseDataType etype) {
        this.value = value;
        this.timestamp = timestamp;
        if(etype == MuseDataType.ACC){
            try {
                throw new Exception("ACC is not a valid MuseDataType in this constructor");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.mType =etype;

    }
    public MuseData(float valueX, float valueY, float valueZ, float valueXX, double timestamp, MuseDataType mType){
        this.valueX = valueX;
        this.valueY = valueY;
        this.valueZ = valueZ;
        this.valueXX = valueXX;
        this.timestamp = timestamp;
        this.mType =mType;

    }
    public MuseData(float valueX, float valueY, float valueZ, double timestamp, MuseDataType mType){
        this.valueX = valueX;
        this.valueY = valueY;
        this.valueZ = valueZ;
        this.timestamp = timestamp;
        this.mType =mType;

    }
}
