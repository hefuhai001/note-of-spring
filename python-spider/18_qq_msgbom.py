# import pynput
from pynput.keyboard import Controller

keyBoard = Controller()

for i in range(100):
    keyBoard.type('这是第{}次python输入'.format(i))
