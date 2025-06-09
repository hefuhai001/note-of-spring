import requests
from bs4 import BeautifulSoup
import json
import re


class CoronaVirusSprider(object):
    def __init__(self):
        self.home_url = 'https://motion.baidu.com/activity/yiqing_bigdata/trend?tab=symptom&source=%E5%85%A8%E5%9B%BD%E7%96%AB%E6%83%85'

    def get_content_from_url(self, url):
        '''
        根据url，获取响应内容的字符串数据
        :param url:请求的内容
        :return:响应内容字符串
        '''
        response = requests.get(url)
        return response.content.decode()

    def parse_home_page(self, home_page):
        '''
        解析首页内容获取解析后的python数据
        :param home_page:首页的内容
        :return:解析后的python数据
        '''
        soup = BeautifulSoup(home_page, 'lxml')
        script = soup.find(id='')
        countries_text = script.text
        json_str = re.findall(r'(\[.+\])', countries_text)[0]
        data = json.loads(json_str)
        return data

    def save(self, data, path):
        with open(path, 'w') as fp:
            json.dump(data, fp, ensure_ascii=False)

    def crawl_last_day_corona_virus(self):
        '''
        采集数据
        :return:
        '''
        home_page = self.get_content_from_url(self.home_url)
        last_day_corona_virus = self.parse_home_page(home_page)
        self.save(last_day_corona_virus, 'data/last_day_corona_virus.json')

    def run(self):
        self.crawl_last_day_corona_virus()


if __name__ == '__main__':
    spider = CoronaVirusSprider()
    spider.run()
