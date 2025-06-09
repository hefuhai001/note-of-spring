import requests

# https://image.baidu.com/search/acjson?tn=resultjson_com&logid=11567165614427862837&ipn=rj&ct=201326592&is=&fp=result&fr=&word=%E5%A3%81%E7%BA%B8&cg=wallpaper&queryWord=%E5%A3%81%E7%BA%B8&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&hd=&latest=&copyright=&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&expermode=&nojc=&isAsync=&pn=90&rn=30&gsm=5a&1671434990375=
url = 'https://image.baidu.com/search/acjson?tn=resultjson_com&logid=11567165614427862837&ipn=rj&ct=201326592&is=&fp=result&fr=&word=%E5%A3%81%E7%BA%B8&cg=wallpaper&queryWord=%E5%A3%81%E7%BA%B8&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&hd=&latest=&copyright=&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&expermode=&nojc=&isAsync=&pn=270&rn=90&gsm=5a&1671434990375='

# 伪装成浏览器
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36 SLBrowser/8.0.0.9071 SLBChan/105'}

# 发送请求
resp = requests.get(url, headers=headers)
resp.encoding = 'utf-8'
# print(resp.text)
# print(resp.request.headers)
resp_json = resp.json()
data_list = resp_json['data']

# print(data_list)
lst = []
for item in data_list:
    # 判断字典里面有没有数据
    if len(item) != 0:
        lst.append(item['thumbURL'])
# print(lst)

count = 240
# 用作图片名称及排序
for item in lst:
    # 请求图片
    resp = requests.get(item, headers=headers)
    count += 1

    with open('data/img/' + str(count) + '.jpg', 'wb') as file:
        file.write(resp.content)
        print('正在下载：%s---------%s' % (count, item))

print('图片爬取完毕！')
