public class Patient implements Comparable<Patient> {
    private String name;
    private int urgency;
    private int time_in;

    /***
     * constructor: new Patient
     * @param name the name of the Patient
     * @param urgency the Patient's initial urgency level
     * @param time_in the time the Patient arrives
     ***/
    public Patient(String name, int urgency, int time_in) {
	this.name = name;
	this.urgency = urgency;
        this.time_in = time_in;
    }

    /***
     *@return the Patient's name
     ***/
    public String name() {
	return this.name;
    }

    /***
     *@return the Patient's urgency level
     ***/
    public int urgency() {
	return this.urgency;
    }

    /***
     *increases the Patient's urgency level by 1
     ***/
    public void incUrgency() {
	this.urgency++;
    }

    /***
     *@return the time the Patient arrived
     ***/
    public int time_in() {
	return this.time_in;
    }

    /***
     *@param other another Patient to compare this one to
     *@return an int value determined by comparing this Patient
     *to <other> by comparing the urgency levels and arrival
     *times if the urgency levels are the same; 
     *a negative value indicates that <other> should get
     *higher precedence either because they have a higher
     *urgency level or (if those are the same) because they
     *arrived first; a positive value indicates that this Patient
     *should get higher precedence
     ***/ 
    public int compareTo(Patient other) {
	long diff = this.urgency - other.urgency();
	if(diff == 0) {
	    diff = other.time_in()-this.time_in;
	}
	if(diff < 0)
	    return -1;
	return 1;
    }

    /***
     *@return a String representation of the Patient
     *that includes name, urgency level, and time in
     ***/
    public String toString() {
	return name + ", " + urgency + ", " + time_in;
    }
}