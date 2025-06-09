import requests
from pyquery import PyQuery

url = 'https://pvp.qq.com/web201605/herolist.shtml'
html = requests.get(url).content
# print(html)

doc = PyQuery(html)

items = doc('.herolist > li').items()
# print(items)
# 生成迭代对象
count = 1

# 循环遍历
for item in items:
    # print(item)
    url = item.find('img').attr('src')
    # print(url)
    urls = 'http:' + url
    name = item.find('a').text()
    # print(name)
    url_content = requests.get(urls).content
    # 下载 w write b bytes 二进制写入
    count += 1

    with open('data/weixinList/' + str(count) + '.jpg', 'wb') as file:
        # 保存
        file.write(url_content)
        print('正在下载：%s%s---------%s' % (count, name, urls))

        # print('正在下载：%s---------%s' % (name, urls))

print('下载完毕')
