import re
import requests

response = requests.get('https://www.baidu.com')
html = response.content.decode()
# print(response.content.decode())

# 字符匹配
rs = re.findall('mnav', html)
print('字符匹配:', rs)
rs = re.findall('m..v', html)
print('字符匹配:', rs)
rs = re.findall('m\.t', html)
print('字符匹配:', rs)
rs = re.findall('m[pa]t', html)
print('字符匹配:', rs)

# 预定义的字符集
rs = re.findall('\d', html)
print('数字:', rs)
rs = re.findall('m\w', html)
print('字符:', rs)

# 数量词
rs = re.findall('m\w*', html)
print('0次或多次:', rs)
rs = re.findall('m\w+', html)
print('1次或多次:', rs)
rs = re.findall('m\w?', html)
print('0次或1次:', rs)
rs = re.findall('m\w{7}', html)
print('指定次数:', rs)


