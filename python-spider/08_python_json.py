import json

json_str = '''
[{"num":"0001","name":"xiaohe"}]'''
rs = json.loads(json_str)

json_str = json.dumps(rs, ensure_ascii=False)
print(json_str)

with open('data/json/test1.json', 'w') as fp:
    json.dump(rs, fp, ensure_ascii=False)
