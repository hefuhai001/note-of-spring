import json

json_str = '''
[{"num":"0001","name":"xiaohe"}]'''

rs = json.loads(json_str)
print(rs)
print(type(rs))
print(type(rs[0]))

with open('data/json/test.json') as fp:
    python_list = json.load(fp)
    print(python_list)
    print(type(python_list))
    print(type(python_list[0]))
