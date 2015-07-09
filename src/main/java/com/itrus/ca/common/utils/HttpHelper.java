/**
 *2012-5-10 下午3:34:07
 */
package com.itrus.ca.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author: bin_liu
 * 
 */
public class HttpHelper {

	public static void main(String[] args) throws IOException {
		String url = "http://localhost:8080/ukeyscca/key/TWD0903103100076/unlocker";
		System.out.println(postHttpRquest(url, "reCode=1o8yfogkJXrRjbDt3HvoFOAmEaJswFCH&adminPinType=nonauto&adminPin=itrus", "UTF-8"));
		System.out.println("----------- sending -------------");

	}

	/**
	 * 请求指定地址
	 * 
	 * @param destURL
	 *            (目标地址,http://xxx.xx.com)
	 * @param parameters
	 *            (POST请求体参数,parame_a=a&param_b=b)
	 * @return String(请求对方回写的内容)
	 * @throws IOException
	 */
	public static String postHttpRquest(String destURL, String parameters)
			throws IOException {
		URL url = null;
		HttpURLConnection uc = null;
		OutputStream out = null;
		InputStream urlStream = null;
		BufferedReader br = null;
		String currentLine = "";
		StringBuffer tempStr = new StringBuffer();

		url = new URL(destURL);
		uc = (HttpURLConnection) url.openConnection();

		uc.setRequestMethod("POST");
		uc.setDoOutput(true);
		out = uc.getOutputStream();
		if (!StringUtils.isEmpty(parameters))
			out.write(parameters.getBytes());
		urlStream = uc.getInputStream();
		Map m = uc.getHeaderFields();

		Iterator it = m.keySet().iterator();

		while (it.hasNext()) {
			String k = (String) it.next();
			tempStr.append(k).append(":").append(uc.getHeaderField(k))
					.append("\n");
		}
		tempStr.append("\n\n");
		br = new BufferedReader(new InputStreamReader(urlStream));
		while ((currentLine = br.readLine()) != null) {
			tempStr.append(currentLine);
		}

		return tempStr.toString();
	}

	/**
	 * 指定字符读回流
	 * 
	 * @param destURL
	 * @param parameters
	 * @param character
	 * @return String
	 * @throws IOException
	 */
	public static String postHttpRquest(String destURL, String parameters,
			String character) throws IOException {
		URL url = null;
		HttpURLConnection uc = null;
		OutputStream out = null;
		InputStream urlStream = null;
		BufferedReader br = null;
		String currentLine = "";
		StringBuffer tempStr = new StringBuffer();

		url = new URL(destURL);
		uc = (HttpURLConnection) url.openConnection();

		uc.setRequestMethod("POST");
		uc.setDoOutput(true);
		out = uc.getOutputStream();
		if (!StringUtils.isEmpty(parameters))
			out.write(parameters.getBytes());
		urlStream = uc.getInputStream();

		// Map m = uc.getHeaderFields();
		// Iterator it = m.keySet().iterator();
		// while(it.hasNext()){
		// String k = (String) it.next();
		// tempStr.append(k).append(":").append(uc.getHeaderField(k))
		// .append("\n");
		// }

		tempStr.append("\n\n");
		br = new BufferedReader(new InputStreamReader(urlStream, character));
		while ((currentLine = br.readLine()) != null) {
			tempStr.append(currentLine);
			tempStr.append("\n");
		}

		return tempStr.toString();
	}

	/**
     * 读回流的时候用指定字符集
     * 
     * @param destURL
     * @param chacter
     * @return String
     * @throws MalformedURLException
     * @throws IOException
     */
    public static String sendGet(String destURL,String chacter)throws MalformedURLException,IOException{
        destURL = destURL.trim().replaceAll(" ","%20");
        destURL = destURL.trim().replaceAll("  ","%20");
        destURL = destURL.trim().replaceAll("   ","%20");
        destURL = destURL.trim().replaceAll("\n","");
        URL url = null;
        HttpURLConnection uc = null;
        OutputStream out = null;
        InputStream urlStream = null;
        BufferedReader br = null;
        String currentLine = "";
        StringBuffer tempStr = new StringBuffer();
        try{
            url = new URL(destURL);
            uc = (HttpURLConnection) url.openConnection();
            uc.setRequestMethod("GET");
            uc.setDoOutput(true);
            out = uc.getOutputStream();
            // 读取回流
            urlStream = uc.getInputStream();
            br = new BufferedReader(new InputStreamReader(urlStream,chacter));
            // 请求头
            Map m = uc.getHeaderFields();
            
            
            //tempStr.append(m.toString()).append("\n");
            
            // 回执请求体
            while(currentLine != null){
                currentLine = br.readLine();
                if(!StringUtils.isEmpty(currentLine))
                    tempStr.append(currentLine).append("\n");
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
            throw new MalformedURLException("url error:" + StringHelper.getStackInfo(e));
        }finally{
            try{
                if(out != null){
                    out.flush();
                    out.close();
                }
                if(br != null)
                    br.close();
                if(uc != null)
                    uc.disconnect();

            }catch(IOException e){
                e.printStackTrace();
                return "IO error:" + StringHelper.getStackInfo(e);
            }
        }

        return tempStr.toString();
    }
	
	/**
	 * @param destURL
	 * @param parameters
	 * @return
	 */
	public static String sendGet(String destURL) throws MalformedURLException,
			IOException {
		destURL = destURL.trim().replaceAll(" ", "%20");
		destURL = destURL.trim().replaceAll("  ", "%20");
		destURL = destURL.trim().replaceAll("   ", "%20");
		destURL = destURL.trim().replaceAll("\n", "");
		URL url = null;
		HttpURLConnection uc = null;
		OutputStream out = null;
		InputStream urlStream = null;
		BufferedReader br = null;
		String currentLine = "";
		StringBuffer tempStr = new StringBuffer();
		try {
			url = new URL(destURL);
			uc = (HttpURLConnection) url.openConnection();
			uc.setRequestMethod("GET");
			uc.setDoOutput(true);
			out = uc.getOutputStream();
			// 读取回流
			urlStream = uc.getInputStream();
			br = new BufferedReader(new InputStreamReader(urlStream));
			// 请求头
			// Map m = uc.getHeaderFields();
			// tempStr.append(m.toString()).append("\n");

			// 回执请求体
			while (currentLine != null) {
				currentLine = br.readLine();
				if (!StringUtils.isEmpty(currentLine))
					tempStr.append(currentLine).append("\n");
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new MalformedURLException("url error:"
					+ StringHelper.getStackInfo(e));
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (br != null)
					br.close();
				if (uc != null)
					uc.disconnect();

			} catch (IOException e) {
				e.printStackTrace();
				return "IO error:" + StringHelper.getStackInfo(e);
			}
		}

		return tempStr.toString();
	}

	public static String getHeader(String destURL) {
		URL url = null;
		HttpURLConnection uc = null;
		OutputStream out = null;
		InputStream urlStream = null;
		BufferedReader br = null;
		String currentLine = "";
		StringBuffer tempStr = new StringBuffer();
		try {
			url = new URL(destURL);
			uc = (HttpURLConnection) url.openConnection();
			Map m = uc.getHeaderFields();
			Iterator it = m.entrySet().iterator();

			while (it.hasNext()) {
				// System.out.println(it.next());
				tempStr.append(it.next()).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempStr.toString();
	}
}
