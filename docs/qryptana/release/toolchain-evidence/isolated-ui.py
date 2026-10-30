import ctypes as c, os, sys, subprocess, time, json, pathlib
os.environ.update(DISPLAY=':98',XAUTHORITY='/tmp/qryptana-isolated-x11/Xauthority')
x=c.CDLL('libX11.so.6'); xt=c.CDLL('libXtst.so.6')
def setup(name,args,ret=c.c_int):
 f=getattr(x,name);f.argtypes=args;f.restype=ret;return f
P=c.c_void_p; U=c.c_ulong; I=c.c_int
setup('XOpenDisplay',[c.c_char_p],P); d=x.XOpenDisplay(b':98'); assert d,'isolated display unavailable'
setup('XDefaultRootWindow',[P],U);root=x.XDefaultRootWindow(d)
setup('XQueryTree',[P,U,c.POINTER(U),c.POINTER(U),c.POINTER(c.POINTER(U)),c.POINTER(c.c_uint)])
setup('XFetchName',[P,U,c.POINTER(c.c_char_p)])
setup('XGetGeometry',[P,U,c.POINTER(U),c.POINTER(I),c.POINTER(I),c.POINTER(c.c_uint),c.POINTER(c.c_uint),c.POINTER(c.c_uint),c.POINTER(c.c_uint)])
setup('XFlush',[P]);setup('XSync',[P,I]);setup('XSetInputFocus',[P,U,I,U]);setup('XResizeWindow',[P,U,c.c_uint,c.c_uint]);setup('XMoveWindow',[P,U,I,I])
def windows(w):
 rr=U();pp=U();kids=c.POINTER(U)();n=c.c_uint();x.XQueryTree(d,w,c.byref(rr),c.byref(pp),c.byref(kids),c.byref(n))
 res=[]
 for k in list(kids[:n.value]):
  name=c.c_char_p();x.XFetchName(d,k,c.byref(name)); ww=c.c_uint();hh=c.c_uint();xx=I();yy=I();bb=c.c_uint();dd=c.c_uint()
  x.XGetGeometry(d,k,c.byref(rr),c.byref(xx),c.byref(yy),c.byref(ww),c.byref(hh),c.byref(bb),c.byref(dd))
  title=name.value.decode(errors='replace') if name.value else ''
  res.append(dict(id=k,title=title,x=xx.value,y=yy.value,w=ww.value,h=hh.value));res+=windows(k)
 return res
ws=windows(root)
if sys.argv[1]=='list':print(json.dumps(ws));sys.exit()
w=next(v for v in ws if v['title']=='Qryptana')
if sys.argv[1]=='snapshot':
 subprocess.run(['magick','import','-display',':98','-window',str(w['id']),sys.argv[2]],check=True);sys.exit()
x.XSync(d,0)
xt.XTestFakeMotionEvent.argtypes=[P,I,I,I,U];xt.XTestFakeButtonEvent.argtypes=[P,c.c_uint,I,U];xt.XTestFakeKeyEvent.argtypes=[P,c.c_uint,I,U]
def button(n):
 xt.XTestFakeButtonEvent(d,n,1,0);xt.XTestFakeButtonEvent(d,n,0,0)
if sys.argv[1]=='click':
 xt.XTestFakeMotionEvent(d,-1,w['x']+int(sys.argv[2]),w['y']+int(sys.argv[3]),0);button(1)
elif sys.argv[1]=='scroll':
 xt.XTestFakeMotionEvent(d,-1,w['x']+w['w']//2,w['y']+w['h']//2,0)
 for _ in range(abs(int(sys.argv[2]))):button(5 if int(sys.argv[2])>0 else 4)
elif sys.argv[1] in ['text','keys']:
 setup('XStringToKeysym',[c.c_char_p],U);setup('XKeysymToKeycode',[P,U],c.c_uint)
 for ch in (sys.argv[2] if sys.argv[1]=='text' else sys.argv[2].split()):
  k=x.XKeysymToKeycode(d,x.XStringToKeysym(('space' if ch==' ' else ch).encode()));xt.XTestFakeKeyEvent(d,k,1,0);xt.XTestFakeKeyEvent(d,k,0,0)
elif sys.argv[1]=='close':
 class Data(c.Union): _fields_=[('l',c.c_long*5),('pad',c.c_byte*20)]
 class ClientMessage(c.Structure): _fields_=[('type',I),('serial',U),('send_event',I),('display',P),('window',U),('message_type',U),('format',I),('data',Data)]
 class Event(c.Union): _fields_=[('client',ClientMessage),('pad',c.c_long*24)]
 setup('XInternAtom',[P,c.c_char_p,I],U);setup('XSendEvent',[P,U,I,c.c_long,c.POINTER(Event)])
 event=Event();event.client.type=33;event.client.display=d;event.client.window=w['id'];event.client.message_type=x.XInternAtom(d,b'WM_PROTOCOLS',0);event.client.format=32;event.client.data.l[0]=x.XInternAtom(d,b'WM_DELETE_WINDOW',0);event.client.data.l[1]=0
 x.XSendEvent(d,w['id'],0,0,c.byref(event))
elif sys.argv[1]=='resize': x.XResizeWindow(d,w['id'],int(sys.argv[2]),int(sys.argv[3]))
x.XSync(d,0)
time.sleep(0.8)
