import requests

# url
url = 'https://www.baidu.com'

# 伪装成浏览器
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36 SLBrowser/8.0.0.9071 SLBChan/105'}

# python发送请求
# resp = requests.get(url)

# 浏览器发送请求
html = requests.get(url, headers=headers)
html.encoding = 'utf-8'

# print(html.text)
# print(html.request.headers)

with open('data/json/baidu_browser.html', 'w') as fp:
    fp.write(html.text)

print('保存完毕！')
