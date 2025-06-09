# https://motion.baidu.com/activity/yiqing_bigdata/trend?tab=symptom&source=%E5%85%A8%E5%9B%BD%E7%96%AB%E6%83%85
# https://wp.m.163.com/163/page/news/virus_report/index.html?_nw_=1&_anw_=1
# https://news.sina.cn/zt_d/yiqing0121
import requests
from bs4 import BeautifulSoup
import json
import re

response = requests.get(
    'https://motion.baidu.com/activity/yiqing_bigdata/trend?tab=symptom&source=%E5%85%A8%E5%9B%BD%E7%96%AB%E6%83%85')

page = response.content.decode()
soup = BeautifulSoup(page, 'lxml')

script = soup.find(id='')
countries_text = script.text

json_str = re.findall(r'(\[.+\])', countries_text)[0]

last_day_corona_virus = json.loads(json_str)

with open('data/last_day_corona_virus.json', 'w') as fp:
    json.dump(last_day_corona_virus, fp, ensure_ascii=False)
