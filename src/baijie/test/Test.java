package baijie.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.BitOperate;
import util.BitOperate.BitOperateException;
import util.webpage.Constant;
import util.webpage.Course;
import util.webpage.Course.TimeAndAddress;
import util.webpage.Post;
import util.webpage.ReadPageHelper;
import util.webpage.SchoolWebpageParser;
import util.webpage.Student;

public class Test {

	/**
	 * @param args
	 */
	/*public static void main(String[] args) {
		InputStream is = null;
		String page = null;
		ReadPageHelper scores = null;
		ReadPageHelper readPageHelper = new ReadPageHelper("20106173","01021061");
		if(login.doLogin()){
			scores = new ReadPageHelper("http://59.67.148.66/score/xszxcjcy.asp",login.getSessionID());
			String result = scores.openInputStream();
			if(result.equals("OK")){
				is = scores.getInputStream();
				try{
					page = readIt(new BufferedInputStream(is),10000);
					System.out.println(page);
				}catch(Exception e){
					System.err.println(e);
				}
				scores.getConnection().disconnect();
			}
			else
				System.err.println(result);
		}
		else
			System.err.println("Can't login");
		

	}*/
	public static void main(String[] args) {
		try{
		switch(15){
		case 1:
			ReadPageHelper readHelper = new ReadPageHelper("20106135","20106135");
			try{
				if(readHelper.doLogin()){
					//System.out.println(readHelper.get(Constant.url.本学期修读课程));
//					System.out.println(readHelper.getWithDocument(Constant.url.个人全部成绩).getElementsByTag("table").get(1));
					Student stu = new Student();
					ArrayList<Course> courses = SchoolWebpageParser.parseScores(
							Constant.url.个人全部成绩, readHelper, stu);
					for(Course c:courses)
						System.out.println(c);
					System.out.println(stu);
					System.out.println(stu.getName()+stu.getNumber()+stu.isMale()+stu.getBirthdayString()
							+stu.getAdmissionTimeString()+stu.getAcademicPeriod()+stu.getSchoolName()
							+stu.getMajorName()+stu.getClassName()+stu.getUrlOfFacedPhoto());
				}
				else
					System.err.println("Can't log in!");
			}
			catch(Exception e){
				System.err.println(e);
			}
		break;
		case 2:
			boolean result = "2,4,6,8,10,12,14,18 1-21".matches(".*[^\\d\\s\u00a0\u3000;；,，\\-－\u2013\u2014\u2015].*");
			System.out.println(result);
			for(String s:"2,4,6,8,10,12,14,18 01-21".split("[\\s\u00a0\u3000;；,，]"))
				System.out.println(s);
			System.out.println(Integer.parseInt("01"));
			int week =  3 ;
			try {
				week = BitOperate.add1onCertainBit(0,"2, 4;6 ;8 ；;,，10，12 14,18- 20,");
			} catch (BitOperateException e) {
				//e.printStackTrace();
				System.out.println(e);
			}
			System.out.println(Integer.toBinaryString(week));
		break;
		case 3:
			try {
				TimeAndAddress t = new TimeAndAddress("5-0102");
				//t.addDays("星期一到星期三   星期五 到星期六 周日");
				//t.addDays("星期一,星期三,;，；   星期六");
				t.addDays("星期三 到 周日");
				System.out.println(Integer.toBinaryString(t.getDay()));
				t.addWeeks("1-6 9 12 13",true);
				System.out.println(Integer.toBinaryString(t.getWeek()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		break;
		/*case 4:
			try {
				String[] result1 = SchoolWebpageParser.splitTime("1-10,12-15,18 周 周一至周四，周五 3-4 节 ");
				for(String s:result1)
					System.out.println(s);
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		break;*/
		case 5:
			try {
				System.out.println(BitOperate.convertIntToString(BitOperate.add1onCertainBit(0, "2, 4;6 ;8 ；;,，10，12 14,18-20")));
			} catch (BitOperateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		break;
		case 6:
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08"), Locale.PRC);
			calendar.clear();
			calendar.set(2012,4, 9);//("2007-05-09")
			Date start = calendar.getTime();
			calendar.clear();
			calendar.set(2007,10, 13);//("2007-11-13");
			Date end = calendar.getTime();
			ReadPageHelper readHelper1 = new ReadPageHelper();
			readHelper1.setTimeout(12000);
			
			ArrayList<Post> resultOfPosts = SchoolWebpageParser.parsePosts(
					Post.SOURCES.WEBSITE_OF_TEACHING_AFFAIRS, start, null, 100, readHelper1);
			for(Post aPost:resultOfPosts)
				System.out.println(aPost.toString());
			System.out.println(resultOfPosts.size());
		break;
		case 7:
			try{
				Document doc = new ReadPageHelper().getWithDocument("http://59.67.148.66:8080/getRecords.jsp?url=list.jsp&pageSize=40&name=%D6%D8%D2%AA%CD%A8%D6%AA&currentPage=1");
				Elements posts = doc.body().select("table table table table").get(0).getElementsByTag("tr");
				System.out.println(posts.size());
				System.out.println(posts.get(40).outerHtml());
				Element post1 = posts.get(1);
				Element link = post1.getElementsByTag("a").first();
				System.out.println(link.attr("abs:href"));
				System.out.println(link.text());
				System.out.println(link.nextSibling().outerHtml().trim().substring(1, 11));
				
				System.out.println(doc.body().select("table table table table").get(1).select("tr td form font:eq(1)").text());
			}catch(Exception e){
				System.err.println(e.getMessage());
			}
		break;
		case 8:
			ReadPageHelper helper9 = new ReadPageHelper("20106173","01021061");
			helper9.setCharset("UTF-8");
			helper9.prepareToParsePostsFromSCCE();
			System.out.println(helper9.getWithDocument("http://59.67.152.3/wnoticemore.aspx").select("form table table").first().getElementsByTag("tr").last().getElementsByTag("td").get(3).text());
			helper9.doLogin();
			//System.out.println(helper9.get(Constant.url.个人全部成绩));
			//System.out.println(helper9.get("http://59.67.152.3/wnoticemore.aspx"));
			
		break;
		case 9:
			ReadPageHelper helper8 = new ReadPageHelper();
			if(helper8.prepareToParsePostsFromSCCE()){
				//Document doc8 = helper8.getWithDocument("http://59.67.152.3/wnewmore.aspx?page=2");
				/*Element page = doc8.body().select("a[href*=more.aspx?page=]").last();
				System.out.println(page.html());
				Pattern pattern = Pattern.compile("\\?page=(\\d+)");
				Matcher matcher = pattern.matcher(page.attr("href"));
				while(matcher.find())
					System.out.println(matcher.group(1));
				Matcher m = Pattern.compile("\\?page=(\\d+)").matcher(doc8.body().select("a[href*=more.aspx?page=]").last().attr("href"));
				m.find();
				System.out.println(m.group(1));*/
				/*Elements posts9 = doc8.select("form table table").first().getElementsByTag("tr");
				posts9.remove(0);
				System.out.println(posts9.html());*/
				ArrayList<Post> result9 = null;
				//result9 = SchoolWebpageParser.parsePostsFromSCCE("学校新闻", null, null, 0, doc8);
				result9 = SchoolWebpageParser.parsePosts(Post.SOURCES.WEBSITE_OF_SCCE, new String[]{Post.CATEGORYS.SCCE_NOTICE_UNION, Post.CATEGORYS.SCCE_NEW_DEPARTMENT}, null, null, 0, helper8);
				//result9 = SchoolWebpageParser.parsePostsFromSCCE(new String[]{"学生通知","学校新闻"}, null, null, 0, helper8, null);
				for(Post p:result9)
					System.out.println(p.toString());
				System.out.println(result9.size());
				//System.out.println(posts9.get(2).getElementsByTag("td").first().getElementsByTag("a").attr("onclick"));
			}else
				System.err.println("can't set session cookie!");
			
			//System.out.println(doc8.body().select(""));
		break;
		case 10:
			ReadPageHelper helper10 = new ReadPageHelper();
			ArrayList<Post> result10 = null;
			result10 = SchoolWebpageParser.parsePosts(Post.SOURCES.WEBSITE_OF_SCCE, Post.convertToDate(2012, 6, 1), null, 22, helper10);
			for(Post p:result10)
				System.out.println(p.toString());
			System.out.println(result10.size());
		break;
		case 11:
			ReadPageHelper helper11 = new ReadPageHelper();
//			Document doc11 = helper11.getWithDocumentForParsePostsFromSCCE("http://59.67.152.6/Channels/7");
//			System.out.println(doc11.select(".oright .orbg ul li").last()
//					.getElementsByClass("date").first().text().trim().substring(1, 11));
//			Matcher matcher = Pattern.compile("共(\\d+)页").matcher(doc11.select(".oright .page").first().text());
//			matcher.find();
//			System.out.println(matcher.group(1));
			ArrayList<Post> result11;
//			result11 = SchoolWebpageParser.parsePostsFromSCCEStudent("新闻", null, null, 0, doc11);
//			result11 = SchoolWebpageParser.parsePostsFromSCCEStudent(Post.CATEGORYS.SCCE_STUDENT_NOTICES, Post.convertToDate(2012, 6, 1), null, 0, helper11);
			result11 = SchoolWebpageParser.parsePosts(Post.SOURCES.STUDENT_WEBSITE_OF_SCCE, Post.convertToDate(2012, 6, 1), null, 0, helper11);
			for(Post p:result11)
				System.out.println(p);
			System.out.println(result11.size());
		break;
		case 12:
			Student stu1 = new Student("20106173", "柏杰", "计算机与通信工程学院", "计算机科学与技术（中加合作）", "中加6班");
			System.out.println(stu1);
			Student stu2 = new Student(stu1);
			stu2.setBirthday("1991-3-8");
			stu2.setAdmissionTime("2010-9-12");
			stu2.setIsMale(true);
			stu2.setAcademicPeriod(4);
			System.out.println(stu1);
			System.out.println(stu2);
		break;
		case 13:
			ReadPageHelper helper13 = new ReadPageHelper("20106173","01021061");
			if(helper13.doLogin()){
				Document doc12 = helper13.getWithDocument(Constant.url.已选下学期课程);
				System.out.println(doc12.body().child(1).text());
//				System.out.println(doc12.select("table").first().text());
				Pattern pattern = Pattern.compile
						("学号(?:：|:)(.*)姓名(?:：|:)(.*)学院(?:：|:)(.*)专业(?:：|:)(.*)班级(?:：|:)(.*\\d+班)");
				Matcher matcher = pattern.matcher(doc12.body().child(1).text());
				if(matcher.find()){
					Student studentInfoToReturn = new Student();
					studentInfoToReturn.setNumber( matcher.group(1).replace('\u00a0', ' ').trim() );
					studentInfoToReturn.setName( matcher.group(2).replace('\u00a0', ' ').trim() );
					studentInfoToReturn.setSchoolName( matcher.group(3).replace('\u00a0', ' ').trim() );
					studentInfoToReturn.setMajorName( matcher.group(4).replace('\u00a0', ' ').trim() );
					studentInfoToReturn.setClassName( matcher.group(5).replace('\u00a0', ' ').trim() );
					System.out.println(studentInfoToReturn);
				}
			}
			else
				System.out.println("Can't log in.");
		break;
		case 14:
			ReadPageHelper helper14 = new ReadPageHelper("20106173","01021061");
			if(helper14.doLogin()){
				/*Document doc14 = helper14.getWithDocument(Constant.url.期末最新成绩);
				System.out.println(doc14.body().getElementsByTag("p").get(2).text());
				Pattern pattern = Pattern.compile
						("学号(?:：|:)(.*)姓名(?:：|:)(.*)");
				Matcher matcher = pattern.matcher(doc14.body().getElementsByTag("p").get(2).text());
				if(matcher.find()){
					Student studentInfoToReturn = new Student();
					studentInfoToReturn.setNumber( matcher.group(1).replaceAll("[\u3000\u00a0]", " ").trim() );
					studentInfoToReturn.setName( matcher.group(2).replaceAll("[\u3000\u00a0]", " ").trim() );
					System.out.println(studentInfoToReturn);
					System.out.println(studentInfoToReturn.getNumber()+studentInfoToReturn.getName());
				}*/
				Document doc14 = helper14.getWithDocument(Constant.url.个人全部成绩);
				System.out.println(doc14.getElementsByTag("table").first());
			}
			else
				System.out.println("Can't log in.");
			
		break;
		case 15:
			Post post1 = new Post();
			Post post2 = new Post(post1);
			Post post3 = post1.clone();
			System.out.println(post1);
			System.out.println(post2);
			System.out.println(post3);
			post2.setDate("2004-3-01");
			post2.setAuthor("Test Jie");
			post3.setDate(2020, 4, 1);
			post3.setTitle("发大水发大水發都發");
			System.out.println(post1);
			System.out.println(post2);
			System.out.println(post3);
			
		break;
		default:;
		}
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	// Reads an InputStream and converts it to a String.
	/*public static String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "GB2312");        
	    char[] buffer = new char[len];
	    reader.read(buffer);
	    stream.close();
	    return new String(buffer);
	}*/
}
