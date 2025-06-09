import requests

# 请求网站页面并打印html源码
url = 'https://wallpaper.seenav.cn/'
r = requests.get(url)
html = r.content.decode('utf-8')
print(html)
