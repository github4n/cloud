import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QQ {
    /*public static void main(String[] args) {
        String a = null;
        String b = null;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH)+1;//获取月份
        if(month<10){
             a = "0"+month;
        }
        int day=cal.get(Calendar.DATE);//获取日
        if(day<10){
        b = "0"+day;
        }

        System.out.println(a);
        System.out.println(b);
    }*/

    public static void main(String[] args) throws ParseException {
      /*  String time = "20180802213500";
        String timeFlag = time.substring(8,10);
        System.out.println(timeFlag);*/
        String begin = "2018-08-04 13";
        String end = "2018-08-04 15";
        belongCalendar(begin,end);
        System.out.println("52555");
    }


    public static boolean belongCalendar(String begin, String end) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");//设置日期格式
        Date now =null;
        Date beginTime = null;
        Date endTime = null;
        now = df.parse(df.format(new Date()));
        beginTime = df.parse(begin);
        endTime = df.parse(end);
        boolean flag =  now.getTime() >= beginTime.getTime() && now.getTime() <= endTime.getTime();
        System.out.println("flag:"+flag);
        return flag;
    }

}
