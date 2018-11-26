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
	
	long startTime; 
	long endTime; 
	
	// Func : Issue the client key after checking client_Id and client_Secret
	@RequestMapping(value="/nkey", method=RequestMethod.GET)
	public Map<String, String> nkey(@RequestParam(value="code", required = true, defaultValue = "0") String code, @RequestParam(value="value", required = true, defaultValue = "") String value, HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String clientID = request.getHeader("X-Naver-Client-Id");
		String clientSecret = request.getHeader("X-Naver-Client-Secret");
		
		Map<String, String> res = new HashMap<String, String>();
		
		if(this.checkClientValid(clientID, clientSecret) == 1) {
			switch(code) {
				case "0":
					res.put("key", this.IssueKey(clientID, clientSecret));
					response.setStatus(200);
					startTime = System.currentTimeMillis(); // Timer(0~7200) 기능을 위해 StartTime을 저장
					break;
				case "1":
					break;
				default:
					res.put("error", "code value does not valid!");
					break;
			}
//			res.put("IsTime", this.checkTime()+"");
		}else {
			res.put("error", "ClientId and ClientSecret does not valid!");
		}
        
		return res;
	}
	
	// func : Call the Image api 
	@RequestMapping(value="/ncaptcha.bin")
	public Map<String, String> capchaImg(@RequestParam(value="key", required=true, defaultValue="") String key, HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("UTF-8");
		String clientID = request.getHeader("X-Naver-Client-Id");
		String clientSecret = request.getHeader("X-Naver-Client-Secret");
		
		Map<String, String> res = new HashMap<String, String>();
		
		if(this.checkClientValid(clientID, clientSecret) == 1) {
			if(this.checkKeyValid(clientID, clientSecret, key) == 1) {
				this.randomImage(clientID, clientSecret);
				
				// Server 내부에 저장된 File 가져오는 부분 (추후에 API로 가져오도록 변경)
				FileVO fileVO = rs.getFile(6);
				String fileUrl = fileVO.getUrl();
	            String fileName = fileVO.getName();
	            /////////////////////////////////////////////////////
				
				//파일 업로드된 경로 
		        try{
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
		                rs.setIsIssued(clientID, clientSecret);
		            }
		        } catch (Exception e) {
		        	response.setContentType("application/json;charset=UTF-8");
		        	response.setStatus(400);
		        	res.put("error", "Unissued image(이미지 발급을 하지 않음)");
		        }
			}else {
				response.setContentType("application/json;charset=UTF-8");
				response.setStatus(403);
				res.put("error", "Invalid key. (키가 만료되거나 없는 키)");
			}
		}else {
			response.setContentType("application/json;charset=UTF-8");
			res.put("error", "ClientId and ClientSecret does not valid!");
		}
		return res;
	}
	
	// func : Issue the client key 
	public String IssueKey(String id, String secret) throws Exception{
		RegisterVO vo = new RegisterVO();
		vo.setClientID(id);
		vo.setClientSecret(secret);
		return rs.setKey(vo);
	}
	
	public void randomImage(String id, String secret) throws Exception{
		rs.randomImage(id, secret);
	}
	
	// func : Calculate the response time after issued key
	public boolean checkTime() throws Exception {
		endTime = System.currentTimeMillis();
        double diff = (endTime - startTime)/1000.0;
		if(0 < diff && diff <= 10) {
			return true;
		}
		return false;
	}
	
	// func : the client id and secret value validation check (1:valid, 0:not valid)
	public int checkClientValid(String id, String secret) throws Exception{
		return rs.checkClientValid(id, secret);
	}
	
	// func : the key value validation check (1:valid, 0:not valid)
	public int checkKeyValid(String id, String secret, String key) throws Exception{
		return rs.checkKeyValid(id, secret, key);
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
