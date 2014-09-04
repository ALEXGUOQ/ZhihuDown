package controller;

import java.util.ArrayList;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.FileReaderWriter;
import model.Zhihu;

public class Spider {
	public static void main(String[] args) {
		// ���弴�����ʵ�����
		String url = "http://www.zhihu.com/explore/recommendations";
		// �������Ӳ���ȡҳ������
		String content = Spider.SendGet(url);

		// ��ȡ�༭�Ƽ�
		ArrayList<Zhihu> myZhihu = Spider.GetRecommendations(content);

		// д�뱾��
		for (Zhihu zhihu : myZhihu) {
			FileReaderWriter.writeIntoFile(zhihu.writeString(),
					"D:/֪��_�༭�Ƽ�.txt", true);
		}
	}

	public static String SendGet(String url) {
		// ����һ���ַ��������洢��ҳ����
		String result = "";
		CloseableHttpClient client = HttpClients.createDefault();
		try{
			HttpGet request = new HttpGet(url);
			CloseableHttpResponse resp = client.execute(request);
			
			result = EntityUtils.toString(resp.getEntity());
			
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				client.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		return null;
	}

	// ��ȡ���еı༭�Ƽ���֪������
	public static ArrayList<Zhihu> GetRecommendations(String content) {
		// Ԥ����һ��ArrayList���洢���
		ArrayList<Zhihu> results = new ArrayList<Zhihu>();
		Document doc = Jsoup.parse(content);
		Elements items =  doc.getElementsByClass("zm-item");
		for(Element item:items){
			Element h2TagEle = item.getElementsByTag("h2").first();
			Element aTagEl = h2TagEle.getElementsByTag("a").first();
			String href = aTagEl.attr("href");
			if(href.contains("question")){
				results.add(new Zhihu(href));
			}
		}
		return results;
	}

}
