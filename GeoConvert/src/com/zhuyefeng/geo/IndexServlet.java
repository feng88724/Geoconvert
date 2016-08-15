package com.zhuyefeng.geo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.zhuyefeng.geo.bean.CommonResult;
import com.zhuyefeng.geo.bean.Point;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/IndexServlet")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IndexServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String coords = request.getParameter("coords");
		String type = request.getParameter("type");
		String result = "请求失败";
		try {
			result = doConvert(coords);
			
			if("1".equals(type)) {
				// 百度经纬度 转 GPS经纬度
				Gson gson = new Gson();
				CommonResult midResult = gson.fromJson(result, CommonResult.class);
				if(midResult != null && midResult.getResult() != null && coords != null) {
					String[] coordsArray = coords.split(";");
					List<Point> coordsList = new ArrayList<Point>();
					for (String str : coordsArray) {
						if(!"".equals(str)) {
							String[] latlng = str.split(",");
							if(latlng != null && latlng.length == 2) {
								coordsList.add(new Point(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1])));
							}
						}
					}
					if(midResult.getResult().size() == coordsList.size()) {
						for (int i = 0; i < coordsList.size(); i++) {
							Point source = coordsList.get(i);
							Point dist = midResult.getResult().get(i);
							dist.setX(2 * source.getX() - dist.getX());
							dist.setY(2 * source.getY() - dist.getY());
						}
					}
					result = gson.toJson(midResult);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			CommonResult cr = new CommonResult();
			cr.setStatus("500");
			cr.setMessage("500 - Internal Server Error");
			Gson gson = new Gson();
			result = gson.toJson(cr);
		}
		response.getWriter().write(result);
		response.getWriter().flush();
		response.flushBuffer();
	}
	
	/**
	 * 经纬度转换
	 * @param coords
	 * @return
	 * @throws Exception
	 */
	public static String doConvert(String coords) throws Exception {
		if(coords == null || coords.trim().length() == 0) {
			return null;
		}
		coords = coords.trim();
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;

		try {
			String urlStr = String.format("http://api.map.baidu.com/geoconv/v1/?coords=%s&from=1&to=5&ak=py0HjwCUjEXL9gNnA6FoY3ckQxO7iSLp", coords);
			System.out.println(urlStr);
			URL url = new URL(urlStr);
			URLConnection connection = url.openConnection();
			inputStream = connection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return resultBuffer.toString();
	}
}
