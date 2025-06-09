import requests
from bs4 import BeautifulSoup

soup = BeautifulSoup('<html>data</html>', 'lxml')
print(soup)

response = requests.get('https://www.baidu.com')
html = response.content.decode()
# print(response.content.decode())
soup = BeautifulSoup(html, 'lxml')
print(soup)
