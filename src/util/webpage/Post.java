/**
 * 
 */
package util.webpage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <strong>Note:</strong>大部分用于设置的方法返回this，做Builder
 * @author Bai Jie
 *
 */
public class Post {
	String tag;
	String title;
	String url;
	String category;
	Date date;

	public Post() {
		super();
		tag = title = url = category = null;
		date = null;
	}
	public Post(String title, String url, String date, String tag, String category) {
		this();
		this.tag = tag;
		this.title = title;
		this.url = url;
		this.category = category;
		try {
			setDate(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Can't parse date normally. "+e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public Post setTag(String tag) {
		this.tag = tag;
		return this;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public Post setTitle(String title) {
		this.title = title;
		return this;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public Post setUrl(String url) {
		this.url = url;
		return this;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public Post setCategory(String category) {
		this.category = category;
		return this;
	}
	/**
	 * 对返回时间的修改不会影响本对象（只读）
	 * @return the date
	 */
	public Date getDate() {
		return (Date)date.clone();
	}
	/**
	 * @param date the date to set
	 */
	public Post setDate(Date date) {
		this.date = date;
		return this;
	}
	/**
	 * 以字符串显示日期，用指定的分隔符（YYYY(delimiter)MM(delimiter)DD）
	 * @param delimiter 日期分隔符
	 * @return 类似2012(delimiter)07(delimiter)16的字符串
	 */
	public String getDateString(String delimiter){
		SimpleDateFormat dateFormat = 
				new SimpleDateFormat("yyyy'"+delimiter+"'MM'"+delimiter+"'dd", Locale.PRC);
		return dateFormat.format(this.date);
	}
	/**
	 * 以字符串显示日期(YYYY-MM-DD)
	 * @return 类似2012-07-16的字符串
	 */
	public String getDateString(){
		return getDateString("-");
	}
	/**
	 * 设置日期
	 * @param year 年
	 * @param month 月
	 * @param date 日
	 * @return 返回this（Builder）
	 */
	public Post setDate(int year, int month, int date){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08"), Locale.PRC);
		calendar.clear();
		calendar.set(year, month-1, date);
		this.date = calendar.getTime();
		return this;
	}
	/**
	 * 以字符串(YYYY-MM-DD格式)设置日期
	 * @param date YYYY-MM-DD格式的字符串，例如2012-07-16
	 * @return 返回this（Builder）
	 * @throws ParseException if the beginning of the specified string cannot be parsed.
	 */
	public Post setDate(String date) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.PRC);
		this.date = dateFormat.parse(date);
		return this;
	}
	
	public String toString(){
		return getCategory()+"\t"+getTag()+"\t"+getTitle()+"\t"+getUrl()+"\t"+getDateString();
	}
}