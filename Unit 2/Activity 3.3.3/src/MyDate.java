public class MyDate {
    private int day;
    private int month;
    private int year;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if(day >0 && day<=31) {
            this.day = day;
        }else{
            System.out.println("Enter a valid day");
            throw new IllegalArgumentException();
        }
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if(month >0 && month<=12) {
            this.month = month;
        }else{
            System.out.println("Enter a valid month");
            throw new IllegalArgumentException();
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
