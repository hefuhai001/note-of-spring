# D:\__easyProjects__\PyCharm\pythonSpider\data\example.pdf
# D:\__easyProjects__\PyCharm\pythonSpider\data\example.json
import PyPDF3
import json

# 打开PDF文件
with open('D:\__easyProjects__\PyCharm\pythonSpider\data\example.pdf', 'rb') as pdf_file:
    # 创建PDF阅读器对象
    pdf_reader = PyPDF3.PdfFileReader(pdf_file)

    # 创建一个空字典，用于存储PDF页面和内容
    pdf_dict = {}
    # 遍历PDF的所有页面，将页面内容存储到字典中
    for i in range(pdf_reader.getNumPages()):
        page = pdf_reader.getPage(i)
        page_content = page.extractText()
        pdf_dict[f"Page {i + 1}"] = page_content.replace('\n', '\n  ').replace('\u02d8', "").replace('\u00b4','')

    # 将字典转换为JSON格式
    json_data = json.dumps(pdf_dict)

# 将JSON数据写入文件
with open('D:\__easyProjects__\PyCharm\pythonSpider\data\example.json', mode='w', encoding='utf-8') as json_file:
    json_file.write(json_data)
    print("----------转化完成----------")
