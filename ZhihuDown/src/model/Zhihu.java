package model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import controller.Spider;

public class Zhihu {
	public String question;// ����
	public String questionDescription;// ��������
	public String zhihuUrl;// ��ҳ����
	public ArrayList<String> answers;// �洢���лش������

	// ���췽����ʼ������
	public Zhihu(String url) {
		// ��ʼ������
		question = "";
		questionDescription = "";
		zhihuUrl = "";
		answers = new ArrayList<String>();

		// �ж�url�Ƿ�Ϸ�
		if (getRealUrl(url)) {
			//System.out.println("����ץȡ" + zhihuUrl);
			// ����url��ȡ���ʴ��ϸ��
			String content = Spider.SendGet(zhihuUrl);
			if(content != null){
				Document doc = Jsoup.parse(content);
				// ƥ�����
				question = doc.title();
				
				// ƥ������
				Element despElement = doc.getElementById("zh-question-detail");
				if(despElement != null){
					questionDescription = despElement.text();
				}
				// ƥ���
				Elements ansItems = doc.getElementsByClass("zm-item-answer");
				for(Element ansItem:ansItems){
					if(ansItem.hasClass("zm-item-rich-text")){
						Element textElement = ansItem.getElementsByClass("zm-item-rich-text").first();
						if(despElement != null){
							answers.add(textElement.text());
						}
					}
				}
			}else{
				System.out.println("content is null");
			}
		}
	}

	// ����url
	boolean getRealUrl(String url) {
		// ��http://www.zhihu.com/question/22355264/answer/21102139
		// ת����http://www.zhihu.com/question/22355264
		// ���򲻱�
		Pattern pattern = Pattern.compile("question/(.*?)/");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			zhihuUrl = "http://www.zhihu.com/question/" + matcher.group(1);
		} else {
			return false;
		}
		return true;
	}

	public String writeString() {
		// ƴ��д�뱾�ص��ַ���
		String result = "";
		result += "���⣺" + question + "\r\n";
		result += "������" + questionDescription + "\r\n";
		result += "���ӣ�" + zhihuUrl + "\r\n\r\n";
		for (int i = 0; i < answers.size(); i++) {
			result += "�ش�" + i + "��" + answers.get(i) + "\r\n\r\n\r\n";
		}
		result += "\r\n\r\n\r\n\r\n\r\n\r\n";
		// �����е�html��ǩ����ɸѡ
		result = result.replaceAll("<br>", "\r\n");
		result = result.replaceAll("<.*?>", "");
		return result;
	}

	@Override
	public String toString() {
		String result = "";
		result += "���⣺" + question + "\n";
		result += "������" + questionDescription + "\n";
		result += "���ӣ�" + zhihuUrl + "\n";
		result += "�ش�" + answers.size() + "\n";
		return result;
	}
}
