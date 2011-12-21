import socket
import sys
import time
import re
import random
import string

class _Getch:
	"""Gets a single character from standard input.  Does not echo to the
screen."""
	def __init__(self):
		try:
			self.impl = _GetchWindows()
		except ImportError:
			self.impl = _GetchUnix()

	def __call__(self): return self.impl()


class _GetchUnix:
	def __init__(self):
		import tty, sys

	def __call__(self):
		import sys, tty, termios
		fd = sys.stdin.fileno()
		old_settings = termios.tcgetattr(fd)
		try:
			tty.setraw(sys.stdin.fileno())
			ch = sys.stdin.read(1)
		finally:
			termios.tcsetattr(fd, termios.TCSADRAIN, old_settings)
		return ch


class _GetchWindows:
	def __init__(self):
		import msvcrt

	def __call__(self):
		import msvcrt
		return msvcrt.getch()


getch = _Getch()

usage = '''Usage:
L - LOGIN
K - JOIN
J - GET_UPDATE
H - GET_UPDATE_OK

WASD - ACTION MOVEMENT
T - ACTION_SHOOT
E - ACTION_ENTEREXIT

I - SHOW THIS INFO
Q - QUIT
'''

def sendMessage(sock,sessionid,message,messageid):
	assembledMSG = 'M '+sessionid+' SEND '+messageid+' '+message+'\n'
	print('SEND: '+assembledMSG)
	sock.send(bytes(assembledMSG,'utf-8'))
	
	#actions are not answered
	if(not message.startswith('ACTION')):
		data = b''
		char = sock.recv(1)
		while(char.decode('utf-8')!='\n'):
			char = sock.recv(1)
			data += char
		respstr = data.decode('utf-8')
		print('RESP: '+respstr)
		
		if(message.startswith('LOGIN')):
			sID = re.findall('\s(\w+?)\s',respstr)[0];
			print('setting session ID to '+sID)
			global sessionID
			sessionID = sID
		if(message.startswith('GETUPDATE')):
			rID = re.findall('GETUPDATE_OK\s(\d+?)\s',respstr)[0];
			print('setting revision ID to '+rID)
			global revisionID
			revisionID = rID
		

ip = input('Please enter target ip (default: 127.0.0.1):')
if ip == '':
	ip = '127.0.0.1'
username = input('Username (default: random):')
if username == '':
	username = 'USER_'+random.choice(string.ascii_uppercase)+random.choice(string.ascii_uppercase)
password = input('Password (default: random):')
if password == '':
	password = 'PASS_'+random.choice(string.ascii_uppercase)+random.choice(string.ascii_uppercase)
print(usage)
s=None
while(s==None):
	try:
		s = socket.create_connection((ip,8095))
	except socket.error:
		print('Connection refused... retring in 5s.')
		time.sleep(5)
print('Connection established.')	
a=''	

sessionID = '1'
messageID = '123'
revisionID = '0'

while(True):
	#fake switch statement
	if (a=='l' ):
		sendMessage(s,sessionID,'LOGIN '+username+' '+password,messageID)
	if (a=='j' ):
		sendMessage(s,sessionID,'GETUPDATE '+revisionID,messageID)
	if (a=='k' ):
		sendMessage(s,sessionID,'JOIN',messageID)
	if (a=='h' ):
		sendMessage(s,sessionID,'GETUPDATE_OK',messageID)
	if (a=='w' ):
		sendMessage(s,sessionID,'ACTION_ACCELERATE',messageID)
	if (a=='a' ):
		sendMessage(s,sessionID,'ACTION_LEFT',messageID)
	if (a=='s' ):
		sendMessage(s,sessionID,'ACTION_DECELERATE',messageID)
	if (a=='d' ):
		sendMessage(s,sessionID,'ACTION_RIGHT',messageID)
	
	if (a=='t' ):
		sendMessage(s,sessionID,'ACTION_SHOOT',messageID)
	if (a=='e' ):
		sendMessage(s,sessionID,'ACTION_ENTEREXIT',messageID)
	
	if (a=='i' ):
		print(usage)
	
	if (a=='q' ):
		break
	a = getch()

print('Closing connection...')
s.close()
print('bye.')
