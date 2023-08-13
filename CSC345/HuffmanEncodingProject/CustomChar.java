public class CustomChar {

    public char getC() {
        return c;
    }
 
    public void setC(char c) {
        this.c = c;
    }
 
    public int getFreq() {
        return freq;
    }
 
    public void setFreq(int freq) {
        this.freq = freq;
    }
 
    public String getEncoding() {
        return encoding;
    }
 
    public String setEncoding(String encoding) {
        return this.encoding = encoding;
    }
 
    private char c;
    private int freq;
 
    private String encoding = "";
 
    public CustomChar(char c, int freq, String encoding) {
        this.c = c;
        this.freq = freq;
        this.encoding = encoding;
    }
 
    public int compareTo(CustomChar other){
        return(this.freq - other.freq);
    }
 
    public String toString(){
        return("Char: " + this.c + " Freq: " + this.freq + " Encoding: " + this.encoding);
    }
 
 }
 
 