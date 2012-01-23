import socket
import sys
import time
import re
import random
import string
from _thread import start_new_thread

profiles = [ ('robot','robot'), ('tom','secretpassword'), ('til','secretpassword'), ('jan','secretpassword') ]
robotmode = False
password = ''
username = ''
ip = ''
automode = False


class DataMap:
	def __init__(self, entries = None):
		self.entries = []
		if (entries != None):
			for (key,val) in entries.items():
				self.add(MapEntry(key,val))
	
	def add(self, entry):
		self.entries.append(entry)
	
	def __str__(self):
		return '{ ' + ''.join([str(entry) for entry in self.entries]) + ' }'
			
class MapEntry:
	def __init__(self, key, value):
		self.key = key
		self.value = value
	def __str__(self):
		return self.key + ' : ' + self.value + ' ; '
		
class ListNode:
	def __init__(self, elements = None):
		self.elements = [] if elements == None else elements
	
	def add(self, element):
		self.elements.append(element)
		
	def __str__(self):
		return ' [ ' + ' , '.join(self.elements) + ' ] '

def sendMessage(sock,sessionid,message,messageid):
	assembledMSG = 'M '+sessionid+' [ SEND '+messageid+' '+message+' , ]\n'
	if( not robotmode ):
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
		if( not robotmode ):
			print('RESP: '+respstr)
		
		if(message.startswith('LOGIN')):
			sID = re.findall('\s(\w+?)\s',respstr)[0];
			print('setting session ID to '+sID)
			return sID
			#global sessionID
			#sessionID = sID
		if(message.startswith('GETUPDATE')):
			rID = re.findall('GETUPDATE_OK\D+(\d+?)\s',respstr);
			if(len(rID)>0):
				rID = rID[0]
			else:
				print('unable to fetch revision!')
				return '0'
			if( robotmode ):
				print(username)
			print('setting revision ID to '+rID)
			return rID
			#global revisionID
			#revisionID = rID
		
def cli(ip,username,password,robotmode):
	ip = input('Please enter target ip (default: 127.0.0.1):')
	if ip == '':
		ip = '127.0.0.1'
	
	
	print('Profiles: ')
	for i in range(len(profiles)):
		print(str(i)+' -> '+profiles[i][0])

	username = input('Username (default: random):')

	try:
		if int(username) in range(len(profiles)):
			print('using profile: '+profiles[int(username)][0])
			if(profiles[int(username)][0] == 'robot'):
				robotmode = True
				username = ''
				password = ''
			else:
				password = profiles[int(username)][1]
				username = profiles[int(username)][0]
	except ValueError:
			print('ommiting profiles.')
	print(usage)
	return (ip,username,password,robotmode)
	
def main(ip,username,password,robotmode):
	if (username == ''):
		if(robotmode):
			username = 'ROBOT_'+random.choice(string.ascii_uppercase)+random.choice(string.ascii_uppercase)
			password = 'PASS'
		else:
			username = 'USER_'+random.choice(string.ascii_uppercase)+random.choice(string.ascii_uppercase)
		
	if(password==''):
		password = input('Password (default: PASS):')
		if password == '':
			password = 'PASS'

	s=None
	while(s==None):
		try:
			s = socket.create_connection((ip,8095))
		except socket.error:
			print('Connection refused... retring in 5s. Hit ctrl-c to cancel')
			time.sleep(5)
	print('Connection established.')	
	a=''	

	sessionID = '1'
	messageID = '123'
	revisionID = '0'

	if(robotmode):
		print('Robot mode enabled! hit ctrl-c to stop')
		
		sendMessage(s,sessionID,'REGISTER '+username+' '+password,messageID)
		sessionID = sendMessage(s,sessionID,'LOGIN '+username+' '+password,messageID)
		sendMessage(s,sessionID,'JOIN',messageID)
		while(True):
			revisionID = sendMessage(s,sessionID,'GETUPDATE '+revisionID,messageID)
			a = random.randint(0,5)
			if( a==0):
				sendMessage(s,sessionID,'ACTION_ACCELERATE',messageID)
			if( a==1):
				sendMessage(s,sessionID,'ACTION_LEFT',messageID)
			if (a==2 ):
				sendMessage(s,sessionID,'ACTION_DECELERATE',messageID)
			if (a==3 ):
				sendMessage(s,sessionID,'ACTION_RIGHT',messageID)
			#if (a==4 ):
				#sendMessage(s,sessionID,'ACTION_SHOOT',messageID)
			if (a==5 ):
				sendMessage(s,sessionID,'ACTION_ENTEREXIT',messageID)
			time.sleep(0.05)
				
	else:
		while(True):
			#fake switch statement
			if (a=='l' ):
				sessionID = sendMessage(s,sessionID,'LOGIN ' + str(DataMap({'authuser': username, 'authpass': password })),messageID)
			if (a=='j' ):
				revisionID = sendMessage(s,sessionID,'GETUPDATE '+ str(DataMap({'rev': revisionID})),messageID)
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

if(len(sys.argv)>1):
	ip = sys.argv[1]
	robotmode = True
	if(len(sys.argv)==3):
		robotcount = int(sys.argv[2])
		print('Starting '+sys.argv[2]+' robot(s)')
		threads = []
		for i in range(robotcount):
			threads.append(start_new_thread(main,(ip,'','',True)))
		input('hit enter to stop threads')
		for i in range(len(threads)):
			threads[i].exit()
		
	else:
		print('Starting 1 robot')
		main(ip,'','',True)
else:
	rettup = cli(ip,username,password,robotmode)
	main(rettup[0],rettup[1],rettup[2],rettup[3])

