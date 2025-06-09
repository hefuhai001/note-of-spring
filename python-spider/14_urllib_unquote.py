# import requests
import urllib.parse

kw = {'city': "北京"}

# 编码
result = urllib.parse.urlencode(kw)
print(result)

# 解码
result = urllib.parse.unquote(result)
print(result)
