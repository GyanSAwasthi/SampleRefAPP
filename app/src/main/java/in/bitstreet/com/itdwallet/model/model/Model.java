package in.bitstreet.com.itdwallet.model.model;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class Model {


    public static final int HEADER_TEXT_TYPE=0;
    public static final int TEXT_CHILD_TYPE=1;
    public static final int AUDIO_TYPE=2;

    public int type;
    public int data;
    public String text;
    public String text1;
    public String text2;
    public String text3;



    public Model(int type, String text, int data)
    {
        this.type=type;
        this.data=data;
        this.text=text;

    }

    public Model(int type, String text1,String text2,String text3, int data)
    {
        this.type=type;
        this.data=data;
        this.text1=text1;
        this.text2=text2;
        this.text3=text3;

    }

}
