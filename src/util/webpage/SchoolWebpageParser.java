package util.webpage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.BitOperate.BitOperateException;
import util.webpage.Course.CourseException;
import util.webpage.Course.TimeAndAddress.TimeAndAddressException;

public class SchoolWebpageParser {

    private static final int SEQUENCE_NUMBER = 0;
    private static final int COURSE_CODE = 1;
    private static final int COURSE_NAME = 2;
    private static final int CLASS_NUMBER = 3;
    private static final int COURSE_TEACHER = 4;
    private static final int COURSE_CREDIT = 5;
    private static final int COURSE_TIME = 6;
    private static final int COURSE_ADDRESS = 7;
    //private static

	
	public SchoolWebpageParser(boolean ignoreWhitespace){}
	public static ArrayList<Course> parseCourse(String url, 
			ReadPageHelper readPageHelper, Student studentInfoToReturn) throws ParserException, IOException{
		Document doc = readPageHelper.getWithDocument(url);
		//student
		
		//courses
		return readCourseTable(doc.getElementsByTag("table").get(0));
	}
	private static ArrayList<Course> readCourseTable(Element table) throws ParserException {
	    int totalCredit = 0, totalCreditCalculated = 0;
	    ArrayList<Course> result = new ArrayList<Course>();
	    Elements courses = table.getElementsByTag("tr");
	    
	    HashMap<Integer, Integer> headingMap = getHeading(courses.first());
	    if(headingMap == null)
	    	throw new ParserException("Can't getHeading because result is null.");
	    for(int i=1;i<courses.size()-1;i++)
			try {
				result.add(readCourse(courses.get(i), headingMap));
			} catch (Exception e) {
				System.out.println("Can't parse \""+courses.get(i).html()+"\".");
				e.printStackTrace();
			}
	    result.trimToSize();
	    totalCredit = Integer.parseInt( courses.last().text().replaceAll("\\D+", "") );
	    
	    for(Course c:result)
	    	totalCreditCalculated += c.getCredit();
	    if(totalCredit != totalCreditCalculated)
	    	System.out.println("Warning: TotalCreditCalculated doesn't match " +
	    			"with totalCredit fetched from page .");
	    	//throw new ParserException(
	    	//		"TotalCreditCalculated doesn't match with totalCredit fetched from page.");
		return result;
	}
	private static Course readCourse(Element course, HashMap<Integer, Integer> headingMap){
		String rawTime = null, rawAddress = null;
		Course result = new Course();
		Elements cols = course.getElementsByTag("td");
		int i;
		Integer fieldCode;
		for(i = 0;i<cols.size();i++){
			fieldCode = headingMap.get(i);
			if(fieldCode == null)
				continue;
			switch(fieldCode){
			case SEQUENCE_NUMBER:continue;
			case COURSE_CODE:result.setCode(cols.get(i).text().trim());break;
			case COURSE_NAME:result.setName(ReadPageHelper.deleteSpace(cols.get(i).text()));break;
			case CLASS_NUMBER:result.setClassNumber(cols.get(i).text().trim());break;
			case COURSE_TEACHER:
				try {
					result.addTeacher(cols.get(i).text().trim());
				} catch (CourseException e) {
					System.out.println("Can't add teacher normally. Because " 
							+ e.getMessage());
				}
				break;
			case COURSE_CREDIT:
				try{
					result.setCredit(Byte.parseByte(cols.get(i).text()));
				}catch(Exception e){
					System.out.println("Can't parse credit normally. Because " 
							+ e.getMessage());
				}
				break;
			case COURSE_TIME:rawTime= cols.get(i).getElementsByTag("font").get(0).html();break;
			case COURSE_ADDRESS:rawAddress=cols.get(i).getElementsByTag("font").get(0).html();break;
			default:System.out.println("Unknown column.");
			}
		}
		try{
			readTimeAndAddress(result, rawTime, rawAddress);
		}catch(Exception e){
			System.out.println(
					"Can't parse time&address normally. Because " + e.getMessage());
		}
		return result;
	}
	private static void readTimeAndAddress(Course result, String rawTime, String rawAddress) 
			throws ParserException, TimeAndAddressException, BitOperateException {
		if(rawTime==null)
			throw new ParserException("Error: rawTime == null");
		if(rawAddress==null)
			throw new ParserException("Error: rawAddress == null");
		String[] times, addresses ,timesSplited;
		times = splitTimeOrAddress(rawTime);
		addresses = splitTimeOrAddress(rawAddress);
		if(times.length != addresses.length)
			throw new ParserException("Error: times.length != addresses.length");
		for(int index = 0;index<times.length;index++){
			timesSplited = splitTime(times[index]);
			result.addTimeAndAddress(timesSplited[0], timesSplited[1], 
					timesSplited[2], addresses[index]);
		}
	}
	private static String[] splitTime(String time) throws ParserException{
		int counter = 0;
		String[] result = new String[3];
		Pattern pattern = Pattern.compile(
				"(\\d[\\d\\s\u00a0\u3000;；,，\\-－\u2013\u2014\u2015]*\\d)|" +
				"((星期|周)[一二三四五六日]([\\s\u00a0\u3000;；,，星期周日一二三四五六至到]*[一二三四五六日])?)");
		Matcher matcher = pattern.matcher(time);
		while(matcher.find())
			if(counter<3){
				result[counter] = matcher.group();
				counter++;
			}
			else
				throw new ParserException("Unexpected time String.");
		if(counter != 3)
			throw new ParserException("Unexpected time String.");
		return result;
	}
	/*
	private static String[] splitTime(String time) throws ParserException{
		int counter = 0;
		String[] result = new String[3];
		Pattern numberPattern = Pattern.compile(
				"\\d[\\d\\s\u00a0\u3000;；,，\\-－\u2013\u2014\u2015]*\\d");
		Pattern dayOfWeekPattern = Pattern.compile(
				"(星期|周)[一二三四五六日]([\\s\u00a0\u3000;；,，星期周日一二三四五六至到]*[一二三四五六日])?");
		Matcher numberMatcher = numberPattern.matcher(time);
		Matcher dayOfWeekMatcher = dayOfWeekPattern.matcher(time);
		while(numberMatcher.find()){
			counter++;
			switch(counter){
			case 1:result[0] = numberMatcher.group();break;
			case 2:result[2] = numberMatcher.group();break;
			default:throw new ParserException("Unexpected time String.");
			}
		}
		if(dayOfWeekMatcher.find())
			result[1] = dayOfWeekMatcher.group();
		return result;
	}*/
	/*
	private String[] splitTime(String time) throws ParserException{
		time = time.trim();
		if(!time.matches(".*节"))
			throw new ParserException("Can't match the time.");
		ArrayList<String> result = new ArrayList<String>(3);
		String[] splited = time.split("周");
		result.add(splited[0].trim());
		splited = splited[1].split("[\\s\u00a0\u3000]");
		int lastWeekString = 0,i;
		for(i = 0;i<splited.length;i++)
			if(splited[i].matches(".*[星期周一二三四五六日].*") && i>lastWeekString)
				lastWeekString = i;
		String temp = "";
		for(i = 0;i<=lastWeekString;i++){
			splited[i] = splited[i].trim();
			if(splited[i].length()>0)
				temp += splited[i] + " ";
		}
		result.add(temp.trim());
		temp = "";
		for(i = lastWeekString;i<splited.length;i++){
			splited[i] = splited[i].trim();
			if(splited[i].length()>0){
				if(!splited[i].matches(".*节.*"))
					temp += splited[i] + " ";
				else if(splited[i].matches(".*节"))
					temp += splited[i].replace("节", "");
			}
		}
	}*/
	private static String[] splitTimeOrAddress(String timeOrAddress){
		String[] first;
		ArrayList<String> second = new ArrayList<String>();
		first = timeOrAddress.split("<[^><]*>");
		for(String s:first){
			s.trim();
			if(s.length()>0)
				second.add(s);
		}
		return (String[])second.toArray(new String[0]);
	}
	private static HashMap<Integer, Integer> getHeading(Element heading) throws ParserException {
		String colName = null;
		HashMap<Integer, Integer> headMap = new HashMap<Integer, Integer>(8);
		Elements cols = heading.getElementsByTag("td");
		if(cols == null || cols.isEmpty())
			throw new ParserException("Can't getHeading because no td tag in first line.");
		for(int i=0;i<cols.size();i++){
			colName = ReadPageHelper.deleteSpace(cols.get(i).text());
			//assert colName != null;
			if("序号".equals(colName))
				headMap.put(i, SEQUENCE_NUMBER);
			else if("课程代码".equals(colName))
				headMap.put(i, COURSE_CODE);
			else if("课程名称".equals(colName))
				headMap.put(i, COURSE_NAME);
			else if("教学班号".equals(colName))
				headMap.put(i, CLASS_NUMBER);
			else if("教师".equals(colName))
				headMap.put(i, COURSE_TEACHER);
			else if("学分".equals(colName))
				headMap.put(i, COURSE_CREDIT);
			else if("时间".equals(colName))
				headMap.put(i, COURSE_TIME);
			else if("地点".equals(colName))
				headMap.put(i, COURSE_ADDRESS);
		}
		return headMap;
	}
	
	public static class ParserException extends Exception{
		private static final long serialVersionUID = 3737828070910029299L;
		public ParserException(String message){
			super(message + " @SchoolWebpageParser");
		}
		public ParserException(){
			this("encounter Exception when parse school page.");
		}
	}
}