import re
import requests

# response = requests.get('https://www.baidu.com')
# html = response.content.decode()
# print(response.content.decode())

# 1.匹配模式
rs = re.findall('m..v', 'm\nav', re.DOTALL)
print('匹配模式:', rs)  # ['m\nav']

# 2.分组匹配
rs = re.findall('m(.+)v', 'm\nav', re.DOTALL)
print('分组匹配:', rs)  # ['\na']

# 2.r原串
rs = re.findall('m\\\\nav', 'm\\nav', re.DOTALL)
print('r原串:', rs)  # ['m\\nav']
rs = re.findall(r'm\\nav', 'm\\nav', re.DOTALL)
print('r原串:', rs)  # ['m\\nav']
