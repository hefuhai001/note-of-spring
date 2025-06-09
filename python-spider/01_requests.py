import requests

response = requests.get('https://www.baidu.com')
html = response.content.decode()
print(html)

with open('data/json/baidu.html', 'w') as fp:
    fp.write(html)
print('保存完毕！')
