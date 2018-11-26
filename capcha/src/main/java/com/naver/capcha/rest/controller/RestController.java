package com.naver.capcha.rest.controller;

import javax.annotation.Resource;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

import java.math.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Document;
import javax.websocket.server.PathParam;

import org.jsoup.nodes.*;
import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.*;
import com.naver.capcha.rest.domain.*;
import com.naver.capcha.rest.service.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	@Resource(name="com.naver.capcha.rest.service.RestService")
	RestService rs;
	
	/*long startTime = System.currentTimeMillis(); 
	long endTime = System.currentTimeMillis(); 
	long diff = endTime - startTime; */
	
	long startTime; 
	long endTime; 
	 
	
	@RequestMapping(value="/getCategory", method=RequestMethod.GET)
	public List<FileCategoryVO> getImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<FileCategoryVO> fvo = rs.getCategory();
		
		// Test Code (Must Delete)
		startTime = System.currentTimeMillis();
		
		return fvo;
	}
	
	// Func : Issue the client key after checking client_Id and client_Secret
	
	@RequestMapping(value="/nkey", method=RequestMethod.GET)
	public Map<String, String> nkey(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		int code = Integer.parseInt(request.getParameter("code"));
		String clientID = request.getHeader("X-Naver-Client-Id");
		String clientSecret = request.getHeader("X-Naver-Client-Secret");
		
		switch(code) {
			case 0:
				this.IssueKey();
				break;
			case 1:
				break;
			default:
				break;
		}
		
		Map<String, String> res = new HashMap<String, String>();
		res.put("clientID", clientID);
		res.put("clientSecret", clientSecret);
		res.put("code", code+"");
		
		res.put("IsTime", this.checkTime()+"");
        
		return res;
	}
	
	/*
	 * IssueKey()
	 * Issue the client key
	 */
	public void IssueKey() throws Exception{
		
	}
	
	@RequestMapping(value="/getCapchaImg", method=RequestMethod.GET)
	public void getCapchaImg(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
        FileVO fileVO = rs.getFile(13);
        
        //파일 업로드된 경로 
        try{
            String fileUrl = fileVO.getUrl();
            String fileName = fileVO.getName();
            InputStream in = null;
            File file = null;
            boolean skip = false;
            
            //파일을 읽어 스트림에 담기  
            try{
                file = new File(fileUrl, fileName);
                in = new FileInputStream(file);
            } catch (FileNotFoundException fe) {
                skip = true;
            }
            
            if(!skip) {
            	response.setContentType("image/jpeg;charset=UTF-8");
            	
            	OutputStream out = response.getOutputStream();
                byte b[] = new byte[(int)file.length()];
                int leng = 0;
                while ((leng = in.read(b)) > 0) {
                	out.write(b, 0, leng);
                }
                in.close();
            }else {
            	response.setContentType("application/json;charset=UTF-8");
            	System.out.println("<script language='javascript'>alert('파일을 찾을 수 없습니다');history.back();</script>");
            }
        } catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
        }
	}
	
	/*
	 * boolean checkTime()
	 * : Calculate the response time after issued key
	 */
	public boolean checkTime() throws Exception {
		endTime = System.currentTimeMillis();
        double diff = (endTime - startTime)/1000.0;
		if(0 < diff && diff <= 10) {
			return true;
		}
		return false;
	}
	
	/*
	 * String checkTime()
	 * : Calculate the response time after issued key
	 */
	public String checkClientValid(String id, String secret) throws Exception{
		
		return "";
	}
	
	
	// Jsoup로 Wiki HTML 크롤링 Test
	@RequestMapping(value="/htmlTest", method=RequestMethod.GET)
//	public ArrayList<String> getSource() throws MalformedURLException, IOException {
	public String getSource() throws MalformedURLException, IOException {
		// No 1
		/*String text_url = "https://en.wikipedia.org/wiki/Contrasting_and_categorization_of_emotions";
		ArrayList<String> output = new ArrayList<>(); 
		BufferedReader br = new BufferedReader(new InputStreamReader(new URL(text_url).openStream(),"utf-8")); 
		String line; 
		while ((line = br.readLine()) != null) { 
		output.add(line); 
		} 
		
		return output;*/ 
		
		// No 2
		/*URL text_url = new URL("https://en.wikipedia.org/wiki/Contrasting_and_categorization_of_emotions");
	    org.jsoup.nodes.Document doc = Jsoup.parse(text_url, 3000);

	    ArrayList<String> downServers = new ArrayList<>();
	    org.jsoup.nodes.Element table = doc.select("table").get(0); //select the first table.
	    org.jsoup.select.Elements rows = table.select("tr");

	    for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
	        org.jsoup.nodes.Element row = rows.get(i);
	        org.jsoup.select.Elements cols = row.select("td");

	        if (cols.get(3).text().equals("Titan")) {
	            if (cols.get(7).text().equals("down"))
	                downServers.add(cols.get(5).text());

	            do {
	                if(i < rows.size() - 1)
	                   i++;
	                row = rows.get(i);
	                cols = row.select("td");
	                if (cols.get(7).text().equals("down") && cols.get(3).text().equals("")) {
	                    downServers.add(cols.get(5).text());
	                }
	                if(i == rows.size() - 1)
	                    break;
	            }
	            while (cols.get(3).text().equals(""));
	            i--;
	        }
	    }
	    return downServers;*/
		
		// No 3
		ArrayList<String> downServers = new ArrayList<>();
		try {
			//웹에서 내용을 가져온다.
			org.jsoup.nodes.Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Contrasting_and_categorization_of_emotions").get();
			//내용 중에서 원하는 부분을 가져온다.
			org.jsoup.select.Elements contents = doc.select("body .wikitable");
			org.jsoup.nodes.Element table = contents.get(1);
			org.jsoup.select.Elements rows = table.select("tr");
			
			 for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
		        org.jsoup.nodes.Element row = rows.get(i);
		        org.jsoup.select.Elements cols = row.select("td");

		        /*if (cols.get(3).text().equals("Titan")) {
		            if (cols.get(7).text().equals("down"))
		                downServers.add(cols.get(5).text());

		            do {
		                if(i < rows.size() - 1)
		                   i++;
		                row = rows.get(i);
		                cols = row.select("td");
		                if (cols.get(7).text().equals("down") && cols.get(3).text().equals("")) {
		                    downServers.add(cols.get(5).text());
		                }
		                if(i == rows.size() - 1)
		                    break;
		            }
		            while (cols.get(3).text().equals(""));
		            i--;
		        }*/
		    }
			
			
			
			String text = "";
			return text;
		} catch (IOException e) { //Jsoup의 connect 부분에서 IOException 오류가 날 수 있으므로 사용한다.   
			e.printStackTrace();
		}
		return "";
	}
	
	
}
