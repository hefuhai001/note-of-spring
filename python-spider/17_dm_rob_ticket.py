from selenium import webdriver
import os
import time
import pickle

base_url = 'https://www.damai.cn/'
login_url = 'https://passport.damai.cn/login?ru=https%3A%2F%2Fwww.damai.cn%2F'
target_url = 'https://detail.damai.cn/item.htm?spm=a2oeg.home.card_0.ditem_1.591b23e1R1U74M&id=695017529352'


class Concert:

    def __init__(self):
        self.status = 0
        self.login_method = 1
        self.driver = webdriver.Edge()

    def set_cookies(self):
        self.driver.get(login_url)
        print('请扫码登录')
        time.sleep(5)
        print('登录成功')
        pickle.dump(self.driver.get_cookies(), open('data/json/cookies.pkl', 'wb'))
        print('cookie保存成功')
        self.driver.get(target_url)

    def get_cookie(self):
        cookies = pickle.load(open('data/json/cookies.pkl', 'rb'))
        for cookie in cookies:
            # print(cookie)
            cookie_dict = {
                'domain': '.damai.cn',
                'name': cookie.get('name'),
                'value': cookie.get('value')
            }
            self.driver.add_cookie(cookie_dict)
        print('转入cookie成功')

    def login(self):
        if self.login_method == 0:
            self.driver.get(login_url)
        elif self.login_method == 1:
            if not os.path.exists('cookies.pkl'):
                self.set_cookies()
            else:
                self.driver.get(target_url)
                self.get_cookie()

    def enter_concert(self):
        print('打开浏览器')
        self.login()
        self.driver.refresh()
        self.status = 2
        print('登陆成功')

    def choose_ticket(self):
        if self.status == 2:
            print('开始选票')
            while self.driver.title.find('确认订单') == 1:
                buybutton = self.driver.find_element(self, 'Buybtn').text
            if buybutton == '缺货':
                self.driver.refresh()
            elif buybutton == '立即购票':
                self.driver.find_element('Buybtn').click()
            elif buybutton == '选座购票':
                self.driver.find_element('Buybtn').click()
                self.status = 3
            else:
                self.status = 100


if __name__ == '__main__':
    con = Concert()
    con.enter_concert()
