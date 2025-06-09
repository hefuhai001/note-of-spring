import requests
from bs4 import BeautifulSoup

response = requests.get('https://www.baidu.com')
# print(response.content.decode())
html = response.content.decode()
soup = BeautifulSoup(html, 'lxml')

# 1.标签查找
title = soup.find('title')
print('title:', title)
# a = soup.find('a')
# print(a)
a_s = soup.find_all('a')
print('a:', a_s)

# 2.id&&class查找
# a = soup.find(id='mnav')
# print(a)
a = soup.find(attrs={'class': 'mnav'})
print('class:', a)

# 3.文本查找
text = soup.find(text='意见反馈')
print('text:', text)

# 4.Tag对象
print(type(a))
print('标签名:', a.name)
print('标签所有属性:', a.attrs)
print('标签文本:', a.text)
