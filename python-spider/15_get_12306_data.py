import requests

# url = 'https://www.baidu.com'
url = 'https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=2022-12-19&leftTicketDTO.from_station=BJP&leftTicketDTO.to_station=NOG&purpose_codes=ADULT'

# 伪装成浏览器
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36 SLBrowser/8.0.0.9071 SLBChan/105',
    'Cookie': '_uab_collina=167144088495117187886785; JSESSIONID=7788C0BF91AA477ED254082A1483E885; BIGipServerotn=3973513482.24610.0000; RAIL_EXPIRATION=1671769937873; RAIL_DEVICEID=ghAPvOxHJyKLjDSAJyKvTkrbZPX0dqSK4RWRmSf5KDsrOkGqjwkwNbDafdV9BmGv3qodR71BsXK9H9yaJkJDe2bfxksm9hG7NWLmSfiuJGdMun_wZrwoRipfyVtNpwS1ef80CtgN1i4Q3SUnrVIEw9HFvX9jvQPs; BIGipServerpassport=786956554.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off; fo=a7xfyatwh4vnsah9WUJw5CnEs9puiKwOPdpPhS0V2NngkaIqCXEgrps1DPVNqTdQ2AfoNDXs9GQ_P6dMnsAUiCE_zbn9f8RWt1XM-1kROYheecYmw2bHaaDv_lm1vfxpeG4Q6ZMXz1E5XZj-MgTGK9EL-Ku2FfsY132Dcb_km7ZhIqFsTww6IQpksPA; route=c5c62a339e7744272a54643b3be5bf64; _jc_save_fromStation=%u5317%u4EAC%2CBJP; _jc_save_toStation=%u5357%20%u660C%2CNOG; _jc_save_fromDate=2022-12-19; _jc_save_toDate=2022-12-19; _jc_save_wfdc_flag=dc; BIGipServerportal=3151233290.17695.0000'}

response = requests.get(url, headers=headers)
html = response.content.decode()
print(html)
