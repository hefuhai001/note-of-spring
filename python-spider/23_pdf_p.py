import PyPDF3
import json

def pdf2json(file_path, json_path):
    # 打开pdf文件
    with open(file_path, 'rb') as pdf_file:
        # 创建一个pdf阅读器对象
        pdf_reader = PyPDF3.PdfFileReader(pdf_file)
        # 初始化一个字典类型，存储每一页的文本内容
        pdf_data = {}
        # 循环遍历pdf文件的每一页
        for page_num in range(pdf_reader.getNumPages()):
            # 获取当前页面对象
            page = pdf_reader.getPage(page_num)
            # 将页面转换成文本内容
            page_text = page.extractText()
            # 将文本内容转换成UTF-8编码格式，并去除可能存在的换行符和空格
            page_text = page_text.encode('utf-8').decode('unicode_escape').replace('\n', '').replace('\r', '').replace(
                '\t', '').strip()
            # 将当前页的文本内容存储到pdf_data字典中
            pdf_data[str(page_num + 1)] = page_text
        # 将pdf_data字典转换成JSON格式，并写入到指定目录下的文件中
        with open(json_path, 'w', encoding='utf-8') as f:
            json.dump(pdf_data, f, ensure_ascii=False)


# 测试代码
pdf_file_path = 'D:/__easyProjects__/PyCharm/pythonSpider/data/example.pdf'
json_file_path = 'D:/__easyProjects__/PyCharm/pythonSpider/data/example.json'
pdf2json(pdf_file_path, json_file_path)